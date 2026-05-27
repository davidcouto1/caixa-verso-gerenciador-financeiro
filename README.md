# Simulador de Financiamentos

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.java.net/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.35.4-blue.svg)](https://quarkus.io/)

API REST para simulação de financiamentos com cálculo de juros compostos. Desenvolvida em Java 25 com Quarkus, persiste dados em H2 e oferece documentação via Swagger.

**Principais características:**
- Cálculo de juros compostos com BigDecimal
- Memória de cálculo mês a mês
- Persistência em H2 Database
- Cobertura de testes: 93%
- Validação de entrada com Bean Validation

## Quick Start

```bash
# Compilar
mvn clean compile
```

```bash
# Executar
mvn quarkus:dev
```

Acesse: http://localhost:8080

```bash
# Testes
mvn clean test
```

**Relatório de cobertura de testes:** Após executar os testes, o relatório do Jacoco é gerado em `target/site/jacoco/index.html`

## Arquitetura e Padrões de Projeto

O projeto utiliza arquitetura em camadas (Resource → Service → Repository) seguindo padrões GoF (Gang of Four) para facilitar manutenção e extensibilidade.

**Padrões GoF implementados:**
- Strategy Pattern para cálculo de juros (JurosSimplesStrategy, JurosCompostosStrategy)
- Factory Pattern para criação de entidades (SimulacaoFactory)
- Builder Pattern para construção fluente de objetos
- Repository Pattern para abstração de persistência

**Princípios SOLID aplicados:**
- Single Responsibility (separação de responsabilidades entre camadas)
- Dependency Inversion (uso de CDI e interfaces)
- Open/Closed (extensibilidade via Strategy)

## Especificações MicroProfile

- **MicroProfile Health**: Liveness/Readiness checks em `/q/health`
- **MicroProfile Metrics**: Métricas Prometheus em `/q/metrics`
- **MicroProfile Config**: Configurações externalizadas
- **MicroProfile Fault Tolerance**: @Timeout e @Retry no Service
- **MicroProfile OpenAPI**: Swagger UI em `/swagger-ui`

## Tecnologias

- Java 25
- Maven 3.9.15
- Quarkus 3.35.4  
- H2 Database (embedded)
- Hibernate ORM with Panache
- JUnit 5 + RestAssured
- Jacoco (93% cobertura)

## Estrutura do Projeto

```
br.com.david.desafio/
├── config/                 # Configurações (OpenAPI)
├── dto/                    # Data Transfer Objects
├── entity/                 # Entidades JPA
├── repository/             # Camada de persistência
├── service/                # Lógica de negócio
├── strategy/               # Strategy Pattern (cálculos)
├── factory/                # Factory Pattern
├── health/                 # Health Checks MicroProfile
├── exception/              # Exceções e mappers
└── SimuladorFinanciamentosResource.java
```

## Endpoints

### API de Negócio
- **POST** `/api/simulacoes` - Cria uma nova simulação
- **GET** `/api/simulacoes/{id}` - Busca uma simulação por ID

### Endpoints MicroProfile
- **GET** `/q/health` - Status geral da aplicação
- **GET** `/q/health/live` - Liveness probe
- **GET** `/q/health/ready` - Readiness probe
- **GET** `/q/metrics` - Métricas Prometheus
- **GET** `/swagger-ui` - Interface Swagger UI
- **GET** `/openapi` - Especificação OpenAPI

## Testando a API

### Collection do Postman

Importe a collection fornecida: `Simulador-Financiamentos.postman_collection.json`

### Exemplos com cURL

```bash
# Criar Simulação
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d '{"valorInicial": 1000.00, "taxaJurosMensal": 1.5, "prazoMeses": 12}'

# Buscar Simulação
curl -X GET http://localhost:8080/api/simulacoes/1

# Health Check
curl -X GET http://localhost:8080/q/health
```

Consulte `EXEMPLOS.md` para mais cenários de teste.

## Banco de Dados

A aplicação utiliza H2 Database em modo file-based. Os dados são persistidos em:

```
./data/simulador-db.mv.db
```

As tabelas são criadas automaticamente pelo Hibernate.

## Testes

Total de testes: **99** | Cobertura: **93%**

```bash
# Executar todos os testes
mvn clean test

# Executar testes específicos
mvn test -Dtest=SimulacaoServiceTest
```

**Cobertura por pacote:**
- Service: 100%
- Strategy: 100%
- Factory: 100%
- Repository: 100%
- Resource: 100%

Relatório HTML: `target/site/jacoco/index.html`

## Validações

A API realiza as seguintes validações:

- `valorInicial` deve ser maior que zero
- `taxaJurosMensal` deve ser maior que zero
- `prazoMeses` deve estar entre 1 e 360 meses

Requisições inválidas retornam HTTP 400 Bad Request.

## Cálculo de Juros Compostos

Fórmula aplicada mês a mês:

```
Saldo Final = Saldo Inicial × (1 + Taxa/100)
```

Os cálculos utilizam `BigDecimal` com `RoundingMode.HALF_UP`:
- Precisão de 2 casas decimais para valores monetários
- Precisão de 10 casas decimais para cálculos intermediários

## Build e Empacotamento

```bash
# Gerar JAR executável
mvn clean package

# Executar o JAR
java -jar target/quarkus-app/quarkus-run.jar
```

## Troubleshooting

**Porta 8080 já está em uso:**
Altere a porta no `application.properties`:
```properties
quarkus.http.port=8081
```

**Testes falhando:**
Remova o diretório de dados e execute novamente:
```bash
rm -rf data/
mvn clean test
```

## Autor

**David Couto Bitencourt**  
Desenvolvedor Java

**Email:** davidcouto1@gmail.com
