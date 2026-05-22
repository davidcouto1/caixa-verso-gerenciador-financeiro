package br.com.david.desafio;

import br.com.david.desafio.dto.ErrorResponseDTO;
import br.com.david.desafio.dto.SimulacaoRequestDTO;
import br.com.david.desafio.dto.SimulacaoResponseDTO;
import br.com.david.desafio.exception.SimulacaoNotFoundException;
import br.com.david.desafio.service.SimulacaoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource REST para gerenciar simulações de financiamento.
 * Expõe endpoints para criar e consultar simulações com cálculo de juros compostos.
 */
@Path("/api/simulacoes")
@Tag(name = "Simulações", description = "API para simulação de financiamentos com juros compostos")
public class SimuladorFinanciamentosResource {

    private static final Logger LOG = LoggerFactory.getLogger(SimuladorFinanciamentosResource.class);

    @Inject
    SimulacaoService simulacaoService;

    /**
     * Cria uma nova simulação de financiamento.
     *
     * @param requestDTO Dados de entrada da simulação
     * @return Simulação criada com todos os cálculos
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Criar nova simulação",
        description = "Cria uma nova simulação de financiamento calculando os juros compostos mês a mês"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "Simulação criada com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SimulacaoResponseDTO.class)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Dados de entrada inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        ),
        @APIResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    })
    public Response criarSimulacao(@Valid SimulacaoRequestDTO requestDTO) {
        LOG.info("POST /api/simulacoes - Criando nova simulação");
        
        try {
            SimulacaoResponseDTO response = simulacaoService.criarSimulacao(requestDTO);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (IllegalArgumentException e) {
            LOG.error("Erro de validação: {}", e.getMessage());
            ErrorResponseDTO error = new ErrorResponseDTO(
                400,
                "Bad Request",
                e.getMessage(),
                "/api/simulacoes"
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } catch (Exception e) {
            LOG.error("Erro ao criar simulação", e);
            ErrorResponseDTO error = new ErrorResponseDTO(
                500,
                "Internal Server Error",
                "Erro ao processar a simulação",
                "/api/simulacoes"
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
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
    @Operation(
        summary = "Buscar simulação por ID",
        description = "Retorna uma simulação existente com todos os seus detalhes e memória de cálculo"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Simulação encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SimulacaoResponseDTO.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Simulação não encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        ),
        @APIResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ErrorResponseDTO.class)
            )
        )
    })
    public Response buscarSimulacao(
            @Parameter(description = "ID da simulação", required = true, example = "1")
            @PathParam("id") Long id) {
        LOG.info("GET /api/simulacoes/{} - Buscando simulação", id);
        
        try {
            SimulacaoResponseDTO response = simulacaoService.buscarSimulacao(id);
            return Response.ok(response).build();
        } catch (SimulacaoNotFoundException e) {
            LOG.error("Simulação não encontrada: {}", e.getMessage());
            ErrorResponseDTO error = new ErrorResponseDTO(
                404,
                "Not Found",
                e.getMessage(),
                "/api/simulacoes/" + id
            );
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        } catch (Exception e) {
            LOG.error("Erro ao buscar simulação", e);
            ErrorResponseDTO error = new ErrorResponseDTO(
                500,
                "Internal Server Error",
                "Erro ao buscar a simulação",
                "/api/simulacoes/" + id
            );
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    /**
     * Endpoint de health check.
     *
     * @return Status da aplicação
     */
    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Health check",
        description = "Verifica se a API está funcionando"
    )
    @APIResponse(responseCode = "200", description = "API funcionando corretamente")
    public String health() {
        return "Simulador de Financiamentos - API is running!";
    }
}
