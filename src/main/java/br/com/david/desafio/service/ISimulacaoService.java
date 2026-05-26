package br.com.david.desafio.service;

import br.com.david.desafio.dto.SimulacaoRequestDTO;
import br.com.david.desafio.dto.SimulacaoResponseDTO;

/**
 * Interface do Service para lógica de negócio das simulações.
 * Aplicação do Dependency Inversion Principle (SOLID).
 * 
 * @author David
 * @since 1.0
 */
public interface ISimulacaoService {
    
    /**
     * Cria e persiste uma nova simulação de financiamento.
     * 
     * @param requestDTO Dados de entrada da simulação
     * @return DTO com a simulação completa e memória de cálculo
     */
    SimulacaoResponseDTO criarSimulacao(SimulacaoRequestDTO requestDTO);
    
    /**
     * Busca uma simulação existente por ID.
     * 
     * @param id Identificador da simulação
     * @return DTO com a simulação completa
     */
    SimulacaoResponseDTO buscarSimulacao(Long id);
}
