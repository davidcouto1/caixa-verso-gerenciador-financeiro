package br.com.david.desafio.factory;

import br.com.david.desafio.dto.SimulacaoRequestDTO;
import br.com.david.desafio.entity.Simulacao;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

/**
 * Factory para criação de entidades Simulacao.
 * 
 * Padrão GoF: Factory Pattern
 * Benefícios: Centraliza a lógica de criação de objetos complexos
 * 
 * @author David
 * @since 1.0
 */
@ApplicationScoped
public class SimulacaoFactory {
    
    /**
     * Cria uma nova instância de Simulacao a partir de um DTO de request.
     * 
     * @param requestDTO DTO com os dados de entrada
     * @return Nova instância de Simulacao
     */
    public Simulacao criarSimulacao(SimulacaoRequestDTO requestDTO) {
        Simulacao simulacao = new Simulacao();
        simulacao.setValorInicial(requestDTO.getValorInicial());
        simulacao.setTaxaJurosMensal(requestDTO.getTaxaJurosMensal());
        simulacao.setPrazoMeses(requestDTO.getPrazoMeses());
        simulacao.setDataCriacao(LocalDateTime.now());
        return simulacao;
    }
    
    /**
     * Cria uma nova instância de Simulacao com valores específicos.
     * Útil para testes e cenários específicos.
     * 
     * @param valorInicial Valor inicial do financiamento
     * @param taxaJurosMensal Taxa de juros mensal (em percentual)
     * @param prazoMeses Prazo em meses
     * @return Nova instância de Simulacao
     */
    public Simulacao criarSimulacao(
            java.math.BigDecimal valorInicial,
            java.math.BigDecimal taxaJurosMensal,
            int prazoMeses) {
        Simulacao simulacao = new Simulacao();
        simulacao.setValorInicial(valorInicial);
        simulacao.setTaxaJurosMensal(taxaJurosMensal);
        simulacao.setPrazoMeses(prazoMeses);
        simulacao.setDataCriacao(LocalDateTime.now());
        return simulacao;
    }
}
