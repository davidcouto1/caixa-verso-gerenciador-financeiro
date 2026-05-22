package br.com.david.desafio.service;

import br.com.david.desafio.dto.SimulacaoRequestDTO;
import br.com.david.desafio.dto.SimulacaoResponseDTO;
import br.com.david.desafio.exception.SimulacaoNotFoundException;
import br.com.david.desafio.repository.SimulacaoRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de integração para o SimulacaoService.
 * Garante a corretude dos cálculos de juros compostos e validações.
 */
@QuarkusTest
@DisplayName("SimulacaoService - Testes de Integração")
class SimulacaoServiceTest {

    @Inject
    SimulacaoService simulacaoService;

    @Inject
    SimulacaoRepository simulacaoRepository;

    private SimulacaoRequestDTO requestValidoDTO;

    @BeforeEach
    @Transactional
    void setUp() {
        // Limpar banco antes de cada teste
        // Usar query nativa para deletar com CASCADE
        simulacaoRepository.getEntityManager()
            .createNativeQuery("DELETE FROM memoria_calculo")
            .executeUpdate();
        simulacaoRepository.getEntityManager()
            .createNativeQuery("DELETE FROM simulacao")
            .executeUpdate();
        
        requestValidoDTO = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.5"),
            12
        );
    }

    @Test
    @DisplayName("Deve criar simulação com cálculo correto de juros compostos")
    void deveCriarSimulacaoComCalculoCorreto() {
        // When
        SimulacaoResponseDTO response = simulacaoService.criarSimulacao(requestValidoDTO);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getValorInicial()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(response.getTaxaJurosMensal()).isEqualByComparingTo(new BigDecimal("1.5"));
        assertThat(response.getPrazoMeses()).isEqualTo(12);
        
        // Verificar valor final calculado
        assertThat(response.getValorTotalFinal()).isNotNull();
        assertThat(response.getValorTotalFinal()).isGreaterThan(response.getValorInicial());
        
        // Verificar total de juros
        assertThat(response.getValorTotalJuros()).isNotNull();
        assertThat(response.getValorTotalJuros())
            .isEqualByComparingTo(response.getValorTotalFinal().subtract(response.getValorInicial()));
        
        // Verificar memória de cálculo
        assertThat(response.getMemoriaCalculos()).hasSize(12);
        assertThat(response.getMemoriaCalculos().get(0).getMes()).isEqualTo(1);
        assertThat(response.getMemoriaCalculos().get(11).getMes()).isEqualTo(12);
    }

    @Test
    @DisplayName("Deve calcular juros compostos corretamente para 1 mês")
    void deveCalcularJurosCompostosParaUmMes() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.5"),
            1
        );

        // When
        SimulacaoResponseDTO response = simulacaoService.criarSimulacao(request);

        // Then
        assertThat(response.getMemoriaCalculos()).hasSize(1);
        
        // Valor final esperado: 1000 * (1 + 0.015) = 1015.00
        BigDecimal valorEsperado = new BigDecimal("1015.00");
        assertThat(response.getValorTotalFinal()).isEqualByComparingTo(valorEsperado);
        
        // Juros esperados: 15.00
        BigDecimal jurosEsperados = new BigDecimal("15.00");
        assertThat(response.getValorTotalJuros()).isEqualByComparingTo(jurosEsperados);
        
        // Verificar memória de cálculo do mês 1
        assertThat(response.getMemoriaCalculos().get(0).getSaldoInicial())
            .isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(response.getMemoriaCalculos().get(0).getJuro())
            .isEqualByComparingTo(jurosEsperados);
        assertThat(response.getMemoriaCalculos().get(0).getSaldoFinal())
            .isEqualByComparingTo(valorEsperado);
    }

    @Test
    @DisplayName("Deve calcular juros compostos corretamente para 12 meses")
    void deveCalcularJurosCompostosParaDozeMeses() {
        // When
        SimulacaoResponseDTO response = simulacaoService.criarSimulacao(requestValidoDTO);

        // Then
        assertThat(response.getMemoriaCalculos()).hasSize(12);
        
        // Verificar crescimento mês a mês
        for (int i = 1; i < response.getMemoriaCalculos().size(); i++) {
            BigDecimal saldoAnterior = response.getMemoriaCalculos().get(i - 1).getSaldoFinal();
            BigDecimal saldoAtual = response.getMemoriaCalculos().get(i).getSaldoInicial();
            
            assertThat(saldoAtual).isEqualByComparingTo(saldoAnterior);
        }
        
        // Valor final deve ser aproximadamente 1000 * (1.015)^12 = 1195.62
        BigDecimal valorEsperadoAproximado = new BigDecimal("1195.62");
        assertThat(response.getValorTotalFinal())
            .isCloseTo(valorEsperadoAproximado, within(new BigDecimal("0.01")));
    }

    @Test
    @DisplayName("Deve validar valor inicial negativo")
    void deveValidarValorInicialNegativo() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("-100.00"),
            new BigDecimal("1.5"),
            12
        );

        // When & Then
        assertThatThrownBy(() -> simulacaoService.criarSimulacao(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("valor inicial deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve validar valor inicial zero")
    void deveValidarValorInicialZero() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            BigDecimal.ZERO,
            new BigDecimal("1.5"),
            12
        );

        // When & Then
        assertThatThrownBy(() -> simulacaoService.criarSimulacao(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("valor inicial deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve validar taxa de juros negativa")
    void deveValidarTaxaJurosNegativa() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("-1.5"),
            12
        );

        // When & Then
        assertThatThrownBy(() -> simulacaoService.criarSimulacao(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("taxa de juros deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve validar taxa de juros zero")
    void deveValidarTaxaJurosZero() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            BigDecimal.ZERO,
            12
        );

        // When & Then
        assertThatThrownBy(() -> simulacaoService.criarSimulacao(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("taxa de juros deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve validar prazo negativo")
    void deveValidarPrazoNegativo() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.5"),
            -1
        );

        // When & Then
        assertThatThrownBy(() -> simulacaoService.criarSimulacao(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("prazo deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve validar prazo zero")
    void deveValidarPrazoZero() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.5"),
            0
        );

        // When & Then
        assertThatThrownBy(() -> simulacaoService.criarSimulacao(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("prazo deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve validar prazo máximo excedido")
    void deveValidarPrazoMaximoExcedido() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.5"),
            361
        );

        // When & Then
        assertThatThrownBy(() -> simulacaoService.criarSimulacao(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("prazo máximo é de 360 meses");
    }

    @Test
    @DisplayName("Deve buscar simulação existente por ID")
    void deveBuscarSimulacaoExistente() {
        // Given - Criar uma simulação primeiro
        SimulacaoResponseDTO simulacaoCriada = simulacaoService.criarSimulacao(requestValidoDTO);
        Long simulacaoId = simulacaoCriada.getId();

        // When
        SimulacaoResponseDTO response = simulacaoService.buscarSimulacao(simulacaoId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(simulacaoId);
        assertThat(response.getValorInicial()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(response.getMemoriaCalculos()).hasSize(12);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar simulação inexistente")
    void deveLancarExcecaoAoBuscarSimulacaoInexistente() {
        // Given
        Long simulacaoId = 999L;

        // When & Then
        assertThatThrownBy(() -> simulacaoService.buscarSimulacao(simulacaoId))
            .isInstanceOf(SimulacaoNotFoundException.class)
            .hasMessageContaining("não encontrada")
            .hasMessageContaining("999");
    }

    @Test
    @DisplayName("Deve calcular com taxa de juros alta")
    void deveCalcularComTaxaJurosAlta() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("10.0"),
            12
        );

        // When
        SimulacaoResponseDTO response = simulacaoService.criarSimulacao(request);

        // Then
        // 1000 * (1.10)^12 = 3138.43
        BigDecimal valorEsperadoAproximado = new BigDecimal("3138.43");
        assertThat(response.getValorTotalFinal())
            .isCloseTo(valorEsperadoAproximado, within(new BigDecimal("1.00")));
        
        assertThat(response.getValorTotalJuros()).isGreaterThan(new BigDecimal("2000.00"));
    }

    @Test
    @DisplayName("Deve calcular com valor inicial grande")
    void deveCalcularComValorInicialGrande() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000000.00"),
            new BigDecimal("0.5"),
            6
        );

        // When
        SimulacaoResponseDTO response = simulacaoService.criarSimulacao(request);

        // Then
        assertThat(response.getValorTotalFinal()).isGreaterThan(new BigDecimal("1000000.00"));
        assertThat(response.getMemoriaCalculos()).hasSize(6);
        
        // Verificar que não há perda de precisão
        assertThat(response.getValorTotalFinal().scale()).isLessThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve calcular com taxa de juros decimal pequena")
    void deveCalcularComTaxaJurosDecimalPequena() {
        // Given
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("0.01"),
            12
        );

        // When
        SimulacaoResponseDTO response = simulacaoService.criarSimulacao(request);

        // Then
        // 1000 * (1.0001)^12 ≈ 1001.20
        assertThat(response.getValorTotalFinal()).isGreaterThan(new BigDecimal("1000.00"));
        assertThat(response.getValorTotalFinal()).isLessThan(new BigDecimal("1002.00"));
        assertThat(response.getValorTotalJuros()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve manter precisão decimal em todos os cálculos")
    void deveManterPrecisaoDecimal() {
        // When
        SimulacaoResponseDTO response = simulacaoService.criarSimulacao(requestValidoDTO);

        // Then
        assertThat(response.getValorTotalFinal().scale()).isEqualTo(2);
        assertThat(response.getValorTotalJuros().scale()).isEqualTo(2);
        
        response.getMemoriaCalculos().forEach(memoria -> {
            assertThat(memoria.getSaldoInicial().scale()).isEqualTo(2);
            assertThat(memoria.getJuro().scale()).isEqualTo(2);
            assertThat(memoria.getSaldoFinal().scale()).isEqualTo(2);
        });
    }
}
