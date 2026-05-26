package br.com.david.desafio.repository;

import br.com.david.desafio.entity.Simulacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * Repository para operações de persistência da entidade Simulacao.
 * Utiliza Panache para simplificar as operações de banco de dados.
 * 
 * Implementa ISimulacaoRepository seguindo o Dependency Inversion Principle (SOLID).
 */
@ApplicationScoped
public class SimulacaoRepository implements ISimulacaoRepository {

    @Override
    public Optional<Simulacao> buscarPorId(Long id) {
        return find("id", id).singleResultOptional();
    }

    @Override
    public Simulacao findByIdWithMemoria(Long id) {
        return find("SELECT s FROM Simulacao s LEFT JOIN FETCH s.memoriaCalculos WHERE s.id = ?1", id)
                .firstResult();
    }
    
    // Métodos persist(), delete(), count() herdados de PanacheRepository via ISimulacaoRepository
}
