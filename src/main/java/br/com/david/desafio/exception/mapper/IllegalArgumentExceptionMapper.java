package br.com.david.desafio.exception.mapper;

import br.com.david.desafio.dto.ErrorResponseDTO;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * Exception Mapper para tratar IllegalArgumentException.
 * 
 * Retorna HTTP 400 (Bad Request) quando há erros de validação de argumentos.
 * 
 * @author David
 * @since 1.0
 */
@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger LOG = Logger.getLogger(IllegalArgumentExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        LOG.warnf("Argumento inválido detectado: %s", exception.getMessage());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                uriInfo.getPath()
        );

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }
}
