package br.com.david.desafio.health;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

/**
 * Testes para os health checks via REST endpoints.
 */
@QuarkusTest
@DisplayName("Health Checks - Testes REST")
class SimuladorHealthCheckTest {

    @Test
    @DisplayName("/q/health/live deve retornar UP")
    void livenessEndpointDeveRetornarUp() {
        RestAssured.given()
            .when().get("/q/health/live")
            .then()
                .statusCode(200)
                .body("status", equalTo("UP"))
                .body("checks.size()", greaterThan(0));
    }

    @Test
    @DisplayName("/q/health/ready deve retornar UP quando banco disponível")
    void readinessEndpointDeveRetornarUp() {
        RestAssured.given()
            .when().get("/q/health/ready")
            .then()
                .statusCode(200)
                .body("status", equalTo("UP"))
                .body("checks.size()", greaterThan(0));
    }

    @Test
    @DisplayName("/q/health deve retornar status geral")
    void healthEndpointDeveRetornarStatusGeral() {
        RestAssured.given()
            .when().get("/q/health")
            .then()
                .statusCode(200)
                .body("status", equalTo("UP"));
    }
}
