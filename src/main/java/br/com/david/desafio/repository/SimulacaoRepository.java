package br.com.david.desafio.repository;

import br.com.david.desafio.entity.Simulacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository para operações de persistência da entidade Simulacao.
 * Utiliza Panache para simplificar as operações de banco de dados.
 */
@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<Simulacao> {

    /**
     * Busca uma simulação por ID com eager loading da memória de cálculo.
     * 
     * @param id Identificador da simulação
     * @return Simulação encontrada ou null
     */
    public Simulacao findByIdWithMemoria(Long id) {
        return find("SELECT s FROM Simulacao s LEFT JOIN FETCH s.memoriaCalculos WHERE s.id = ?1", id)
                .firstResult();
    }
}
