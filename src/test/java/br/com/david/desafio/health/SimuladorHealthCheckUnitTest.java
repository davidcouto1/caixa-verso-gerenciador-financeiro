package br.com.david.desafio.health;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para os health checks com cenários de falha.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Health Checks - Testes Unitários com Falhas")
class SimuladorHealthCheckUnitTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @InjectMocks
    private SimuladorHealthCheck.ReadinessCheck readinessCheck;

    private SimuladorHealthCheck.LivenessCheck livenessCheck;

    @BeforeEach
    void setUp() {
        livenessCheck = new SimuladorHealthCheck.LivenessCheck();
    }

    @Test
    @DisplayName("Liveness check deve sempre retornar UP")
    void livenessCheckDeveRetornarUp() {
        HealthCheckResponse response = livenessCheck.call();

        assertThat(response.getStatus()).isEqualTo(HealthCheckResponse.Status.UP);
        assertThat(response.getName()).contains("Liveness");
        assertThat(response.getData()).isPresent();
        assertThat(response.getData().get().get("status")).isEqualTo("alive");
    }

    @Test
    @DisplayName("Readiness check deve retornar UP quando conexão está válida")
    void readinessCheckDeveRetornarUpQuandoConexaoValida() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(anyInt())).thenReturn(true);

        HealthCheckResponse response = readinessCheck.call();

        assertThat(response.getStatus()).isEqualTo(HealthCheckResponse.Status.UP);
        assertThat(response.getName()).contains("H2");
        assertThat(response.getData()).isPresent();
        assertThat(response.getData().get().get("connection")).isEqualTo("active");
    }

    @Test
    @DisplayName("Readiness check deve retornar DOWN quando conexão não está válida")
    void readinessCheckDeveRetornarDownQuandoConexaoInvalida() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(anyInt())).thenReturn(false);

        HealthCheckResponse response = readinessCheck.call();

        assertThat(response.getStatus()).isEqualTo(HealthCheckResponse.Status.DOWN);
        assertThat(response.getName()).contains("H2");
        assertThat(response.getData()).isPresent();
        assertThat(response.getData().get().get("connection")).isEqualTo("inactive");
    }

    @Test
    @DisplayName("Readiness check deve retornar DOWN quando ocorre SQLException")
    void readinessCheckDeveRetornarDownQuandoOcorreSqlException() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException("Erro de conexão"));

        HealthCheckResponse response = readinessCheck.call();

        assertThat(response.getStatus()).isEqualTo(HealthCheckResponse.Status.DOWN);
        assertThat(response.getName()).contains("H2");
        assertThat(response.getData()).isPresent();
        assertThat(response.getData().get().get("error")).isEqualTo("Erro de conexão");
    }

    @Test
    @DisplayName("Readiness check deve retornar DOWN quando datasource lança exceção genérica")
    void readinessCheckDeveRetornarDownQuandoExcecaoGenerica() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new RuntimeException("Erro inesperado"));

        HealthCheckResponse response = readinessCheck.call();

        assertThat(response.getStatus()).isEqualTo(HealthCheckResponse.Status.DOWN);
        assertThat(response.getName()).contains("H2");
        assertThat(response.getData()).isPresent();
        assertThat(response.getData().get().get("error")).isEqualTo("Erro inesperado");
    }

    @Test
    @DisplayName("Readiness check deve incluir dados do database no response")
    void readinessCheckDeveIncluirDadosDatabase() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(anyInt())).thenReturn(true);

        HealthCheckResponse response = readinessCheck.call();

        assertThat(response.getData()).isPresent();
        assertThat(response.getData().get().get("database")).isEqualTo("H2");
    }

    @Test
    @DisplayName("Liveness check deve ter nome específico")
    void livenessCheckDeveTerNomeEspecifico() {
        HealthCheckResponse response = livenessCheck.call();

        assertThat(response.getName()).isEqualTo("Simulador de Financiamentos - Liveness");
    }

    @Test
    @DisplayName("Readiness check deve ter nome específico")
    void readinessCheckDeveTerNomeEspecifico() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(anyInt())).thenReturn(true);

        HealthCheckResponse response = readinessCheck.call();

        assertThat(response.getName()).isEqualTo("Database Connection - H2");
    }
}
