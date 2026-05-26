package br.com.david.desafio.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Health checks do MicroProfile para monitoramento da aplicação.
 */
public class SimuladorHealthCheck {

    /**
     * Liveness check - verifica se a aplicação está viva.
     * Este check nunca deve falhar a menos que a aplicação esteja realmente morta.
     */
    @Liveness
    @ApplicationScoped
    public static class LivenessCheck implements HealthCheck {
        @Override
        public HealthCheckResponse call() {
            return HealthCheckResponse
                .named("Simulador de Financiamentos - Liveness")
                .up()
                .withData("status", "alive")
                .build();
        }
    }

    /**
     * Readiness check - verifica se a aplicação está pronta para receber requisições.
     * Valida a conexão com o banco de dados H2.
     */
    @Readiness
    @ApplicationScoped
    public static class ReadinessCheck implements HealthCheck {

        @Inject
        DataSource dataSource;

        @Override
        public HealthCheckResponse call() {
            try (Connection connection = dataSource.getConnection()) {
                boolean isValid = connection.isValid(2); // timeout de 2 segundos
                
                if (isValid) {
                    return HealthCheckResponse
                        .named("Database Connection - H2")
                        .up()
                        .withData("database", "H2")
                        .withData("connection", "active")
                        .build();
                } else {
                    return HealthCheckResponse
                        .named("Database Connection - H2")
                        .down()
                        .withData("database", "H2")
                        .withData("connection", "inactive")
                        .build();
                }
            } catch (Exception e) {
                return HealthCheckResponse
                    .named("Database Connection - H2")
                    .down()
                    .withData("database", "H2")
                    .withData("error", e.getMessage())
                    .build();
            }
        }
    }
}
