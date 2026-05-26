package br.com.david.desafio.exception.mapper;

import br.com.david.desafio.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * Exception Mapper para tratar ConstraintViolationException.
 * 
 * Retorna HTTP 400 (Bad Request) com lista detalhada de erros de validação
 * quando as validações do Bean Validation falham.
 * 
 * @author David
 * @since 1.0
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOG = Logger.getLogger(ConstraintViolationExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        LOG.warnf("Violação de validação detectada: %d erro(s)", exception.getConstraintViolations().size());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                Response.Status.BAD_REQUEST.getStatusCode(),
                Response.Status.BAD_REQUEST.getReasonPhrase(),
                "Erro de validação nos dados fornecidos",
                uriInfo.getPath()
        );

        // Adiciona cada erro de validação à lista de erros
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String fieldName = getFieldName(violation.getPropertyPath().toString());
            String errorMessage = String.format("%s: %s", fieldName, violation.getMessage());
            errorResponse.addError(errorMessage);
            LOG.debugf("Erro de validação: %s", errorMessage);
        }

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }

    /**
     * Extrai o nome do campo do caminho da propriedade.
     * Exemplo: "criarSimulacao.request.valorInicial" -> "valorInicial"
     */
    private String getFieldName(String propertyPath) {
        if (propertyPath == null || propertyPath.isEmpty()) {
            return "campo";
        }
        
        String[] parts = propertyPath.split("\\.");
        return parts[parts.length - 1];
    }
}
