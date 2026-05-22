package br.com.david.desafio.service;

import br.com.david.desafio.dto.MemoriaCalculoDTO;
import br.com.david.desafio.dto.SimulacaoRequestDTO;
import br.com.david.desafio.dto.SimulacaoResponseDTO;
import br.com.david.desafio.entity.MemoriaCalculo;
import br.com.david.desafio.entity.Simulacao;
import br.com.david.desafio.exception.SimulacaoNotFoundException;
import br.com.david.desafio.repository.SimulacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsável pela lógica de negócio das simulações de financiamento.
 * Implementa o cálculo de juros compostos com precisão financeira usando BigDecimal.
 */
@ApplicationScoped
public class SimulacaoService {

    private static final Logger LOG = LoggerFactory.getLogger(SimulacaoService.class);
    
    private static final int SCALE = 2; // Precisão de 2 casas decimais para valores monetários
    private static final int CALCULATION_SCALE = 10; // Precisão intermediária para cálculos
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final BigDecimal CEM = new BigDecimal("100");

    @Inject
    SimulacaoRepository simulacaoRepository;

    /**
     * Cria e persiste uma nova simulação de financiamento.
     * Calcula os juros compostos mês a mês e gera a memória de cálculo completa.
     *
     * @param requestDTO Dados de entrada da simulação
     * @return DTO com a simulação completa e memória de cálculo
     */
    @Transactional
    public SimulacaoResponseDTO criarSimulacao(SimulacaoRequestDTO requestDTO) {
        LOG.info("Iniciando criação de simulação: {}", requestDTO);
        
        // Validação dos dados de entrada
        validarDadosEntrada(requestDTO);
        
        // Criar entidade
        Simulacao simulacao = new Simulacao(
            requestDTO.getValorInicial(),
            requestDTO.getTaxaJurosMensal(),
            requestDTO.getPrazoMeses()
        );
        
        // Calcular juros compostos e gerar memória de cálculo
        calcularJurosCompostos(simulacao);
        
        // Persistir
        simulacaoRepository.persist(simulacao);
        
        LOG.info("Simulação criada com sucesso - ID: {}", simulacao.getId());
        
        return converterParaDTO(simulacao);
    }

    /**
     * Busca uma simulação existente por ID.
     *
     * @param id Identificador da simulação
     * @return DTO com a simulação completa
     * @throws SimulacaoNotFoundException se a simulação não for encontrada
     */
    public SimulacaoResponseDTO buscarSimulacao(Long id) {
        LOG.info("Buscando simulação com ID: {}", id);
        
        Simulacao simulacao = simulacaoRepository.findByIdWithMemoria(id);
        
        if (simulacao == null) {
            LOG.warn("Simulação não encontrada - ID: {}", id);
            throw new SimulacaoNotFoundException(id);
        }
        
        LOG.info("Simulação encontrada - ID: {}", id);
        return converterParaDTO(simulacao);
    }

    /**
     * Calcula os juros compostos mês a mês e popula a memória de cálculo.
     * Fórmula: Saldo Final = Saldo Inicial × (1 + Taxa/100)
     *
     * @param simulacao Entidade da simulação a ser calculada
     */
    private void calcularJurosCompostos(Simulacao simulacao) {
        LOG.debug("Calculando juros compostos para {} meses", simulacao.getPrazoMeses());
        
        // Converter taxa percentual para decimal (ex: 1.5% -> 0.015)
        BigDecimal taxaDecimal = simulacao.getTaxaJurosMensal()
            .divide(CEM, CALCULATION_SCALE, ROUNDING_MODE);
        
        // Fator multiplicador: (1 + taxa)
        BigDecimal fatorJuros = BigDecimal.ONE.add(taxaDecimal);
        
        BigDecimal saldoAtual = simulacao.getValorInicial();
        
        // Calcular cada mês
        for (int mes = 1; mes <= simulacao.getPrazoMeses(); mes++) {
            BigDecimal saldoInicial = saldoAtual.setScale(SCALE, ROUNDING_MODE);
            
            // Calcular novo saldo: Saldo × (1 + taxa)
            BigDecimal saldoFinal = saldoAtual.multiply(fatorJuros)
                .setScale(SCALE, ROUNDING_MODE);
            
            // Juros do mês = Saldo Final - Saldo Inicial
            BigDecimal juroMes = saldoFinal.subtract(saldoInicial);
            
            // Criar registro da memória de cálculo
            MemoriaCalculo memoria = new MemoriaCalculo(mes, saldoInicial, juroMes, saldoFinal);
            simulacao.adicionarMemoriaCalculo(memoria);
            
            // Atualizar saldo para próximo mês
            saldoAtual = saldoFinal;
            
            LOG.debug("Mês {}: Saldo Inicial={}, Juros={}, Saldo Final={}", 
                mes, saldoInicial, juroMes, saldoFinal);
        }
        
        // Calcular totais finais
        simulacao.calcularTotais();
        
        LOG.debug("Cálculo finalizado - Valor Total: {}, Total Juros: {}", 
            simulacao.getValorTotalFinal(), simulacao.getValorTotalJuros());
    }

    /**
     * Valida os dados de entrada da simulação.
     *
     * @param requestDTO Dados a serem validados
     * @throws IllegalArgumentException se algum dado for inválido
     */
    private void validarDadosEntrada(SimulacaoRequestDTO requestDTO) {
        if (requestDTO.getValorInicial().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor inicial deve ser maior que zero");
        }
        
        if (requestDTO.getTaxaJurosMensal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A taxa de juros deve ser maior que zero");
        }
        
        if (requestDTO.getPrazoMeses() <= 0) {
            throw new IllegalArgumentException("O prazo deve ser maior que zero");
        }
        
        if (requestDTO.getPrazoMeses() > 360) {
            throw new IllegalArgumentException("O prazo máximo é de 360 meses");
        }
    }

    /**
     * Converte a entidade Simulacao para o DTO de resposta.
     *
     * @param simulacao Entidade a ser convertida
     * @return DTO de resposta
     */
    private SimulacaoResponseDTO converterParaDTO(Simulacao simulacao) {
        List<MemoriaCalculoDTO> memoriaCalculosDTO = simulacao.getMemoriaCalculos().stream()
            .map(memoria -> new MemoriaCalculoDTO(
                memoria.getMes(),
                memoria.getSaldoInicial(),
                memoria.getJuro(),
                memoria.getSaldoFinal()
            ))
            .collect(Collectors.toList());
        
        return new SimulacaoResponseDTO(
            simulacao.getId(),
            simulacao.getValorInicial(),
            simulacao.getTaxaJurosMensal(),
            simulacao.getPrazoMeses(),
            simulacao.getValorTotalFinal(),
            simulacao.getValorTotalJuros(),
            memoriaCalculosDTO
        );
    }
}
