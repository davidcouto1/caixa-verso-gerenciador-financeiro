package br.com.david.desafio;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;

/**
 * Redireciona a raiz para o Swagger UI
 */
@Path("/")
public class RedirectResource {

    @GET
    public Response redirectToSwagger() {
        return Response.seeOther(URI.create("/swagger-ui")).build();
    }
}
