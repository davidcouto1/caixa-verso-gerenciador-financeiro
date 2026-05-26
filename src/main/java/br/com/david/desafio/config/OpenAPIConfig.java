package br.com.david.desafio.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
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
            
            ## Características:
            - ✅ Cálculo preciso de juros compostos usando BigDecimal
            - ✅ Memória de cálculo detalhada mês a mês
            - ✅ Persistência em H2 Database embutido
            - ✅ Validação completa de dados de entrada
            - ✅ Tratamento de erros padronizado
            - ✅ Observabilidade com MicroProfile Health e Metrics
            
            ## Fórmula de Juros Compostos:
            ```
            Saldo Final = Saldo Inicial × (1 + Taxa/100)
            ```
            
            ## Validações:
            - Valor inicial: maior que zero
            - Taxa de juros: maior que zero
            - Prazo: entre 1 e 360 meses
            
            ## Tecnologias:
            - Java 25
            - Quarkus 3.35.4
            - H2 Database (embedded)
            - MicroProfile OpenAPI
            """,
        contact = @Contact(
            name = "David Couto Bitencourt - Desenvolvedor Java",
            email = "davidcouto1@gmail.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
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
        ),
        @Tag(
            name = "Health",
            description = "Endpoints de monitoramento da saúde da aplicação"
        ),
        @Tag(
            name = "Metrics",
            description = "Endpoints de métricas e observabilidade"
        )
    }
)
public class OpenAPIConfig extends Application {
}
