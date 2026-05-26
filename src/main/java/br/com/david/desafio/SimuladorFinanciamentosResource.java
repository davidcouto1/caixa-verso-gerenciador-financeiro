package br.com.david.desafio;

import br.com.david.desafio.dto.ErrorResponseDTO;
import br.com.david.desafio.dto.SimulacaoRequestDTO;
import br.com.david.desafio.dto.SimulacaoResponseDTO;
import br.com.david.desafio.exception.SimulacaoNotFoundException;
import br.com.david.desafio.service.ISimulacaoService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource REST para gerenciar simulações de financiamento.
 * Aplica Dependency Inversion Principle: Depende da interface ISimulacaoService.
 */
@Path("/api/simulacoes")
@Tag(name = "Simulações", description = "API para simulação de financiamentos com juros compostos")
public class SimuladorFinanciamentosResource {

    private static final Logger LOG = LoggerFactory.getLogger(SimuladorFinanciamentosResource.class);

    // Dependency Inversion: Depende da interface, não da implementação
    @Inject
    ISimulacaoService simulacaoService;

    /**
     * Cria uma nova simulação de financiamento.
     *
     * @param requestDTO Dados de entrada da simulação
     * @return Simulação criada com todos os cálculos
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Counted(value = "simulacoes.criadas", description = "Número de simulações criadas")
    @Timed(value = "simulacoes.criar.tempo", description = "Tempo de criação de simulações")
    @Operation(
        summary = "Criar nova simulação de financiamento",
        description = """
            Cria uma nova simulação calculando juros compostos mês a mês.
            
            O cálculo utiliza a fórmula: **Saldo Final = Saldo Inicial × (1 + Taxa/100)**
            
            A API retorna:
            - ID da simulação criada
            - Valores totais (final e juros)
            - Memória de cálculo completa mês a mês
            """
    )
    @RequestBody(
        description = "Dados para simular o financiamento",
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = SimulacaoRequestDTO.class),
            examples = {
                @ExampleObject(
                    name = "Financiamento Básico",
                    summary = "Simulação de R$ 1.000 por 12 meses",
                    description = "Exemplo básico de financiamento de mil reais a 1,5% ao mês",
                    value = """
                        {
                          "valorInicial": 1000.00,
                          "taxaJurosMensal": 1.5,
                          "prazoMeses": 12
                        }
                        """
                ),
                @ExampleObject(
                    name = "Financiamento Alto Valor",
                    summary = "Simulação de R$ 100.000 por 24 meses",
                    description = "Financiamento de alto valor com taxa de 2,5% ao mês",
                    value = """
                        {
                          "valorInicial": 100000.00,
                          "taxaJurosMensal": 2.5,
                          "prazoMeses": 24
                        }
                        """
                ),
                @ExampleObject(
                    name = "Financiamento Longo Prazo",
                    summary = "Simulação de R$ 50.000 por 120 meses",
                    description = "Financiamento de longo prazo (10 anos) com taxa de 1,0% ao mês",
                    value = """
                        {
                          "valorInicial": 50000.00,
                          "taxaJurosMensal": 1.0,
                          "prazoMeses": 120
                        }
                        """
                ),
                @ExampleObject(
                    name = "Financiamento Taxa Baixa",
                    summary = "Simulação de R$ 5.000 com taxa de 0,5%",
                    description = "Financiamento com taxa de juros baixa por 6 meses",
                    value = """
                        {
                          "valorInicial": 5000.00,
                          "taxaJurosMensal": 0.5,
                          "prazoMeses": 6
                        }
                        """
                )
            }
        )
    )
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "Simulação criada com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SimulacaoResponseDTO.class),
                examples = @ExampleObject(
                    name = "Simulação Criada",
                    summary = "Exemplo de resposta completa",
                    description = "Resposta com ID, valores calculados e memória mês a mês",
                    value = """
                        {
                          "id": 1,
                          "valorInicial": 1000.00,
                          "taxaJurosMensal": 1.50,
                          "prazoMeses": 12,
                          "valorTotalFinal": 1195.63,
                          "valorTotalJuros": 195.63,
                          "memoriaCalculos": [
                            {
                              "mes": 1,
                              "saldoInicial": 1000.00,
                              "juro": 15.00,
                              "saldoFinal": 1015.00
                            },
                            {
                              "mes": 2,
                              "saldoInicial": 1015.00,
                              "juro": 15.23,
                              "saldoFinal": 1030.23
                            },
                            {
                              "mes": 12,
                              "saldoInicial": 1177.96,
                              "juro": 17.67,
                              "saldoFinal": 1195.63
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Dados de entrada inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Erro de Validação",
                        summary = "Valor negativo não permitido",
                        description = "Exemplo de erro quando valor inicial é negativo",
                        value = """
                            {
                              "timestamp": "2026-05-25T19:00:00",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Validation failed",
                              "path": "/api/simulacoes",
                              "errors": [
                                "O valor inicial deve ser maior que zero"
                              ]
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Prazo Inválido",
                        summary = "Prazo fora dos limites",
                        description = "Exemplo de erro quando prazo excede 360 meses",
                        value = """
                            {
                              "timestamp": "2026-05-25T19:00:00",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Validation failed",
                              "path": "/api/simulacoes",
                              "errors": [
                                "O prazo não pode ser superior a 360 meses"
                              ]
                            }
                            """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = @ExampleObject(
                    name = "Erro Interno",
                    summary = "Erro inesperado no servidor",
                    value = """
                        {
                          "timestamp": "2026-05-25T19:00:00",
                          "status": 500,
                          "error": "Internal Server Error",
                          "message": "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                          "path": "/api/simulacoes",
                          "errors": []
                        }
                        """
                )
            )
        )
    })
    public Response criarSimulacao(@Valid SimulacaoRequestDTO requestDTO) {
        LOG.info("POST /api/simulacoes - Criando nova simulação");
        
        SimulacaoResponseDTO response = simulacaoService.criarSimulacao(requestDTO);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    /**
     * Busca uma simulação existente por ID.
     *
     * @param id Identificador da simulação
     * @return Simulação encontrada
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(value = "simulacoes.buscadas", description = "Número de buscas de simulações")
    @Timed(value = "simulacoes.buscar.tempo", description = "Tempo de busca de simulações")
    @Operation(
        summary = "Buscar simulação por ID",
        description = """
            Retorna uma simulação existente com todos os seus detalhes:
            - Dados de entrada (valor, taxa, prazo)
            - Valores calculados (total final e juros)
            - Memória de cálculo completa mês a mês
            
            Útil para recuperar simulações anteriores e analisar a evolução do financiamento.
            """
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Simulação encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SimulacaoResponseDTO.class),
                examples = @ExampleObject(
                    name = "Simulação Encontrada",
                    summary = "Exemplo de simulação recuperada",
                    description = "Resposta completa com todos os dados da simulação",
                    value = """
                        {
                          "id": 1,
                          "valorInicial": 1000.00,
                          "taxaJurosMensal": 1.50,
                          "prazoMeses": 12,
                          "valorTotalFinal": 1195.63,
                          "valorTotalJuros": 195.63,
                          "memoriaCalculos": [
                            {
                              "mes": 1,
                              "saldoInicial": 1000.00,
                              "juro": 15.00,
                              "saldoFinal": 1015.00
                            },
                            {
                              "mes": 2,
                              "saldoInicial": 1015.00,
                              "juro": 15.23,
                              "saldoFinal": 1030.23
                            },
                            {
                              "mes": 12,
                              "saldoInicial": 1177.96,
                              "juro": 17.67,
                              "saldoFinal": 1195.63
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Simulação não encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = @ExampleObject(
                    name = "Simulação Não Encontrada",
                    summary = "ID inexistente no banco de dados",
                    description = "Erro retornado quando a simulação não existe",
                    value = """
                        {
                          "timestamp": "2026-05-25T19:00:00",
                          "status": 404,
                          "error": "Not Found",
                          "message": "Simulação com ID 999 não encontrada",
                          "path": "/api/simulacoes/999",
                          "errors": []
                        }
                        """
                )
            )
        ),
        @APIResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponseDTO.class),
                examples = @ExampleObject(
                    name = "Erro Interno",
                    summary = "Erro inesperado no servidor",
                    value = """
                        {
                          "timestamp": "2026-05-25T19:00:00",
                          "status": 500,
                          "error": "Internal Server Error",
                          "message": "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                          "path": "/api/simulacoes/999",
                          "errors": []
                        }
                        """
                )
            )
        )
    })
    public Response buscarSimulacao(
            @Parameter(
                description = "Identificador único da simulação no banco de dados",
                required = true,
                example = "1",
                schema = @Schema(type = SchemaType.INTEGER, format = "int64", minimum = "1")
            )
            @PathParam("id") Long id) {
        LOG.info("GET /api/simulacoes/{} - Buscando simulação", id);
        
        SimulacaoResponseDTO response = simulacaoService.buscarSimulacao(id);
        return Response.ok(response).build();
    }
}
