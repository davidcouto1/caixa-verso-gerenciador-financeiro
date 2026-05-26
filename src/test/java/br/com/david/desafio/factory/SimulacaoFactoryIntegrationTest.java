package br.com.david.desafio.factory;

import br.com.david.desafio.entity.Simulacao;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@DisplayName("SimulacaoFactory - Testes de Integração")
class SimulacaoFactoryIntegrationTest {

    @Inject
    SimulacaoFactory factory;

    @Test
    @DisplayName("Deve criar simulação usando método com BigDecimal direto")
    void deveCriarSimulacaoComBigDecimalDireto() {
        // Given
        BigDecimal valorInicial = new BigDecimal("5000.00");
        BigDecimal taxaJuros = new BigDecimal("2.5");
        int prazo = 24;

        // When
        Simulacao simulacao = factory.criarSimulacao(valorInicial, taxaJuros, prazo);

        // Then
        assertThat(simulacao).isNotNull();
        assertThat(simulacao.getValorInicial()).isEqualByComparingTo(valorInicial);
        assertThat(simulacao.getTaxaJurosMensal()).isEqualByComparingTo(taxaJuros);
        assertThat(simulacao.getPrazoMeses()).isEqualTo(prazo);
        assertThat(simulacao.getDataCriacao()).isNotNull();
        assertThat(simulacao.getMemoriaCalculos()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Deve criar simulações independentes com método BigDecimal")
    void deveCriarSimulacoesIndependentesComBigDecimal() {
        // Given
        BigDecimal valor = new BigDecimal("1000.00");
        BigDecimal taxa = new BigDecimal("1.5");
        int prazo = 12;

        // When
        Simulacao simulacao1 = factory.criarSimulacao(valor, taxa, prazo);
        Simulacao simulacao2 = factory.criarSimulacao(valor, taxa, prazo);

        // Then
        assertThat(simulacao1).isNotSameAs(simulacao2);
        assertThat(simulacao1.getMemoriaCalculos()).isNotSameAs(simulacao2.getMemoriaCalculos());
    }

    @Test
    @DisplayName("Deve criar simulação com valores extremos usando BigDecimal")
    void deveCriarComValoresExtremosComBigDecimal() {
        // Given
        BigDecimal valorMinimo = new BigDecimal("0.01");
        BigDecimal taxaMinima = new BigDecimal("0.01");
        int prazoMinimo = 1;

        // When
        Simulacao simulacao = factory.criarSimulacao(valorMinimo, taxaMinima, prazoMinimo);

        // Then
        assertThat(simulacao.getValorInicial()).isEqualByComparingTo(valorMinimo);
        assertThat(simulacao.getTaxaJurosMensal()).isEqualByComparingTo(taxaMinima);
        assertThat(simulacao.getPrazoMeses()).isEqualTo(prazoMinimo);
    }
}
