package br.com.david.desafio.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Configuração do OpenAPI/Swagger para documentação da API.
 * Define informações globais sobre a API, incluindo título, versão, descrição e contato.
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Simulador de Financiamentos API",
        version = "1.0.0",
        description = """
            API REST para simulação de financiamentos com cálculo de juros compostos.
            
            ## Tecnologias:
            - Java 25
            - Maven 3.9.15
            - Quarkus 3.35.4
            - H2 Database (embedded)
            - MicroProfile OpenAPI
            
            **Desenvolvedor:** David Couto Bitencourt
            """
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Servidor de Desenvolvimento"
        ),
        @Server(
            url = "https://api.simuladorfinanceiro.com",
            description = "Servidor de Produção"
        )
    },
    tags = {
        @Tag(
            name = "Simulações",
            description = "Operações para criar e consultar simulações de financiamento"
        )
    }
)
public class OpenAPIConfig extends Application {
}
