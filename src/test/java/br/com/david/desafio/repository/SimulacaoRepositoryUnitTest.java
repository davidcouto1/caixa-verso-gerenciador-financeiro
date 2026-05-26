package br.com.david.desafio.repository;

import br.com.david.desafio.entity.Simulacao;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para SimulacaoRepository.
 */
@QuarkusTest
@DisplayName("SimulacaoRepository - Testes Unitários Focados")
class SimulacaoRepositoryUnitTest {

    @Inject
    SimulacaoRepository repository;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    void limparBanco() {
        entityManager.createNativeQuery("DELETE FROM memoria_calculo").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM simulacao").executeUpdate();
    }

    @Test
    @Transactional
    @DisplayName("persist deve salvar simulação no banco")
    void persistDeveSalvarSimulacao() {
        // Given
        Simulacao simulacao = criarSimulacao();

        // When
        repository.persist(simulacao);
        entityManager.flush();
        entityManager.clear();

        // Then
        Simulacao encontrada = entityManager.find(Simulacao.class, simulacao.getId());
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getValorInicial()).isEqualByComparingTo(new BigDecimal("1000.00"));
    }

    @Test
    @Transactional
    @DisplayName("buscarPorId deve retornar Optional.empty para ID inexistente")
    void buscarPorIdDeveRetornarEmptyParaIdInexistente() {
        // When
        Optional<Simulacao> resultado = repository.buscarPorId(99999L);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("buscarPorId deve retornar simulação existente")
    void buscarPorIdDeveRetornarSimulacaoExistente() {
        // Given
        Simulacao simulacao = criarSimulacao();
        repository.persist(simulacao);
        entityManager.flush();
        Long id = simulacao.getId();

        // When
        Optional<Simulacao> resultado = repository.buscarPorId(id);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(id);
    }

    @Test
    @Transactional
    @DisplayName("findById deve funcionar com Panache")
    void findByIdDeveFuncionarComPanache() {
        // Given
        Simulacao simulacao = criarSimulacao();
        repository.persist(simulacao);
        entityManager.flush();
        Long id = simulacao.getId();

        // When
        Simulacao encontrada = repository.findById(id);

        // Then
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getId()).isEqualTo(id);
    }

    @Test
    @Transactional
    @DisplayName("listAll deve listar todas as simulações")
    void listAllDeveListarTodasSimulacoes() {
        // Given
        repository.persist(criarSimulacao());
        repository.persist(criarSimulacao());
        entityManager.flush();

        // When
        var lista = repository.listAll();

        // Then
        assertThat(lista).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("count deve retornar quantidade de simulações")
    void countDeveRetornarQuantidadeSimulacoes() {
        // Given
        repository.persist(criarSimulacao());
        repository.persist(criarSimulacao());
        entityManager.flush();

        // When
        long count = repository.count();

        // Then
        assertThat(count).isGreaterThanOrEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("delete deve remover simulação do banco")
    void deleteDeveRemoverSimulacao() {
        // Given
        Simulacao simulacao = criarSimulacao();
        repository.persist(simulacao);
        entityManager.flush();
        Long id = simulacao.getId();

        // When
        repository.delete(simulacao);
        entityManager.flush();

        // Then
        Simulacao naoEncontrada = entityManager.find(Simulacao.class, id);
        assertThat(naoEncontrada).isNull();
    }

    private Simulacao criarSimulacao() {
        Simulacao simulacao = new Simulacao();
        simulacao.setValorInicial(new BigDecimal("1000.00"));
        simulacao.setTaxaJurosMensal(new BigDecimal("1.5"));
        simulacao.setPrazoMeses(12);
        simulacao.setValorTotalFinal(new BigDecimal("1195.62"));
        simulacao.setValorTotalJuros(new BigDecimal("195.62"));
        return simulacao;
    }
}
