package br.com.david.desafio.exception;

/**
 * Exception lançada quando uma simulação não é encontrada no banco de dados.
 */
public class SimulacaoNotFoundException extends RuntimeException {

    private final Long simulacaoId;

    public SimulacaoNotFoundException(Long simulacaoId) {
        super(String.format("Simulação com ID %d não encontrada", simulacaoId));
        this.simulacaoId = simulacaoId;
    }

    public Long getSimulacaoId() {
        return simulacaoId;
    }
}
