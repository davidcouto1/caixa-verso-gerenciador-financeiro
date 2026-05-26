package br.com.david.desafio.factory;

import br.com.david.desafio.dto.SimulacaoRequestDTO;
import br.com.david.desafio.entity.Simulacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("SimulacaoFactory - Testes de Criação de Simulações")
class SimulacaoFactoryTest {

    private SimulacaoFactory factory;

    @BeforeEach
    void setUp() {
        factory = new SimulacaoFactory();
    }

    @Test
    @DisplayName("Deve criar simulação a partir de DTO com todos os campos")
    void deveCriarSimulacaoDeDTO() {
        // Given
        SimulacaoRequestDTO dto = new SimulacaoRequestDTO(
                new BigDecimal("10000.00"),
                new BigDecimal("1.5"),
                360
        );

        // When
        Simulacao simulacao = factory.criarSimulacao(dto);

        // Then
        assertThat(simulacao).isNotNull();
        assertThat(simulacao.getValorInicial()).isEqualByComparingTo(new BigDecimal("10000.00"));
        assertThat(simulacao.getTaxaJurosMensal()).isEqualByComparingTo(new BigDecimal("1.5"));
        assertThat(simulacao.getPrazoMeses()).isEqualTo(360);
        assertThat(simulacao.getDataCriacao()).isCloseTo(LocalDateTime.now(), within(1, java.time.temporal.ChronoUnit.SECONDS));
        assertThat(simulacao.getMemoriaCalculos()).isEmpty();
    }

    @Test
    @DisplayName("Deve criar simulação com valores diretos (sem DTO)")
    void deveCriarSimulacaoComValoresDiretos() {
        // Given
        BigDecimal valorInicial = new BigDecimal("5000.00");
        BigDecimal taxaJuros = new BigDecimal("2.0");
        int prazo = 180;

        // When
        Simulacao simulacao = factory.criarSimulacao(valorInicial, taxaJuros, prazo);

        // Then
        assertThat(simulacao).isNotNull();
        assertThat(simulacao.getValorInicial()).isEqualByComparingTo(valorInicial);
        assertThat(simulacao.getTaxaJurosMensal()).isEqualByComparingTo(taxaJuros);
        assertThat(simulacao.getPrazoMeses()).isEqualTo(prazo);
        assertThat(simulacao.getDataCriacao()).isNotNull();
    }

    @Test
    @DisplayName("Deve criar simulação com valores mínimos")
    void deveCriarSimulacaoComValoresMinimos() {
        // Given
        SimulacaoRequestDTO dto = new SimulacaoRequestDTO(
                new BigDecimal("100.00"),
                new BigDecimal("0.1"),
                1
        );

        // When
        Simulacao simulacao = factory.criarSimulacao(dto);

        // Then
        assertThat(simulacao.getValorInicial()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(simulacao.getTaxaJurosMensal()).isEqualByComparingTo(new BigDecimal("0.1"));
        assertThat(simulacao.getPrazoMeses()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve criar simulação com valores máximos")
    void deveCriarSimulacaoComValoresMaximos() {
        // Given
        SimulacaoRequestDTO dto = new SimulacaoRequestDTO(
                new BigDecimal("1000000.00"),
                new BigDecimal("10.0"),
                360
        );

        // When
        Simulacao simulacao = factory.criarSimulacao(dto);

        // Then
        assertThat(simulacao.getValorInicial()).isEqualByComparingTo(new BigDecimal("1000000.00"));
        assertThat(simulacao.getTaxaJurosMensal()).isEqualByComparingTo(new BigDecimal("10.0"));
        assertThat(simulacao.getPrazoMeses()).isEqualTo(360);
    }

    @Test
    @DisplayName("Deve definir data de criação automaticamente ao criar")
    void deveDefinirDataCriacaoAutomaticamente() {
        // Given
        SimulacaoRequestDTO dto = new SimulacaoRequestDTO(
                new BigDecimal("1000.00"),
                new BigDecimal("1.0"),
                12
        );
        LocalDateTime antes = LocalDateTime.now();

        // When
        Simulacao simulacao = factory.criarSimulacao(dto);

        // Then
        LocalDateTime depois = LocalDateTime.now();
        assertThat(simulacao.getDataCriacao()).isNotNull();
        assertThat(simulacao.getDataCriacao()).isAfterOrEqualTo(antes);
        assertThat(simulacao.getDataCriacao()).isBeforeOrEqualTo(depois);
    }

    @Test
    @DisplayName("Deve criar simulação com lista de memória de cálculo vazia")
    void deveCriarComMemoriaCalculoVazia() {
        // Given
        SimulacaoRequestDTO dto = new SimulacaoRequestDTO(
                new BigDecimal("2000.00"),
                new BigDecimal("1.5"),
                24
        );

        // When
        Simulacao simulacao = factory.criarSimulacao(dto);

        // Then
        assertThat(simulacao.getMemoriaCalculos()).isNotNull();
        assertThat(simulacao.getMemoriaCalculos()).isEmpty();
    }

    @Test
    @DisplayName("Deve criar novas instâncias independentes a cada chamada")
    void deveCriarInstanciasIndependentes() {
        // Given
        SimulacaoRequestDTO dto = new SimulacaoRequestDTO(
                new BigDecimal("1000.00"),
                new BigDecimal("1.0"),
                12
        );

        // When
        Simulacao simulacao1 = factory.criarSimulacao(dto);
        Simulacao simulacao2 = factory.criarSimulacao(dto);

        // Then
        assertThat(simulacao1).isNotSameAs(simulacao2);
        assertThat(simulacao1.getMemoriaCalculos()).isNotSameAs(simulacao2.getMemoriaCalculos());
    }

    @Test
    @DisplayName("Deve criar simulação com BigDecimal mantendo escala")
    void deveCriarComBigDecimalManendoEscala() {
        // Given
        BigDecimal valorComDuasCasas = new BigDecimal("1000.00");
        BigDecimal taxaComDuasCasas = new BigDecimal("1.50");

        // When
        Simulacao simulacao = factory.criarSimulacao(valorComDuasCasas, taxaComDuasCasas, 12);

        // Then
        assertThat(simulacao.getValorInicial()).isEqualByComparingTo(valorComDuasCasas);
        assertThat(simulacao.getTaxaJurosMensal()).isEqualByComparingTo(taxaComDuasCasas);
    }
}
