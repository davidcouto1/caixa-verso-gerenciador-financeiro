package br.com.david.desafio.repository;

import br.com.david.desafio.entity.Simulacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * Repository para operações de persistência da entidade Simulacao.
 * Usa Panache para simplificar queries.
 */
@ApplicationScoped
public class SimulacaoRepository implements ISimulacaoRepository {

    @Override
    public Optional<Simulacao> buscarPorId(Long id) {
        return find("id", id).singleResultOptional();
    }

    @Override
    public Simulacao findByIdWithMemoria(Long id) {
        // TODO: avaliar performance dessa query com muitos registros de memória
        return find("SELECT s FROM Simulacao s LEFT JOIN FETCH s.memoriaCalculos WHERE s.id = ?1", id)
                .firstResult();
    }
    
    // Métodos persist(), delete(), count() herdados de PanacheRepository via ISimulacaoRepository
}
