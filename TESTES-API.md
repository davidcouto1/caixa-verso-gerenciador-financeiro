# 🧪 Exemplos de Testes da API - Simulador de Financiamentos

## 📋 Pré-requisitos
- Aplicação rodando em `http://localhost:8080`
- Para iniciar: `mvnw.cmd quarkus:dev`

---

## ✅ 1. Health Check

```bash
curl -X GET http://localhost:8080/api/simulacoes/health
```

**Resposta esperada:**
```
Simulador de Financiamentos - API is running!
```

---

## 📊 2. Criar Simulação - Básica

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 1000.00, \"taxaJurosMensal\": 1.5, \"prazoMeses\": 12}"
```

**Resposta esperada (201 Created):**
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
    },
    ...
  ]
}
```

---

## 🔍 3. Buscar Simulação por ID

```bash
curl -X GET http://localhost:8080/api/simulacoes/1
```

**Resposta esperada (200 OK):**
```json
{
  "id": 1,
  "valorInicial": 1000.00,
  "taxaJurosMensal": 1.5,
  "prazoMeses": 12,
  "valorTotalFinal": 1195.62,
  "valorTotalJuros": 195.62,
  "memoriaCalculos": [...]
}
```

---

## 💰 4. Simulação com Valor Alto

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 100000.00, \"taxaJurosMensal\": 2.5, \"prazoMeses\": 24}"
```

**Resultado:** R$ 100.000,00 a 2,5% a.m. por 24 meses

---

## 📅 5. Simulação de Longo Prazo

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 50000.00, \"taxaJurosMensal\": 1.0, \"prazoMeses\": 120}"
```

**Resultado:** R$ 50.000,00 a 1,0% a.m. por 120 meses (10 anos)

---

## 📉 6. Simulação com Taxa Baixa

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 5000.00, \"taxaJurosMensal\": 0.5, \"prazoMeses\": 6}"
```

**Resultado:** R$ 5.000,00 a 0,5% a.m. por 6 meses

---

## ❌ 7. Testes de Validação

### 7.1 Valor Inicial Negativo (Erro 400)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": -1000.00, \"taxaJurosMensal\": 1.5, \"prazoMeses\": 12}"
```

**Resposta esperada (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "O valor inicial deve ser maior que zero",
  "path": "/api/simulacoes"
}
```

### 7.2 Valor Inicial Zero (Erro 400)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 0, \"taxaJurosMensal\": 1.5, \"prazoMeses\": 12}"
```

### 7.3 Taxa de Juros Negativa (Erro 400)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 1000.00, \"taxaJurosMensal\": -1.5, \"prazoMeses\": 12}"
```

### 7.4 Taxa de Juros Zero (Erro 400)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 1000.00, \"taxaJurosMensal\": 0, \"prazoMeses\": 12}"
```

### 7.5 Prazo Zero (Erro 400)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 1000.00, \"taxaJurosMensal\": 1.5, \"prazoMeses\": 0}"
```

### 7.6 Prazo Negativo (Erro 400)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 1000.00, \"taxaJurosMensal\": 1.5, \"prazoMeses\": -12}"
```

### 7.7 Prazo Excedido - Máximo 360 meses (Erro 400)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 1000.00, \"taxaJurosMensal\": 1.5, \"prazoMeses\": 361}"
```

### 7.8 Buscar Simulação Inexistente (Erro 404)

```bash
curl -X GET http://localhost:8080/api/simulacoes/99999
```

**Resposta esperada (404 Not Found):**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Simulação com ID 99999 não encontrada",
  "path": "/api/simulacoes/99999"
}
```

---

## 🧮 8. Casos de Teste Especiais

### 8.1 Prazo Máximo Permitido (360 meses / 30 anos)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 200000.00, \"taxaJurosMensal\": 0.8, \"prazoMeses\": 360}"
```

### 8.2 Taxa Decimal Pequena (0,01%)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 1000.00, \"taxaJurosMensal\": 0.01, \"prazoMeses\": 12}"
```

### 8.3 Taxa Alta (10% a.m.)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 1000.00, \"taxaJurosMensal\": 10.0, \"prazoMeses\": 12}"
```

**Resultado esperado:** Crescimento exponencial significativo (juros compostos)

### 8.4 Prazo Mínimo (1 mês)

```bash
curl -X POST http://localhost:8080/api/simulacoes \
  -H "Content-Type: application/json" \
  -d "{\"valorInicial\": 1000.00, \"taxaJurosMensal\": 1.5, \"prazoMeses\": 1}"
```

**Resultado esperado:** R$ 1.015,00 (apenas 1 mês de juros)

---

## 📖 9. Acessar Documentação Swagger

Abra no navegador:
```
http://localhost:8080/swagger-ui
```

Ou acesse a especificação OpenAPI:
```
http://localhost:8080/openapi
```

---

## 🎨 10. Acessar Página Inicial Customizada

```
http://localhost:8080/
```

Você verá a página de boas-vindas com:
- Descrição do projeto
- Features principais
- Links para Swagger UI e Health Check

---

## 🧪 11. PowerShell - Versão Windows

Se você estiver usando PowerShell no Windows, use esta sintaxe:

```powershell
# Health Check
Invoke-RestMethod -Uri "http://localhost:8080/api/simulacoes/health" -Method GET

# Criar Simulação
$body = @{
    valorInicial = 1000.00
    taxaJurosMensal = 1.5
    prazoMeses = 12
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/simulacoes" -Method POST -ContentType "application/json" -Body $body

# Buscar Simulação
Invoke-RestMethod -Uri "http://localhost:8080/api/simulacoes/1" -Method GET
```

---

## 📊 12. Verificar Cálculos

Para validar se os juros compostos estão corretos, use a fórmula:

```
M = C × (1 + i)^n

Onde:
- M = Montante final
- C = Capital inicial (valorInicial)
- i = Taxa de juros (taxaJurosMensal / 100)
- n = Número de períodos (prazoMeses)
```

**Exemplo:** R$ 1.000,00 a 1,5% a.m. por 12 meses
```
M = 1000 × (1 + 0.015)^12
M = 1000 × 1.195618...
M ≈ R$ 1.195,62
```

✅ **Precisão garantida:** Todos os cálculos usam `BigDecimal` com arredondamento `HALF_UP` e 2 casas decimais.

---

## 🎯 13. Cenários de Teste Recomendados

Execute nesta ordem para validar o sistema completo:

1. ✅ Health Check
2. ✅ Criar simulação básica
3. ✅ Buscar simulação criada
4. ✅ Criar simulação com valor alto
5. ✅ Criar simulação de longo prazo
6. ❌ Testar todas as validações (valores inválidos)
7. ❌ Buscar ID inexistente
8. ✅ Verificar cálculos com diferentes cenários
9. 📖 Explorar documentação Swagger
10. 🧪 Rodar testes automatizados: `mvnw.cmd test`

---

**🎉 Sistema testado e aprovado!**
