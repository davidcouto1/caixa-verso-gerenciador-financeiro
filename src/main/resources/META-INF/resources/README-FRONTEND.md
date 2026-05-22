# 🎨 Frontend - Simulador de Financiamentos

## 📁 Estrutura de Arquivos

```
src/main/resources/META-INF/resources/
├── index.html              # Página inicial (Home)
├── simulador.html          # Página do simulador com formulário
├── documentacao.html       # Documentação completa do projeto
├── css/
│   └── styles.css         # Estilos CSS (responsivo)
└── js/
    └── app.js             # Lógica JavaScript para comunicação com API
```

## 🌐 Páginas

### 1. **Home Page** (`index.html`)
- ✅ Página inicial com apresentação do projeto
- ✅ 4 features principais em grid responsivo
- ✅ Links para: Funcionalidades, Documentação, Swagger UI, Health Check
- ✅ Design com gradiente roxo e cards estilizados

**Acesso:** `http://localhost:8080/` ou `http://localhost:8080/index.html`

---

### 2. **Simulador** (`simulador.html`)
- ✅ Formulário completo para criar simulações
- ✅ Validação de dados em tempo real
- ✅ Exibição de resultados com destaque visual
- ✅ Tabela de memória de cálculo mês a mês (com scroll)
- ✅ Loading spinner durante requisições
- ✅ Alertas de sucesso/erro
- ✅ Botão para nova simulação

**Campos do formulário:**
- 💰 Valor Inicial (R$)
- 📈 Taxa de Juros Mensal (%)
- 📅 Prazo (meses)

**Acesso:** `http://localhost:8080/simulador.html`

---

### 3. **Documentação** (`documentacao.html`)
- ✅ Documentação completa do projeto
- ✅ Endpoints da API com exemplos de Request/Response
- ✅ Fórmula matemática de juros compostos
- ✅ Regras de validação
- ✅ Instruções de como testar (Swagger, cURL, Postman)
- ✅ Arquitetura do sistema
- ✅ Informações sobre o banco de dados
- ✅ Como executar o projeto

**Acesso:** `http://localhost:8080/documentacao.html`

---

## 🎨 CSS (`styles.css`)

