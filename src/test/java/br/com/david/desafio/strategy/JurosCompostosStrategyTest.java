package br.com.david.desafio.strategy;

import br.com.david.desafio.entity.MemoriaCalculo;
import br.com.david.desafio.entity.Simulacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JurosCompostosStrategy - Testes de Cálculo de Juros Compostos")
class JurosCompostosStrategyTest {

    private JurosCompostosStrategy strategy;
    private Simulacao simulacao;

    @BeforeEach
    void setUp() {
        strategy = new JurosCompostosStrategy();
        simulacao = new Simulacao();
    }

    @Test
    @DisplayName("Deve calcular juros compostos corretamente com valores simples")
    void deveCalcularJurosCompostosCorretamente() {
        // Given
        simulacao.setValorInicial(new BigDecimal("1000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("10.00")); // 10% ao mês
        simulacao.setPrazoMeses(3);

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then
        assertThat(simulacao.getMemoriaCalculos()).hasSize(3);
        assertThat(simulacao.getValorTotalFinal()).isEqualByComparingTo(new BigDecimal("1331.00")); // 1000 * 1.1^3
        assertThat(simulacao.getValorTotalJuros()).isEqualByComparingTo(new BigDecimal("331.00"));

        // Verificar primeiro mês
        MemoriaCalculo mes1 = simulacao.getMemoriaCalculos().get(0);
        assertThat(mes1.getMes()).isEqualTo(1);
        assertThat(mes1.getSaldoInicial()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(mes1.getJuro()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(mes1.getSaldoFinal()).isEqualByComparingTo(new BigDecimal("1100.00"));
    }

    @Test
    @DisplayName("Deve calcular juros compostos com taxa decimal")
    void deveCalcularComTaxaDecimal() {
        // Given
        simulacao.setValorInicial(new BigDecimal("5000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("1.5")); // 1.5% ao mês
        simulacao.setPrazoMeses(12);

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then
        assertThat(simulacao.getMemoriaCalculos()).hasSize(12);
        assertThat(simulacao.getValorTotalFinal()).isGreaterThan(new BigDecimal("5000.00"));
        assertThat(simulacao.getValorTotalFinal()).isLessThan(new BigDecimal("6000.00"));
        
        // Aproximadamente 5000 * (1.015)^12 ≈ 5978.09 (mas com arredondamento pode ser 5978.11)
        assertThat(simulacao.getValorTotalFinal()).isEqualByComparingTo(new BigDecimal("5978.11"));
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
        assertThat(simulacao.getValorTotalFinal()).isGreaterThan(new BigDecimal("10000.00"));
        
        // Verificar último mês
        MemoriaCalculo ultimoMes = simulacao.getMemoriaCalculos().get(359);
        assertThat(ultimoMes.getMes()).isEqualTo(360);
        assertThat(ultimoMes.getSaldoFinal()).isEqualByComparingTo(simulacao.getValorTotalFinal());
    }

    @Test
    @DisplayName("Deve retornar nome correto da estratégia")
    void deveRetornarNomeEstrategia() {
        // When/Then
        assertThat(strategy.getNomeEstrategia()).isEqualTo("Juros Compostos");
    }

    @Test
    @DisplayName("Deve calcular juros crescentes a cada mês (efeito composto)")
    void deveCalcularJurosCrescentes() {
        // Given
        simulacao.setValorInicial(new BigDecimal("1000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("10.0"));
        simulacao.setPrazoMeses(3);

        // When
        strategy.calcular(simulacao, 2, 10);

        // Then - Juros devem crescer a cada mês devido ao efeito composto
        BigDecimal jurosMes1 = simulacao.getMemoriaCalculos().get(0).getJuro();
        BigDecimal jurosMes2 = simulacao.getMemoriaCalculos().get(1).getJuro();
        BigDecimal jurosMes3 = simulacao.getMemoriaCalculos().get(2).getJuro();

        assertThat(jurosMes2).isGreaterThan(jurosMes1);
        assertThat(jurosMes3).isGreaterThan(jurosMes2);
    }
}
