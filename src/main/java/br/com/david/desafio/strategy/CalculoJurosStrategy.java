package br.com.david.desafio.strategy;

import br.com.david.desafio.entity.Simulacao;

/**
 * Interface Strategy para cálculo de juros.
 * Permite diferentes estratégias de cálculo (Compostos, Simples, etc.).
 * 
 * Padrão GoF: Strategy Pattern
 * 
 * @author David
 * @since 1.0
 */
public interface CalculoJurosStrategy {
    
    /**
     * Calcula os juros e preenche a memória de cálculo da simulação.
     * 
     * @param simulacao Simulação a ser calculada
     * @param scaleMonetario Escala para valores monetários (casas decimais)
     * @param scaleIntermediario Escala para cálculos intermediários
     */
    void calcular(Simulacao simulacao, int scaleMonetario, int scaleIntermediario);
    
    /**
     * Retorna o nome da estratégia de cálculo.
     * 
     * @return Nome da estratégia
     */
    String getNomeEstrategia();
}