### Características:
- ✅ **Responsivo:** Adapta-se a mobile, tablet e desktop
- ✅ **Gradiente:** Background com degradê roxo (#667eea → #764ba2)
- ✅ **Componentes:** Botões, cards, formulários, tabelas, alertas
- ✅ **Animações:** Transições suaves e efeitos hover
- ✅ **Tabela com scroll:** Memória de cálculo com cabeçalho fixo
- ✅ **Loading spinner:** Indicador visual de carregamento

### Paleta de Cores:
- **Primária:** #667eea (roxo)
- **Sucesso:** #28a745 (verde)
- **Erro:** #dc3545 (vermelho)
- **Fundo:** Linear gradient roxo
- **Cards:** Branco com sombras

---

## ⚙️ JavaScript (`app.js`)

### Funcionalidades:

#### 1. **Comunicação com API**
```javascript
const API_BASE_URL = '/api/simulacoes';
```

#### 2. **Funções Principais**
- `criarSimulacao(data)` - POST para criar nova simulação
- `buscarSimulacao(id)` - GET para buscar simulação existente
- `verificarHealthCheck()` - GET para verificar status da API
- `validarDados(data)` - Validação client-side antes de enviar

#### 3. **Manipulação do DOM**
- `mostrarResultado(data)` - Exibe resultado com formatação
- `preencherTabelaMemoria(memoria)` - Preenche tabela dinamicamente
- `mostrarAlerta(msg, tipo)` - Exibe alertas (success/error/info)
- `mostrarLoading(show)` - Controla loading spinner

#### 4. **Formatação**
- `formatarMoeda(valor)` - Formata valores para BRL (R$)

#### 5. **Utilitários Exportados**
```javascript
window.SimuladorAPI = {
    criarSimulacao,
    buscarSimulacao,
    verificarHealthCheck,
    limparFormulario
};
```

**Console Debug:**
```javascript
// Testar no console do navegador
SimuladorAPI.verificarHealthCheck();
SimuladorAPI.buscarSimulacao(1);
```

---

## 🚀 Como o Quarkus Serve os Arquivos

O Quarkus automaticamente serve arquivos estáticos do diretório:
```
src/main/resources/META-INF/resources/
```

**Funcionamento:**
1. Quarkus detecta arquivos `.html`, `.css`, `.js` no diretório
2. Serve na raiz da aplicação (`http://localhost:8080/`)
3. `index.html` é servido automaticamente em `/`
4. Outros arquivos são acessíveis pelo nome: `/simulador.html`, `/css/styles.css`, etc.

**Vantagens:**
- ✅ Não precisa configurar servidor de arquivos estáticos
- ✅ Tudo em uma única aplicação (backend + frontend)
- ✅ Hot reload também funciona para arquivos estáticos em dev mode

---

## 📱 Responsividade

### Breakpoints:
- **Mobile:** < 768px
  - Grid de features: 1 coluna
  - Botões empilhados verticalmente
  - Tabela com fonte menor
  
- **Tablet/Desktop:** ≥ 768px
  - Grid de features: 2-4 colunas (auto-fit)
  - Botões lado a lado
  - Tabela com fonte normal

### Testado em:
- ✅ Chrome/Edge
- ✅ Firefox
- ✅ Safari (mobile)
- ✅ Telas de 320px a 1920px

---

## 🧪 Testando o Frontend

### 1. Iniciar a aplicação
```bash
mvnw.cmd quarkus:dev
```

### 2. Acessar no navegador
- Home: http://localhost:8080/
- Simulador: http://localhost:8080/simulador.html
- Documentação: http://localhost:8080/documentacao.html

### 3. Testar funcionalidades
1. ✅ Criar uma simulação no formulário
2. ✅ Ver resultado com memória de cálculo
3. ✅ Validar campos inválidos (valores negativos, zero, etc)
4. ✅ Limpar formulário
5. ✅ Criar nova simulação
6. ✅ Verificar responsividade (redimensionar janela)

---

## 🔧 Personalização

### Alterar cores:
Edite `css/styles.css`:
```css
/* Cor primária */
.btn-primary {
    background: #667eea;  /* Sua cor aqui */
}

/* Gradiente de fundo */
body {
    background: linear-gradient(135deg, #SuaCor1, #SuaCor2);
}
```

### Adicionar nova página:
1. Crie `nova-pagina.html` em `META-INF/resources/`
2. Use `<link rel="stylesheet" href="/css/styles.css">`
3. Acesse em `http://localhost:8080/nova-pagina.html`

---

## 📦 Build para Produção

O frontend é automaticamente incluído no build:

```bash
# Build nativo (Maven)
mvn clean package

# O JAR gerado inclui todos os arquivos estáticos
# target/simulador-financiamentos-1.0.0-SNAPSHOT-runner.jar
```

**Deploy:** Apenas execute o JAR e acesse a aplicação!

---

## 🎯 Separação Frontend/Backend

### ✅ Antes (Misturado):
```java
@Path("/")
@GET
@Produces(MediaType.TEXT_HTML)
public String home() {
    return "<html>...</html>";  // HTML no Java ❌
}
```

### ✅ Depois (Separado):
```
Java (API REST):
- SimuladorFinanciamentosResource.java (apenas endpoints JSON)

Frontend (Arquivos estáticos):
- index.html
- simulador.html
- documentacao.html
- css/styles.css
- js/app.js
```

**Benefícios:**
- ✅ API REST limpa (apenas JSON)
- ✅ Frontend separado e manutenível
- ✅ Fácil de testar separadamente
- ✅ Possível usar outro frontend (React, Vue, etc)
- ✅ Código organizado e profissional

---

**🎉 Frontend completo e pronto para uso!**
