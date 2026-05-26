package br.com.david.desafio.repository;

import br.com.david.desafio.entity.MemoriaCalculo;
import br.com.david.desafio.entity.Simulacao;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@DisplayName("SimulacaoRepository - Testes de Persistência")
class SimulacaoRepositoryTest {

    @Inject
    SimulacaoRepository repository;

    @BeforeEach
    @Transactional
    void setUp() {
        // Limpar dados anteriores - deletar memórias primeiro devido ao FK
        repository.getEntityManager()
            .createNativeQuery("DELETE FROM memoria_calculo")
            .executeUpdate();
        repository.getEntityManager()
            .createNativeQuery("DELETE FROM simulacao")
            .executeUpdate();
    }

    @Test
    @Transactional
    @DisplayName("Deve persistir e buscar simulação por ID")
    void devePersistirEBuscarPorId() {
        // Given
        Simulacao simulacao = criarSimulacaoTeste();

        // When
        repository.persist(simulacao);
        Optional<Simulacao> encontrada = repository.buscarPorId(simulacao.getId());

        // Then
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getId()).isEqualTo(simulacao.getId());
        assertThat(encontrada.get().getValorInicial()).isEqualByComparingTo(new BigDecimal("10000.00"));
    }

    @Test
    @Transactional
    @DisplayName("Deve retornar Optional vazio ao buscar ID inexistente")
    void deveRetornarVazioAoBuscarIdInexistente() {
        // When
        Optional<Simulacao> resultado = repository.buscarPorId(99999L);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("Deve buscar simulação com memória de cálculo (eager loading)")
    void deveBuscarComMemoriaCalculo() {
        // Given
        Simulacao simulacao = criarSimulacaoTeste();
        
        MemoriaCalculo memoria1 = new MemoriaCalculo(1, 
            new BigDecimal("10000.00"), 
            new BigDecimal("150.00"), 
            new BigDecimal("10150.00"));
        memoria1.setSimulacao(simulacao);
        
        MemoriaCalculo memoria2 = new MemoriaCalculo(2, 
            new BigDecimal("10150.00"), 
            new BigDecimal("152.25"), 
            new BigDecimal("10302.25"));
        memoria2.setSimulacao(simulacao);
        
        simulacao.getMemoriaCalculos().add(memoria1);
        simulacao.getMemoriaCalculos().add(memoria2);

        repository.persist(simulacao);
        Long id = simulacao.getId();

        // When
        Simulacao encontrada = repository.findByIdWithMemoria(id);

        // Then
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getMemoriaCalculos()).hasSize(2);
        assertThat(encontrada.getMemoriaCalculos().get(0).getMes()).isEqualTo(1);
        assertThat(encontrada.getMemoriaCalculos().get(1).getMes()).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("Deve retornar null ao buscar com memória quando ID não existe")
    void deveRetornarNullAoBuscarComMemoriaIdInexistente() {
        // When
        Simulacao resultado = repository.findByIdWithMemoria(99999L);

        // Then
        assertThat(resultado).isNull();
    }

    @Test
    @Transactional
    @DisplayName("Deve deletar simulação existente")
    void deveDeletarSimulacaoExistente() {
        // Given
        Simulacao simulacao = criarSimulacaoTeste();
        repository.persist(simulacao);
        Long id = simulacao.getId();

        // When - Carregar entidade gerenciada e deletar
        Simulacao gerenciada = repository.findById(id);
        repository.delete(gerenciada);

        // Then
        Optional<Simulacao> buscada = repository.buscarPorId(id);
        assertThat(buscada).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("Deve contar número de simulações")
    void deveContarSimulacoes() {
        // Given
        repository.persist(criarSimulacaoTeste());
        repository.persist(criarSimulacaoTeste());
        repository.persist(criarSimulacaoTeste());

        // When
        long count = repository.count();

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @Transactional
    @DisplayName("Deve retornar zero ao contar quando não há simulações")
    void deveRetornarZeroAoContarQuandoVazio() {
        // When
        long count = repository.count();

        // Then
        assertThat(count).isZero();
    }

    @Test
    @Transactional
    @DisplayName("Deve persistir simulação com todos os campos preenchidos")
    void devePersistirComTodosCampos() {
        // Given
        Simulacao simulacao = criarSimulacaoTeste();
        simulacao.setValorTotalFinal(new BigDecimal("15000.00"));
        simulacao.setValorTotalJuros(new BigDecimal("5000.00"));

        // When
        repository.persist(simulacao);
        Optional<Simulacao> encontrada = repository.buscarPorId(simulacao.getId());

        // Then
        assertThat(encontrada).isPresent();
        Simulacao s = encontrada.get();
        assertThat(s.getValorInicial()).isEqualByComparingTo(new BigDecimal("10000.00"));
        assertThat(s.getTaxaJurosMensal()).isEqualByComparingTo(new BigDecimal("1.5"));
        assertThat(s.getPrazoMeses()).isEqualTo(360);
        assertThat(s.getValorTotalFinal()).isEqualByComparingTo(new BigDecimal("15000.00"));
        assertThat(s.getValorTotalJuros()).isEqualByComparingTo(new BigDecimal("5000.00"));
        assertThat(s.getDataCriacao()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("Deve deletar todas as simulações")
    void deveDeletarTodasSimulacoes() {
        // Given
        repository.persist(criarSimulacaoTeste());
        repository.persist(criarSimulacaoTeste());
        repository.persist(criarSimulacaoTeste());
        long countAntes = repository.count();
        assertThat(countAntes).isEqualTo(3);

        // When
        // Deletar com queries nativas devido ao FK
        repository.getEntityManager()
            .createNativeQuery("DELETE FROM memoria_calculo")
            .executeUpdate();
        repository.deleteAll();

        // Then
        assertThat(repository.count()).isZero();
    }

    @Test
    @Transactional
    @DisplayName("Deve buscar simulação sem memória quando não há cálculos")
    void deveBuscarSemMemoriaQuandoNaoHaCalculos() {
        // Given
        Simulacao simulacao = criarSimulacaoTeste();
        repository.persist(simulacao);
        Long id = simulacao.getId();

        // When
        Simulacao encontrada = repository.findByIdWithMemoria(id);

        // Then
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getMemoriaCalculos()).isEmpty();
    }

    private Simulacao criarSimulacaoTeste() {
        Simulacao simulacao = new Simulacao();
        simulacao.setValorInicial(new BigDecimal("10000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("1.5"));
        simulacao.setPrazoMeses(360);
        simulacao.setDataCriacao(LocalDateTime.now());
        // Campos obrigatórios no banco
        simulacao.setValorTotalFinal(new BigDecimal("10000.00"));
        simulacao.setValorTotalJuros(BigDecimal.ZERO);
        return simulacao;
    }
}
