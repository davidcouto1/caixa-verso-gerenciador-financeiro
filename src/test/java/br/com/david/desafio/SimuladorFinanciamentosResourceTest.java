package br.com.david.desafio;

import br.com.david.desafio.dto.SimulacaoRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Testes de integração para a API de Simulação de Financiamentos.
 * Testa os endpoints REST com o Quarkus rodando.
 */
@QuarkusTest
@DisplayName("SimuladorFinanciamentosResource - Testes de Integração")
class SimuladorFinanciamentosResourceTest {

    @Test
    @DisplayName("Deve criar simulação com sucesso e retornar 201")
    void deveCriarSimulacaoComSucesso() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.5"),
            12
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", notNullValue())
            .body("valorInicial", equalTo(1000.00f))
            .body("taxaJurosMensal", equalTo(1.5f))
            .body("prazoMeses", equalTo(12))
            .body("valorTotalFinal", greaterThan(1000.00f))
            .body("valorTotalJuros", greaterThan(0.00f))
            .body("memoriaCalculos", hasSize(12))
            .body("memoriaCalculos[0].mes", equalTo(1))
            .body("memoriaCalculos[11].mes", equalTo(12));
    }

    @Test
    @DisplayName("Deve buscar simulação existente e retornar 200")
    void deveBuscarSimulacaoExistente() {
        // Criar simulação primeiro
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("5000.00"),
            new BigDecimal("2.0"),
            6
        );

        Integer id = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // Buscar simulação criada
        given()
        .when()
            .get("/api/simulacoes/" + id)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(id))
            .body("valorInicial", equalTo(5000.00f))
            .body("taxaJurosMensal", equalTo(2.0f))
            .body("prazoMeses", equalTo(6))
            .body("memoriaCalculos", hasSize(6));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar simulação inexistente")
    void deveRetornar404AoBuscarSimulacaoInexistente() {
        given()
        .when()
            .get("/api/simulacoes/999999")
        .then()
            .statusCode(404)
            .contentType(ContentType.JSON)
            .body("status", equalTo(404))
            .body("error", equalTo("Not Found"))
            .body("message", containsString("não encontrada"));
    }

    @Test
    @DisplayName("Deve retornar 400 para valor inicial negativo")
    void deveRetornar400ParaValorInicialNegativo() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("-100.00"),
            new BigDecimal("1.5"),
            12
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("status", equalTo(400));
    }

    @Test
    @DisplayName("Deve retornar 400 para taxa de juros zero")
    void deveRetornar400ParaTaxaJurosZero() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            BigDecimal.ZERO,
            12
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("status", equalTo(400));
    }

    @Test
    @DisplayName("Deve retornar 400 para prazo zero")
    void deveRetornar400ParaPrazoZero() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.5"),
            0
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("status", equalTo(400));
    }

    @Test
    @DisplayName("Deve calcular juros compostos corretamente para 1 mês")
    void deveCalcularJurosCorretamenteParaUmMes() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.5"),
            1
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(201)
            .body("valorTotalFinal", equalTo(1015.00f))
            .body("valorTotalJuros", equalTo(15.00f))
            .body("memoriaCalculos", hasSize(1))
            .body("memoriaCalculos[0].saldoInicial", equalTo(1000.00f))
            .body("memoriaCalculos[0].juro", equalTo(15.00f))
            .body("memoriaCalculos[0].saldoFinal", equalTo(1015.00f));
    }

    @Test
    @DisplayName("Deve verificar crescimento progressivo na memória de cálculo")
    void deveVerificarCrescimentoProgressivo() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("2.0"),
            3
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(201)
            .body("memoriaCalculos[0].saldoFinal", lessThan(1021.00f))
            .body("memoriaCalculos[1].saldoFinal", lessThan(1043.00f))
            .body("memoriaCalculos[2].saldoFinal", lessThan(1065.00f))
            .body("memoriaCalculos[0].saldoFinal", greaterThan(1019.00f))
            .body("memoriaCalculos[1].saldoFinal", greaterThan(1039.00f))
            .body("memoriaCalculos[2].saldoFinal", greaterThan(1060.00f));
    }

    @Test
    @DisplayName("Deve validar que cada mês tem juros calculados")
    void deveValidarJurosCalculadosPorMes() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.0"),
            5
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(201)
            .body("memoriaCalculos[0].juro", greaterThan(0.00f))
            .body("memoriaCalculos[1].juro", greaterThan(0.00f))
            .body("memoriaCalculos[2].juro", greaterThan(0.00f))
            .body("memoriaCalculos[3].juro", greaterThan(0.00f))
            .body("memoriaCalculos[4].juro", greaterThan(0.00f));
    }

    @Test
    @DisplayName("Deve testar health check endpoint")
    void deveTestarHealthCheck() {
        given()
        .when()
            .get("/api/simulacoes/health")
        .then()
            .statusCode(200)
            .contentType(ContentType.TEXT)
            .body(containsString("running"));
    }

    @Test
    @DisplayName("Deve aceitar valores decimais com precisão")
    void deveAceitarValoresDecimaisComPrecisao() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1234.56"),
            new BigDecimal("1.75"),
            6
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(201)
            .body("valorInicial", equalTo(1234.56f))
            .body("taxaJurosMensal", equalTo(1.75f));
    }

    @Test
    @DisplayName("Deve calcular corretamente para taxa muito pequena")
    void deveCalcularParaTaxaMuitoPequena() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("0.01"),
            12
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(201)
            .body("valorTotalFinal", greaterThan(1000.00f))
            .body("valorTotalJuros", greaterThan(0.00f))
            .body("valorTotalJuros", lessThan(2.00f));
    }

    @Test
    @DisplayName("Deve calcular corretamente para valor alto")
    void deveCalcularParaValorAlto() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("100000.00"),
            new BigDecimal("1.5"),
            12
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(201)
            .body("valorInicial", equalTo(100000.00f))
            .body("valorTotalFinal", greaterThan(100000.00f))
            .body("valorTotalJuros", greaterThan(10000.00f));
    }

    @Test
    @DisplayName("Deve validar prazo máximo (360 meses)")
    void deveValidarPrazoMaximo() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.5"),
            360
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(201)
            .body("prazoMeses", equalTo(360))
            .body("memoriaCalculos", hasSize(360));
    }

    @Test
    @DisplayName("Deve rejeitar prazo acima de 360 meses")
    void deveRejeitarPrazoAcimaDoMaximo() {
        SimulacaoRequestDTO request = new SimulacaoRequestDTO(
            new BigDecimal("1000.00"),
            new BigDecimal("1.5"),
            361
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/simulacoes")
        .then()
            .statusCode(400)
            .body("status", equalTo(400));
    }
}
