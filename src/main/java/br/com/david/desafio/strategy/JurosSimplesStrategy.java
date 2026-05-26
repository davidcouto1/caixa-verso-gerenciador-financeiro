package br.com.david.desafio.strategy;

import br.com.david.desafio.entity.MemoriaCalculo;
import br.com.david.desafio.entity.Simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementação da estratégia de cálculo de juros simples.
 * Fórmula: Juros Mes = Principal × Taxa/100 (constante todo mês)
 * 
 * Padrão GoF: Strategy Pattern (Implementação Concreta Alternativa)
 * Nota: NÃO usa @ApplicationScoped pois a instância é controlada por CalculoJurosConfig.
 * 
 * @author David
 * @since 1.0
 */
public class JurosSimplesStrategy implements CalculoJurosStrategy {
    
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final BigDecimal CEM = new BigDecimal("100");

    @Override
    public void calcular(Simulacao simulacao, int scaleMonetario, int scaleIntermediario) {
        BigDecimal principal = simulacao.getValorInicial();
        BigDecimal taxaDecimal = simulacao.getTaxaJurosMensal().divide(CEM, scaleIntermediario, ROUNDING_MODE);
        
        simulacao.getMemoriaCalculos().clear();
        BigDecimal saldoAtual = principal;
        
        for (int mes = 1; mes <= simulacao.getPrazoMeses(); mes++) {
            BigDecimal jurosMes = principal.multiply(taxaDecimal)
                    .setScale(scaleMonetario, ROUNDING_MODE);
            
            saldoAtual = saldoAtual.add(jurosMes);
            
            MemoriaCalculo memoria = new MemoriaCalculo(
                mes,
                saldoAtual.subtract(jurosMes),
                jurosMes,
                saldoAtual
            );
            memoria.setSimulacao(simulacao);
            simulacao.getMemoriaCalculos().add(memoria);
        }
        
        simulacao.setValorTotalFinal(saldoAtual);
        simulacao.setValorTotalJuros(
            saldoAtual.subtract(principal)
                    .setScale(scaleMonetario, ROUNDING_MODE)
        );
    }

    @Override
    public String getNomeEstrategia() {
        return "Juros Simples";
    }
}
