package br.com.david.desafio.factory;

import br.com.david.desafio.dto.SimulacaoRequestDTO;
import br.com.david.desafio.entity.Simulacao;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

/**
 * Factory para criação de entidades Simulacao.
 */
@ApplicationScoped
public class SimulacaoFactory {
    
    public Simulacao criarSimulacao(SimulacaoRequestDTO requestDTO) {
        Simulacao simulacao = new Simulacao();
        simulacao.setValorInicial(requestDTO.getValorInicial());
        simulacao.setTaxaJurosMensal(requestDTO.getTaxaJurosMensal());
        simulacao.setPrazoMeses(requestDTO.getPrazoMeses());
        simulacao.setDataCriacao(LocalDateTime.now());
        return simulacao;
    }
    
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
