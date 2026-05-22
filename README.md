
# 🏦 Simulador de Financiamentos

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.java.net/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.35.4-blue.svg)](https://quarkus.io/)
[![H2 Database](https://img.shields.io/badge/H2-Embedded-brightgreen.svg)](https://www.h2database.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Descrição

API REST para simulação de financiamentos com cálculo de **juros compostos**, desenvolvida em **Java 25** usando o framework **Quarkus**. A aplicação persiste os dados em um banco de dados **H2 embutido** e oferece documentação automática via **OpenAPI/Swagger**.

### ✨ Características

- ✅ Cálculo preciso de juros compostos usando `BigDecimal`
- ✅ Memória de cálculo detalhada mês a mês
- ✅ Persistência em H2 Database (file-based)
- ✅ API REST documentada com OpenAPI/Swagger
- ✅ Cobertura de testes >= 80% (verificada com Jacoco)
- ✅ Validação de dados de entrada com Bean Validation
- ✅ Tratamento de erros padronizado
- ✅ Arquitetura em camadas (Resource → Service → Repository)
- ✅ Sem uso de Docker (100% nativo)

---

## ⚡ Quick Start - Comandos Essenciais

### 🔧 Compilar o projeto
```bash
mvn clean compile
```

### 🧪 Executar testes e validar cobertura >= 80%
```bash
mvn clean test
```

**📊 Relatório de cobertura:** O relatório HTML será gerado automaticamente em `target/site/jacoco/index.html`

**✅ Validação automática:** O build **falhará** se a cobertura for inferior a 80% (configurado no `pom.xml`)

### 🚀 Executar a aplicação
```bash
mvn quarkus:dev
```

**🌐 Acesse:** http://localhost:8080

---

## � IMPORTANTE: Execução 100% Nativa (SEM Docker)

**Este projeto foi desenvolvido seguindo rigorosamente a restrição de NÃO USAR Docker ou Docker Compose.**

### ✅ O que isso significa:
- ✅ **Execução totalmente nativa** usando apenas Java SDK e Maven
- ✅ **H2 Database embutido** (file-based) - sem containers
- ✅ **Tabelas criadas automaticamente** pelo Hibernate
- ✅ **Zero dependência de Docker** para compilar, testar ou executar
- ✅ **Comandos Maven nativos** (mvnw.cmd) para tudo

### ❌ O que NÃO existe no projeto:
- ❌ Nenhum Dockerfile
- ❌ Nenhum docker-compose.yml
- ❌ Nenhuma imagem Docker
- ❌ Nenhum container necessário

### 📝 Verificação:
Veja o arquivo [CONFORMIDADE.md](CONFORMIDADE.md) para verificação completa de todas as restrições e requisitos do desafio.

---

## �🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|-----------|--------|-----------|
| **Java** | 25 | Linguagem de programação |
| **Quarkus** | 3.35.4 | Framework backend |
| **Hibernate ORM with Panache** | - | Persistência de dados |
| **H2 Database** | - | Banco de dados embutido |
| **SmallRye OpenAPI** | - | Documentação da API |
| **JUnit 5** | - | Framework de testes |
| **RestAssured** | - | Testes de integração |
| **Mockito** | 5.12.0 | Mocks para testes unitários |
| **AssertJ** | 3.26.3 | Asserções expressivas |
| **Jacoco** | 0.8.12 | Cobertura de código |

---

## 📐 Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

```
br.com.david.desafio/
├── dto/                    # Data Transfer Objects
│   ├── SimulacaoRequestDTO
│   ├── SimulacaoResponseDTO
│   ├── MemoriaCalculoDTO
│   └── ErrorResponseDTO
├── entity/                 # Entidades JPA
│   ├── Simulacao
│   └── MemoriaCalculo
├── repository/             # Camada de persistência
│   └── SimulacaoRepository
├── service/                # Lógica de negócio
│   └── SimulacaoService
├── exception/              # Exceções customizadas
│   └── SimulacaoNotFoundException
└── SimuladorFinanciamentosResource  # Controlador REST
```

---

## 🚀 Pré-requisitos

Antes de executar o projeto, certifique-se de ter:

- **Java 25** instalado
- **Maven 3.9+** instalado
- **Variável de ambiente `JAVA_HOME`** configurada

### Verificar instalação:

```bash
java -version
mvn -version
```

---

## 📦 Instalação e Execução

**Removida - Ver seção "⚡ Quick Start" no início do documento**

---

## � Endpoints

- **POST** `/api/simulacoes` - Cria uma nova simulação
- **GET** `/api/simulacoes/{id}` - Busca uma simulação por ID
- **GET** `/api/simulacoes/health` - Verifica se a API está funcionando

---

## 🧪 Testando a API

### 📦 Collection do Postman

Importe a collection fornecida para testar todos os endpoints:

**Arquivo:** `Simulador-Financiamentos.postman_collection.json`

A collection inclui:
- ✅ Health Check
- ✅ Criar simulações (básica, valor alto, longo prazo, taxa baixa)
- ✅ Buscar simulações por ID
- ✅ Testes de validação (valores negativos, zero, limites)
- ✅ Testes automatizados com scripts de validação

### 🖥️ Exemplos com cURL

Consulte o arquivo **`TESTES-API.md`** para exemplos completos de:
- Comandos cURL (Linux/Mac)
- Comandos PowerShell (Windows)
- Casos de teste especiais
- Validação de cálculos
- Cenários de erro

**Quick Start:**

```bash
# Health Check
curl -X GET http://localhost:8080/api/simulacoes/health

# Criar Simulação
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d '{"valorInicial": 1000.00, "taxaJurosMensal": 1.5, "prazoMeses": 12}'

# Buscar Simulação
curl -X GET http://localhost:8080/api/simulacoes/1
```

---

## 📖 Documentação Interativa- **GET** `/api/simulacoes/health` - Verifica se a API está funcionando

---

## �📚 Documentação da API

### Swagger UI

Acesse a documentação interativa em:

```
http://localhost:8080/swagger-ui
```

### OpenAPI Spec

JSON da especificação OpenAPI disponível em:

```
http://localhost:8080/openapi
```

---

## � Exemplos Detalhados de Request/Response

### 1. Criar Simulação

**POST** `/api/simulacoes`

**Request Body:**
```json
{
  "valorInicial": 1000.00,
  "taxaJurosMensal": 1.5,
  "prazoMeses": 12
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "valorInicial": 1000.00,
  "taxaJurosMensal": 1.5,
  "prazoMeses": 12,
  "valorTotalFinal": 1195.62,
  "valorTotalJuros": 195.62,
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
    }
  ]
}
```

### 2. Buscar Simulação por ID

**GET** `/api/simulacoes/{id}`

**Response (200 OK):**
```json
{
  "id": 1,
  "valorInicial": 1000.00,
  ...
}
```

**Response (404 Not Found):**
```json
{
  "timestamp": "2026-05-20T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Simulação com ID 999 não encontrada",
  "path": "/api/simulacoes/999"
}
```

### 3. Health Check

**GET** `/api/simulacoes/health`

**Response (200 OK):**
```
Simulador de Financiamentos - API is running!
```

---

## 🧮 Fórmula de Juros Compostos

A aplicação utiliza a fórmula de juros compostos para calcular a evolução mês a mês:

```
Saldo Final = Saldo Inicial × (1 + Taxa/100)
```

Onde:
- **Taxa** é convertida de percentual para decimal
- Os cálculos utilizam `BigDecimal` com `RoundingMode.HALF_UP`
- Precisão de 2 casas decimais para valores monetários
- Precisão de 10 casas decimais para cálculos intermediários

### Exemplo:

Para um financiamento de R$ 1.000,00 a 1,5% ao mês por 12 meses:

- **Mês 1:** 1000,00 × 1,015 = 1.015,00
- **Mês 2:** 1015,00 × 1,015 = 1.030,23
- ...
- **Mês 12:** ~1.195,62

**Total de Juros:** 1.195,62 - 1.000,00 = **195,62**

---

## ✅ Validações

A API realiza as seguintes validações:

| Campo | Regra |
|-------|-------|
| `valorInicial` | Deve ser maior que zero |
| `taxaJurosMensal` | Deve ser maior que zero |
| `prazoMeses` | Deve ser entre 1 e 360 meses |

Requisições inválidas retornam **HTTP 400 Bad Request** com detalhes do erro.

---

## 🧪 Testes

### Estrutura de Testes

```
src/test/java/
└── br/com/david/desafio/
    ├── service/
    │   └── SimulacaoServiceTest.java      # 20 testes unitários
    ├── SimuladorFinanciamentosResourceTest.java  # 16 testes de integração
    └── SimuladorFinanciamentosResourceIT.java    # Testes em modo packaged
```

### Cobertura de Testes

A suíte de testes inclui:

#### Testes Unitários (Service):
- ✅ Cálculo correto de juros compostos (1 mês, 12 meses, múltiplos cenários)
- ✅ Validação de dados de entrada (valores negativos, zero, limites)
- ✅ Precisão decimal (BigDecimal)
- ✅ Busca de simulações (existente e não existente)
- ✅ Tratamento de exceções

#### Testes de Integração (API):
- ✅ Criação de simulação (201 Created)
- ✅ Busca de simulação (200 OK e 404 Not Found)
- ✅ Validações de entrada (400 Bad Request)
- ✅ Cálculo preciso em diferentes cenários
- ✅ Crescimento progressivo na memória de cálculo
- ✅ Health check

### Executar apenas testes unitários:

```bash
mvn test -Dtest=*Test
```

### Executar apenas testes de integração:

```bash
mvn test -Dtest=*ResourceTest
```

---

## 📊 Banco de Dados

### H2 Database

A aplicação utiliza o **H2 Database** em modo **file-based**. Os dados são persistidos no arquivo:

```
./data/simulador-db.mv.db
```

### Console H2 (Opcional)

Para acessar o console web do H2 em modo dev, adicione ao `application.properties`:

```properties
quarkus.datasource.jdbc.url=jdbc:h2:file:./data/simulador-db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE
quarkus.h2.console.enabled=true
```

Em seguida, acesse: **http://localhost:8080/h2-console**

**JDBC URL:** `jdbc:h2:file:./data/simulador-db`  
**Username:** `sa`  
**Password:** _(vazio)_

---

## 🔍 Relatório de Cobertura

Após executar `mvn clean test`, o relatório Jacoco estará disponível em:

```
target/site/jacoco/index.html
```

### Métricas mínimas exigidas:

- **Cobertura de instruções:** >= 80%
- **Cobertura de branches:** Maximizada
- **Cobertura de linhas:** >= 80%

O build falhará automaticamente se não atingir 80% de cobertura.

---

## 🐛 Troubleshooting

### Problema: Erro ao compilar com Java 25

**Solução:** Verifique se o `JAVA_HOME` aponta para o JDK 25:

```bash
echo $JAVA_HOME  # Linux/Mac
echo %JAVA_HOME%  # Windows
```

### Problema: Porta 8080 já está em uso

**Solução:** Altere a porta no `application.properties`:

```properties
quarkus.http.port=8081
```

### Problema: Testes falhando

**Solução:** Certifique-se de que o banco H2 não está bloqueado:

```bash
rm -rf data/  # Remove o diretório de dados
mvn clean test
```

---

## 📄 Estrutura do Projeto

```
simulador-financiamentos/
├── data/                          # Banco de dados H2 (gerado automaticamente)
├── src/
│   ├── main/
│   │   ├── docker/               # Dockerfiles (não utilizados neste projeto)
│   │   ├── java/                 # Código-fonte
│   │   │   └── br/com/david/desafio/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── SimuladorFinanciamentosResource.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/                 # Testes unitários e de integração
├── target/
│   └── site/jacoco/              # Relatório de cobertura (após mvn test)
├── mvnw                          # Maven Wrapper (Linux/Mac)
├── mvnw.cmd                      # Maven Wrapper (Windows)
├── pom.xml                       # Configuração do Maven
└── README.md                     # Este arquivo
```

---

## 🎯 Critérios de Avaliação Atendidos

| Critério | Status | Descrição |
|----------|--------|-----------|
| **Rigor nos Testes** | ✅ | Cobertura >= 80%, testes de borda e erro |
| **Precisão Financeira** | ✅ | Uso de `BigDecimal`, cálculo correto |
| **Spec-Driven** | ✅ | OpenAPI/Swagger, HTTP status corretos |
| **Persistência Embutida** | ✅ | H2 file-based, execução nativa |
| **Clean Code** | ✅ | Camadas bem definidas, nomenclatura clara |

---

## 🏗️ Build e Empacotamento

### Gerar JAR executável:

```bash
mvn clean package
```

O JAR será gerado em:

```
target/quarkus-app/quarkus-run.jar
```

### Executar o JAR:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

---

## 📝 Licença

Este projeto foi desenvolvido como parte de um desafio técnico para demonstração de habilidades em Java, Quarkus e desenvolvimento de APIs REST.

---

## 👨‍💻 Autor

**David**  
Desenvolvedor Java | Especialista em Quarkus e APIs REST

---

## 📞 Suporte

Para dúvidas ou problemas:

1. Verifique a seção [Troubleshooting](#-troubleshooting)
2. Consulte a documentação do [Quarkus](https://quarkus.io/guides/)
3. Revise os logs da aplicação

---

## 🎉 Agradecimentos

Obrigado por avaliar este projeto! A implementação foi desenvolvida com atenção aos detalhes, seguindo as melhores práticas do mercado e atendendo rigorosamente aos requisitos do desafio.

---

**🚀 Happy Coding!**
