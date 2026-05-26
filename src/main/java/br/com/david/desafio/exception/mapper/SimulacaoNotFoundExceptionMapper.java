package br.com.david.desafio.exception.mapper;

import br.com.david.desafio.dto.ErrorResponseDTO;
import br.com.david.desafio.exception.SimulacaoNotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * Exception Mapper para tratar SimulacaoNotFoundException.
 * 
 * Retorna HTTP 404 (Not Found) quando uma simulação não é encontrada.
 * 
 * @author David
 * @since 1.0
 */
@Provider
public class SimulacaoNotFoundExceptionMapper implements ExceptionMapper<SimulacaoNotFoundException> {

    private static final Logger LOG = Logger.getLogger(SimulacaoNotFoundExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(SimulacaoNotFoundException exception) {
        LOG.warnf("Simulação não encontrada: ID %d", exception.getSimulacaoId());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                uriInfo.getPath()
        );

        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .build();
    }
}
