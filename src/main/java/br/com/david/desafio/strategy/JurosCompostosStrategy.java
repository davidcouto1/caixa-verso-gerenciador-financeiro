package br.com.david.desafio.strategy;

import br.com.david.desafio.entity.MemoriaCalculo;
import br.com.david.desafio.entity.Simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cálculo de juros compostos.
 * Fórmula: Saldo Final = Saldo Inicial × (1 + Taxa/100)
 */
public class JurosCompostosStrategy implements CalculoJurosStrategy {
    
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final BigDecimal CEM = new BigDecimal("100");

    @Override
    public void calcular(Simulacao simulacao, int scaleMonetario, int scaleIntermediario) {
        BigDecimal saldoAtual = simulacao.getValorInicial();
        BigDecimal taxaDecimal = simulacao.getTaxaJurosMensal().divide(CEM, scaleIntermediario, ROUNDING_MODE);
        BigDecimal fatorJuros = BigDecimal.ONE.add(taxaDecimal);
        
        simulacao.getMemoriaCalculos().clear();
        
        // Calcula mês a mês aplicando juros sobre o saldo
        for (int mes = 1; mes <= simulacao.getPrazoMeses(); mes++) {
            BigDecimal saldoInicial = saldoAtual;
            BigDecimal jurosMes = saldoInicial.multiply(taxaDecimal).setScale(scaleMonetario, ROUNDING_MODE);
            
            saldoAtual = saldoInicial.multiply(fatorJuros)
                    .setScale(scaleMonetario, ROUNDING_MODE);
            
            MemoriaCalculo memoria = new MemoriaCalculo(mes, saldoInicial, jurosMes, saldoAtual);
            memoria.setSimulacao(simulacao);
            simulacao.getMemoriaCalculos().add(memoria);
        }
        
        simulacao.setValorTotalFinal(saldoAtual);
        simulacao.setValorTotalJuros(
            saldoAtual.subtract(simulacao.getValorInicial())
                    .setScale(scaleMonetario, ROUNDING_MODE)
        );
    }

    @Override
    public String getNomeEstrategia() {
        return "Juros Compostos";
    }
}
