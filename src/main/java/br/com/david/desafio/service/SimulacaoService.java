package br.com.david.desafio.service;

import br.com.david.desafio.dto.MemoriaCalculoDTO;
import br.com.david.desafio.dto.SimulacaoRequestDTO;
import br.com.david.desafio.dto.SimulacaoResponseDTO;
import br.com.david.desafio.dto.builder.SimulacaoResponseBuilder;
import br.com.david.desafio.entity.Simulacao;
import br.com.david.desafio.exception.SimulacaoNotFoundException;
import br.com.david.desafio.factory.SimulacaoFactory;
import br.com.david.desafio.repository.ISimulacaoRepository;
import br.com.david.desafio.strategy.CalculoJurosStrategy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsável pela lógica de negócio das simulações de financiamento.
 * Implementa ISimulacaoService seguindo o Dependency Inversion Principle (SOLID).
 * 
 * Aplica padrões:
 * - Strategy Pattern: Para diferentes cálculos de juros
 * - Factory Pattern: Para criação de entidades
 * - Builder Pattern: Para construção de DTOs
 * - Dependency Inversion: Depende de abstrações (interfaces)
 */
@ApplicationScoped
public class SimulacaoService implements ISimulacaoService {

    private static final Logger LOG = LoggerFactory.getLogger(SimulacaoService.class);

    @ConfigProperty(name = "simulador.calculo.scale.monetario", defaultValue = "2")
    int scaleMonetario;
    
    @ConfigProperty(name = "simulador.calculo.scale.intermediario", defaultValue = "10")
    int scaleIntermediario;
    
    @ConfigProperty(name = "simulador.prazo.maximo.meses", defaultValue = "360")
    int prazoMaximoMeses;

    // Dependency Inversion: Depende da interface, não da implementação
    @Inject
    ISimulacaoRepository simulacaoRepository;
    
    // Strategy Pattern: Injeção da estratégia de cálculo
    @Inject
    CalculoJurosStrategy calculoJurosStrategy;
    
    // Factory Pattern: Para criação de entidades
    @Inject
    SimulacaoFactory simulacaoFactory;

    @Override
    @Transactional
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Retry(maxRetries = 3, delay = 500, maxDuration = 10000)
    public SimulacaoResponseDTO criarSimulacao(SimulacaoRequestDTO requestDTO) {
        LOG.info("Iniciando criação de simulação: {}", requestDTO);
        
        validarDadosEntrada(requestDTO);
        
        // Factory Pattern: Cria a entidade
        Simulacao simulacao = simulacaoFactory.criarSimulacao(requestDTO);
        
        // Strategy Pattern: Calcula os juros
        calculoJurosStrategy.calcular(simulacao, scaleMonetario, scaleIntermediario);
        
        simulacaoRepository.persist(simulacao);
        
        LOG.info("Simulação criada com sucesso - ID: {} usando estratégia: {}", 
                simulacao.getId(), calculoJurosStrategy.getNomeEstrategia());
        
        return converterParaDTO(simulacao);
    }

    @Override
    @Timeout(value = 3, unit = ChronoUnit.SECONDS)
    @Retry(maxRetries = 2, delay = 200)
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
     * Valida os dados de entrada da simulação.
     * Aplica o Single Responsibility Principle.
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
        
        if (requestDTO.getPrazoMeses() > prazoMaximoMeses) {
            throw new IllegalArgumentException("O prazo máximo é de " + prazoMaximoMeses + " meses");
        }
    }

    /**
     * Converte a entidade para DTO usando o Builder Pattern.
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
        
        // Builder Pattern: Construção fluente do DTO
        return SimulacaoResponseBuilder.builder()
            .id(simulacao.getId())
            .valorInicial(simulacao.getValorInicial())
            .taxaJurosMensal(simulacao.getTaxaJurosMensal())
            .prazoMeses(simulacao.getPrazoMeses())
            .valorTotalFinal(simulacao.getValorTotalFinal())
            .valorTotalJuros(simulacao.getValorTotalJuros())
            .memoriaCalculos(memoriaCalculosDTO)
            .build();
    }
}
