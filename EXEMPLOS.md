# Exemplos de Uso da API

Aplicação deve estar rodando em `http://localhost:8080`

Para iniciar: `mvn quarkus:dev`

## Criar Simulação

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d '{"valorInicial": 1000.00, "taxaJurosMensal": 1.5, "prazoMeses": 12}'
```

Resposta (201 Created):
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

## Buscar Simulação por ID

```bash
curl -X GET http://localhost:8080/api/simulacoes/1
```

Resposta (200 OK):
```json
{
  "id": 1,
  "valorInicial": 1000.00,
  ...
}
```

## Buscar Simulação Inexistente

```bash
curl -X GET http://localhost:8080/api/simulacoes/999
```

Resposta (404 Not Found):
```json
{
  "timestamp": "2026-05-26T00:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Simulação com ID 999 não encontrada",
  "path": "/api/simulacoes/999"
}
```

## Health Check

```bash
curl -X GET http://localhost:8080/q/health
```

Resposta (200 OK):
```json
{
  "status": "UP",
  "checks": [
    {
      "name": "Simulador de Financiamentos - Liveness",
      "status": "UP"
    },
    {
      "name": "Database Connection - H2",
      "status": "UP"
    }
  ]
}
```

## Métricas

```bash
curl -X GET http://localhost:8080/q/metrics/application
```

Resposta (formato Prometheus):
```
# HELP simulacoes_criadas_total Número de simulações criadas
# TYPE simulacoes_criadas_total counter
simulacoes_criadas_total 42.0
```

## Validações de Erro

### Valor inicial negativo

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d '{"valorInicial": -1000.00, "taxaJurosMensal": 1.5, "prazoMeses": 12}'
```

Resposta (400 Bad Request):
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "O valor inicial deve ser maior que zero"
}
```

### Taxa de juros zero

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d '{"valorInicial": 1000.00, "taxaJurosMensal": 0, "prazoMeses": 12}'
```

Resposta (400 Bad Request)

### Prazo acima do limite (360 meses)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d '{"valorInicial": 1000.00, "taxaJurosMensal": 1.5, "prazoMeses": 500}'
```

Resposta (400 Bad Request):
```json
{
  "message": "O prazo máximo permitido é de 360 meses"
}
```

## Outros Cenários

### Valor alto

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d '{"valorInicial": 100000.00, "taxaJurosMensal": 2.5, "prazoMeses": 24}'
```

### Taxa baixa

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d '{"valorInicial": 5000.00, "taxaJurosMensal": 0.5, "prazoMeses": 6}'
```

### Longo prazo

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d '{"valorInicial": 50000.00, "taxaJurosMensal": 1.0, "prazoMeses": 120}'
```

## Collection do Postman

Para testar todos os endpoints de forma mais fácil, importe a collection incluída no projeto:

**Arquivo:** `Simulador-Financiamentos.postman_collection.json`

A collection já possui 21 requests configurados com testes automatizados.
