# Exception Mappers

Este pacote contém os **Exception Mappers** que centralizam o tratamento de exceções na API REST.

## 📋 Mappers Implementados

### 1️⃣ SimulacaoNotFoundExceptionMapper
**Exceção tratada:** `SimulacaoNotFoundException`  
**Status HTTP:** `404 Not Found`  
**Quando ocorre:** Quando uma simulação buscada não existe no banco de dados

**Exemplo de resposta:**
```json
{
  "timestamp": "2026-05-24T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Simulação com ID 999 não encontrada",
  "path": "/api/simulacoes/999",
  "errors": []
}
```

---

### 2️⃣ ConstraintViolationExceptionMapper
**Exceção tratada:** `ConstraintViolationException` (Bean Validation)  
**Status HTTP:** `400 Bad Request`  
**Quando ocorre:** Quando os dados de entrada violam as validações do Bean Validation

**Exemplo de resposta:**
```json
{
  "timestamp": "2026-05-24T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação nos dados fornecidos",
  "path": "/api/simulacoes",
  "errors": [
    "valorInicial: deve ser maior que 0",
    "taxaJurosMensal: deve ser maior que 0",
    "prazoMeses: deve ser entre 1 e 360"
  ]
}
```

---

### 3️⃣ GenericExceptionMapper
**Exceção tratada:** `Exception` (genérica) e `WebApplicationException`  
**Status HTTP:** `500 Internal Server Error` (ou status original da WebApplicationException)  
**Quando ocorre:** Quando ocorre uma exceção não tratada por mappers específicos

**Exemplo de resposta:**
```json
{
  "timestamp": "2026-05-24T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Erro interno do servidor. Por favor, contate o suporte.",
  "path": "/api/simulacoes",
  "errors": []
}
```

**⚠️ Nota de segurança:** Detalhes técnicos da exceção são registrados apenas nos logs, não são expostos ao cliente.

---

## 🎯 Benefícios

✅ **Centralização:** Todo o tratamento de erros em um único lugar  
✅ **Consistência:** Respostas padronizadas em toda a API  
✅ **Manutenibilidade:** Fácil adicionar novos mappers ou modificar comportamento  
✅ **Logging:** Registro automático de todas as exceções  
✅ **Segurança:** Exceções internas não são expostas ao cliente  
✅ **Profissionalismo:** Respostas de erro detalhadas e informativas  

---

## 🔧 Como funciona

Os **Exception Mappers** são componentes do JAX-RS anotados com `@Provider` que implementam a interface `ExceptionMapper<T>`.

O Quarkus/JAX-RS automaticamente:
1. Detecta todos os mappers no classpath
2. Quando uma exceção é lançada, busca o mapper mais específico
3. Invoca o método `toResponse()` do mapper
4. Retorna a resposta HTTP ao cliente

### Hierarquia de prioridade:
1. Mapper mais específico (ex: SimulacaoNotFoundException)
2. Mappers intermediários (ex: ConstraintViolationException)
3. Mapper genérico (Exception.class)

---

## 📝 Como adicionar um novo mapper

```java
@Provider
public class MinhaExcecaoMapper implements ExceptionMapper<MinhaExcecao> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(MinhaExcecao exception) {
        ErrorResponseDTO error = new ErrorResponseDTO(
            400,
            "Bad Request",
            exception.getMessage(),
            uriInfo.getPath()
        );
        
        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(error)
            .build();
    }
}
```

---

## 🧪 Testes

Os exception mappers são testados automaticamente através dos testes de integração da API:

- `SimuladorFinanciamentosResourceTest` - Testa cenários de erro 400 e 404
- `SimuladorFinanciamentosResourceIT` - Testa a aplicação em modo packaged

---

## 📚 Referências

- [JAX-RS Exception Mappers](https://docs.oracle.com/javaee/7/tutorial/jaxrs-advanced004.htm)
- [Quarkus REST Error Handling](https://quarkus.io/guides/resteasy-reactive#exception-mapping)
- [Bean Validation](https://beanvalidation.org/)
