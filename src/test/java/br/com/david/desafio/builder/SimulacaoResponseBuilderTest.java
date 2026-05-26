package br.com.david.desafio.builder;

import br.com.david.desafio.dto.MemoriaCalculoDTO;
import br.com.david.desafio.dto.SimulacaoResponseDTO;
import br.com.david.desafio.dto.builder.SimulacaoResponseBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para SimulacaoResponseBuilder.
 */
@DisplayName("SimulacaoResponseBuilder - Testes")
class SimulacaoResponseBuilderTest {

    @Test
    @DisplayName("Deve construir SimulacaoResponseDTO completo")
    void deveConstruirResponseCompleto() {
        // Given
        List<MemoriaCalculoDTO> memorias = Arrays.asList(
            new MemoriaCalculoDTO(1, new BigDecimal("1000"), new BigDecimal("15"), new BigDecimal("1015")),
            new MemoriaCalculoDTO(2, new BigDecimal("1015"), new BigDecimal("15.23"), new BigDecimal("1030.23"))
        );

        // When
        SimulacaoResponseDTO response = SimulacaoResponseBuilder.builder()
            .id(1L)
            .valorInicial(new BigDecimal("1000.00"))
            .taxaJurosMensal(new BigDecimal("1.5"))
            .prazoMeses(2)
            .valorTotalFinal(new BigDecimal("1030.23"))
            .valorTotalJuros(new BigDecimal("30.23"))
            .memoriaCalculos(memorias)
            .build();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getValorInicial()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(response.getTaxaJurosMensal()).isEqualByComparingTo(new BigDecimal("1.5"));
        assertThat(response.getPrazoMeses()).isEqualTo(2);
        assertThat(response.getValorTotalFinal()).isEqualByComparingTo(new BigDecimal("1030.23"));
        assertThat(response.getValorTotalJuros()).isEqualByComparingTo(new BigDecimal("30.23"));
        assertThat(response.getMemoriaCalculos()).hasSize(2);
    }

    @Test
    @DisplayName("Deve construir com lista de memórias vazia")
    void deveConstruirComListaVazia() {
        // When
        SimulacaoResponseDTO response = SimulacaoResponseBuilder.builder()
            .id(1L)
            .valorInicial(new BigDecimal("1000"))
            .taxaJurosMensal(new BigDecimal("1.5"))
            .prazoMeses(0)
            .valorTotalFinal(new BigDecimal("1000"))
            .valorTotalJuros(BigDecimal.ZERO)
            .memoriaCalculos(Arrays.asList())
            .build();

        // Then
        assertThat(response.getMemoriaCalculos()).isEmpty();
    }

    @Test
    @DisplayName("Deve construir com valores BigDecimal precisos")
    void deveConstruirComValoresPrecisos() {
        // When
        SimulacaoResponseDTO response = SimulacaoResponseBuilder.builder()
            .id(999L)
            .valorInicial(new BigDecimal("123456.78"))
            .taxaJurosMensal(new BigDecimal("2.75"))
            .prazoMeses(360)
            .valorTotalFinal(new BigDecimal("999999.99"))
            .valorTotalJuros(new BigDecimal("876543.21"))
            .memoriaCalculos(Arrays.asList())
            .build();

        // Then
        assertThat(response.getId()).isEqualTo(999L);
        assertThat(response.getValorInicial().toPlainString()).isEqualTo("123456.78");
        assertThat(response.getValorTotalFinal().toPlainString()).isEqualTo("999999.99");
    }
}
