package br.com.david.desafio.repository;

import br.com.david.desafio.entity.Simulacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

/**
 * Interface do Repository para operações de persistência da entidade Simulacao.
 * Aplicação do Dependency Inversion Principle (SOLID).
 * 
 * Extende PanacheRepository para herdar métodos padrão (persist, delete, count, etc).
 * Esta interface adiciona apenas métodos customizados específicos do domínio.
 * 
 * @author David
 * @since 1.0
 */
public interface ISimulacaoRepository extends PanacheRepository<Simulacao> {
    
    /**
     * Busca uma simulação por ID.
     * 
     * @param id Identificador da simulação
     * @return Optional contendo a simulação se encontrada
     */
    Optional<Simulacao> buscarPorId(Long id);
    
    /**
     * Busca uma simulação por ID com eager loading da memória de cálculo.
     * 
     * @param id Identificador da simulação
     * @return Simulação encontrada ou null
     */
    Simulacao findByIdWithMemoria(Long id);
}
