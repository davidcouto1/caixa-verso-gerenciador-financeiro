package br.com.david.desafio.config;

import br.com.david.desafio.strategy.CalculoJurosStrategy;
import br.com.david.desafio.strategy.JurosCompostosStrategy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * Configuração CDI para definir qual Strategy de cálculo de juros será usada.
 * 
 * Aplica Strategy Pattern com inversão de dependências.
 * Por padrão, usa Juros Compostos, mas pode ser facilmente alterado.
 * 
 * @author David
 * @since 1.0
 */
@ApplicationScoped
public class CalculoJurosConfig {
    
    /**
     * Produz a estratégia de cálculo de juros padrão do sistema.
     * 
     * Para alterar a estratégia, basta retornar outra implementação
     * (ex: new JurosSimplesStrategy()).
     * 
     * @return Estratégia de cálculo a ser usada
     */
    @Produces
    public CalculoJurosStrategy calculoJurosStrategy() {
        return new JurosCompostosStrategy();
    }
}
