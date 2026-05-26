package br.com.david.desafio.exception.mapper;

import br.com.david.desafio.dto.ErrorResponseDTO;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * Exception Mapper genérico para capturar exceções não tratadas por mappers específicos.
 * 
 * Retorna HTTP 500 (Internal Server Error) para exceções inesperadas,
 * mas preserva o status de WebApplicationException do JAX-RS.
 * 
 * Este mapper tem a menor prioridade e só é acionado quando nenhum
 * mapper específico trata a exceção.
 * 
 * @author David
 * @since 1.0
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GenericExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        // Se for uma WebApplicationException, preserva o status HTTP original
        if (exception instanceof WebApplicationException) {
            WebApplicationException webEx = (WebApplicationException) exception;
            int status = webEx.getResponse().getStatus();
            
            LOG.warnf("WebApplicationException capturada: %s (Status: %d)", 
                      exception.getMessage(), status);
            
            // Verifica se há uma mensagem customizada (não a mensagem padrão do JAX-RS)
            String message = exception.getMessage();
            String reasonPhrase = Response.Status.fromStatusCode(status).getReasonPhrase();
            
            // Se a mensagem é null, vazia ou igual ao status HTTP padrão, usa mensagem genérica
            if (message == null || message.trim().isEmpty() || message.startsWith("HTTP " + status)) {
                message = "Erro na requisição";
            }
            
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                    status,
                    reasonPhrase,
                    message,
                    uriInfo.getPath()
            );
            
            return Response
                    .status(status)
                    .entity(errorResponse)
                    .build();
        }

        // Para outras exceções, retorna 500 Internal Server Error
        LOG.errorf(exception, "Erro não tratado na aplicação: %s", exception.getMessage());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Erro interno do servidor. Por favor, contate o suporte.",
                uriInfo.getPath()
        );

        // Em ambiente de produção, não exponha detalhes da exceção
        // Apenas registre no log para análise
        LOG.errorf("Stack trace: %s", getStackTrace(exception));

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }

    /**
     * Gera uma string com o stack trace da exceção para logging.
     */
    private String getStackTrace(Exception exception) {
        StringBuilder sb = new StringBuilder();
        sb.append(exception.getClass().getName()).append(": ").append(exception.getMessage()).append("\n");
        
        for (StackTraceElement element : exception.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        
        if (exception.getCause() != null) {
            sb.append("Caused by: ").append(exception.getCause().toString()).append("\n");
        }
        
        return sb.toString();
    }
}
