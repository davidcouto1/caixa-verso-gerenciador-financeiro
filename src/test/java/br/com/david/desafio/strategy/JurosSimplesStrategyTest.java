package br.com.david.desafio.strategy;

import br.com.david.desafio.entity.MemoriaCalculo;
import br.com.david.desafio.entity.Simulacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JurosSimplesStrategy - Testes de Cálculo de Juros Simples")
class JurosSimplesStrategyTest {

    private JurosSimplesStrategy strategy;
    private Simulacao simulacao;

    @BeforeEach
    void setUp() {
        strategy = new JurosSimplesStrategy();
        simulacao = new Simulacao();
    }

    @Test
    @DisplayName("Deve calcular juros simples corretamente com valores básicos")
    void deveCalcularJurosSimplesCorretamente() {
        // Given
        simulacao.setValorInicial(new BigDecimal("1000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("10.00")); // 10% ao mês
        simulacao.setPrazoMeses(3);

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then
        assertThat(simulacao.getMemoriaCalculos()).hasSize(3);
        // Juros simples: 1000 + (100 * 3) = 1300
        assertThat(simulacao.getValorTotalFinal()).isEqualByComparingTo(new BigDecimal("1300.00"));
        assertThat(simulacao.getValorTotalJuros()).isEqualByComparingTo(new BigDecimal("300.00"));

        // Verificar primeiro mês
        MemoriaCalculo mes1 = simulacao.getMemoriaCalculos().get(0);
        assertThat(mes1.getMes()).isEqualTo(1);
        assertThat(mes1.getSaldoInicial()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(mes1.getJuro()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(mes1.getSaldoFinal()).isEqualByComparingTo(new BigDecimal("1100.00"));
    }

    @Test
    @DisplayName("Deve calcular juros simples com taxa decimal")
    void deveCalcularComTaxaDecimal() {
        // Given
        simulacao.setValorInicial(new BigDecimal("5000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("1.5")); // 1.5% ao mês
        simulacao.setPrazoMeses(12);

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then
        assertThat(simulacao.getMemoriaCalculos()).hasSize(12);
        // 5000 + (5000 * 0.015 * 12) = 5900
        assertThat(simulacao.getValorTotalFinal()).isEqualByComparingTo(new BigDecimal("5900.00"));
        assertThat(simulacao.getValorTotalJuros()).isEqualByComparingTo(new BigDecimal("900.00"));
    }

    @Test
    @DisplayName("Deve calcular corretamente com um único mês")
    void deveCalcularComUmMes() {
        // Given
        simulacao.setValorInicial(new BigDecimal("2000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("5.0")); // 5%
        simulacao.setPrazoMeses(1);

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then
        assertThat(simulacao.getMemoriaCalculos()).hasSize(1);
        assertThat(simulacao.getValorTotalFinal()).isEqualByComparingTo(new BigDecimal("2100.00"));
        assertThat(simulacao.getValorTotalJuros()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Deve limpar memórias de cálculo anteriores ao recalcular")
    void deveLimparMemoriasAnteriores() {
        // Given
        simulacao.setValorInicial(new BigDecimal("1000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("5.0"));
        simulacao.setPrazoMeses(3);

        // Adicionar memórias fictícias
        simulacao.getMemoriaCalculos().add(new MemoriaCalculo(1, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        simulacao.getMemoriaCalculos().add(new MemoriaCalculo(2, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then
        assertThat(simulacao.getMemoriaCalculos()).hasSize(3);
        assertThat(simulacao.getMemoriaCalculos().get(0).getSaldoInicial()).isNotEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve associar simulação às memórias de cálculo")
    void deveAssociarSimulacaoAsMemorias() {
        // Given
        simulacao.setValorInicial(new BigDecimal("1000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("2.0"));
        simulacao.setPrazoMeses(2);

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then
        assertThat(simulacao.getMemoriaCalculos())
                .allMatch(memoria -> memoria.getSimulacao() == simulacao);
    }

    @Test
    @DisplayName("Deve calcular corretamente com prazo longo (360 meses)")
    void deveCalcularComPrazoLongo() {
        // Given
        simulacao.setValorInicial(new BigDecimal("10000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("1.0")); // 1% ao mês
        simulacao.setPrazoMeses(360);

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then
        assertThat(simulacao.getMemoriaCalculos()).hasSize(360);
        // 10000 + (10000 * 0.01 * 360) = 46000
        assertThat(simulacao.getValorTotalFinal()).isEqualByComparingTo(new BigDecimal("46000.00"));
        
        // Verificar último mês
        MemoriaCalculo ultimoMes = simulacao.getMemoriaCalculos().get(359);
        assertThat(ultimoMes.getMes()).isEqualTo(360);
        assertThat(ultimoMes.getSaldoFinal()).isEqualByComparingTo(simulacao.getValorTotalFinal());
    }

    @Test
    @DisplayName("Deve retornar nome correto da estratégia")
    void deveRetornarNomeEstrategia() {
        // When/Then
        assertThat(strategy.getNomeEstrategia()).isEqualTo("Juros Simples");
    }

    @Test
    @DisplayName("Deve calcular juros constantes a cada mês (característica de juros simples)")
    void deveCalcularJurosConstantes() {
        // Given
        simulacao.setValorInicial(new BigDecimal("1000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("10.0"));
        simulacao.setPrazoMeses(3);

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then - Juros devem ser constantes a cada mês (característica de juros simples)
        BigDecimal jurosMes1 = simulacao.getMemoriaCalculos().get(0).getJuro();
        BigDecimal jurosMes2 = simulacao.getMemoriaCalculos().get(1).getJuro();
        BigDecimal jurosMes3 = simulacao.getMemoriaCalculos().get(2).getJuro();

        assertThat(jurosMes1).isEqualByComparingTo(jurosMes2);
        assertThat(jurosMes2).isEqualByComparingTo(jurosMes3);
        assertThat(jurosMes1).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Deve ter saldo inicial crescente a cada mês")
    void deveTerSaldoInicialCrescente() {
        // Given
        simulacao.setValorInicial(new BigDecimal("1000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("5.0"));
        simulacao.setPrazoMeses(3);

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then
        BigDecimal saldoInicialMes1 = simulacao.getMemoriaCalculos().get(0).getSaldoInicial();
        BigDecimal saldoInicialMes2 = simulacao.getMemoriaCalculos().get(1).getSaldoInicial();
        BigDecimal saldoInicialMes3 = simulacao.getMemoriaCalculos().get(2).getSaldoInicial();

        assertThat(saldoInicialMes2).isGreaterThan(saldoInicialMes1);
        assertThat(saldoInicialMes3).isGreaterThan(saldoInicialMes2);
    }
}
