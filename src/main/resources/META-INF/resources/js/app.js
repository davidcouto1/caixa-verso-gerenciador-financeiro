// Simulador de Financiamentos - JavaScript

// Configurações da API
const API_BASE_URL = '/api/simulacoes';

// Elementos do DOM
let form, buscaForm, loading, alertBox, resultado;

// Inicialização quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', function() {
    initializeElements();
    attachEventListeners();
});

/**
 * Inicializa as referências dos elementos do DOM
 */
function initializeElements() {
    form = document.getElementById('simuladorForm');
    buscaForm = document.getElementById('buscaForm');
    loading = document.getElementById('loading');
    alertBox = document.getElementById('alert');
    resultado = document.getElementById('resultado');
}

/**
 * Anexa os event listeners aos elementos
 */
function attachEventListeners() {
    if (form) {
        form.addEventListener('submit', handleSubmit);
    }
    
    if (buscaForm) {
        buscaForm.addEventListener('submit', handleBuscaSubmit);
    }
    
    const btnLimpar = document.getElementById('btnLimpar');
    if (btnLimpar) {
        btnLimpar.addEventListener('click', limparFormulario);
    }
    
    const btnNovaSimulacao = document.getElementById('btnNovaSimulacao');
    if (btnNovaSimulacao) {
        btnNovaSimulacao.addEventListener('click', novaSimulacao);
    }
}

/**
 * Manipula o submit do formulário
 */
async function handleSubmit(event) {
    event.preventDefault();
    
    // Coleta os dados do formulário
    const formData = {
        valorInicial: parseFloat(document.getElementById('valorInicial').value),
        taxaJurosMensal: parseFloat(document.getElementById('taxaJurosMensal').value),
        prazoMeses: parseInt(document.getElementById('prazoMeses').value)
    };
    
    // Valida os dados antes de enviar
    if (!validarDados(formData)) {
        return;
    }
    
    // Envia para a API
    await criarSimulacao(formData);
}

/**
 * Manipula o submit do formulário de busca
 */
async function handleBuscaSubmit(event) {
    event.preventDefault();
    
    // Coleta o ID da simulação
    const simulacaoId = parseInt(document.getElementById('simulacaoId').value);
    
    // Valida o ID
    if (!simulacaoId || simulacaoId < 1) {
        mostrarAlerta('Por favor, informe um ID válido (maior que zero)', 'error');
        return;
    }
    
    // Busca a simulação
    await buscarSimulacao(simulacaoId);
}

/**
 * Valida os dados do formulário
 */
function validarDados(data) {
    if (data.valorInicial <= 0) {
        mostrarAlerta('O valor inicial deve ser maior que zero', 'error');
        return false;
    }
    
    if (data.taxaJurosMensal <= 0) {
        mostrarAlerta('A taxa de juros deve ser maior que zero', 'error');
        return false;
    }
    
    if (data.prazoMeses < 1 || data.prazoMeses > 360) {
        mostrarAlerta('O prazo deve estar entre 1 e 360 meses', 'error');
        return false;
    }
    
    return true;
}

/**
 * Cria uma nova simulação via API
 */
async function criarSimulacao(data) {
    try {
        // Mostra loading
        mostrarLoading(true);
        esconderAlerta();
        esconderResultado();
        
        // Faz a requisição POST
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        // Processa a resposta
        const responseData = await response.json();
        
        if (response.ok) {
            // Sucesso (201 Created)
            mostrarResultado(responseData);
            mostrarAlerta('✅ Simulação criada com sucesso!', 'success');
        } else {
            // Erro (400, 500, etc)
            const errorMessage = responseData.message || 'Erro ao criar simulação';
            mostrarAlerta(errorMessage, 'error');
        }
        
    } catch (error) {
        console.error('Erro ao comunicar com a API:', error);
        mostrarAlerta('❌ Erro ao comunicar com o servidor. Verifique se a API está rodando.', 'error');
    } finally {
        mostrarLoading(false);
    }
}

/**
 * Mostra ou esconde o loading
 */
function mostrarLoading(show) {
    if (loading) {
        loading.classList.toggle('show', show);
    }
}

/**
 * Mostra um alerta
 */
function mostrarAlerta(mensagem, tipo) {
    if (!alertBox) return;
    
    alertBox.className = 'alert';
    alertBox.classList.add('show');
    alertBox.classList.add('alert-' + tipo);
    alertBox.textContent = mensagem;
    
    // Auto-esconde após 5 segundos para mensagens de sucesso
    if (tipo === 'success') {
        setTimeout(() => {
            esconderAlerta();
        }, 5000);
    }
}

/**
 * Esconde o alerta
 */
function esconderAlerta() {
    if (alertBox) {
        alertBox.classList.remove('show');
    }
}

/**
 * Mostra o resultado da simulação
 */
function mostrarResultado(data) {
    if (!resultado) return;
    
    // Preenche os dados principais
    document.getElementById('resultId').textContent = data.id;
    document.getElementById('resultValorInicial').textContent = formatarMoeda(data.valorInicial);
    document.getElementById('resultTaxa').textContent = data.taxaJurosMensal + '%';
    document.getElementById('resultPrazo').textContent = data.prazoMeses + ' meses';
    document.getElementById('resultValorFinal').textContent = formatarMoeda(data.valorTotalFinal);
    document.getElementById('resultJuros').textContent = formatarMoeda(data.valorTotalJuros);
    
    // Preenche a tabela de memória de cálculo
    preencherTabelaMemoria(data.memoriaCalculos);
    
    // Mostra a seção de resultado
    resultado.classList.add('show');
    
    // Scroll suave até o resultado
    setTimeout(() => {
        resultado.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }, 100);
}

/**
 * Esconde o resultado
 */
function esconderResultado() {
    if (resultado) {
        resultado.classList.remove('show');
    }
}

/**
 * Preenche a tabela de memória de cálculo
 */
function preencherTabelaMemoria(memoriaCalculos) {
    const tbody = document.getElementById('tabelaMemoriaBody');
    if (!tbody) return;
    
    // Limpa o conteúdo anterior
    tbody.innerHTML = '';
    
    // Adiciona as linhas
    memoriaCalculos.forEach(memoria => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${memoria.mes}</td>
            <td>${formatarMoeda(memoria.saldoInicial)}</td>
            <td>${formatarMoeda(memoria.juro)}</td>
            <td>${formatarMoeda(memoria.saldoFinal)}</td>
        `;
        tbody.appendChild(tr);
    });
}

/**
 * Formata um valor para moeda brasileira
 */
function formatarMoeda(valor) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(valor);
}

/**
 * Limpa o formulário
 */
function limparFormulario() {
    if (form) {
        form.reset();
    }
    if (buscaForm) {
        buscaForm.reset();
    }
    esconderAlerta();
    esconderResultado();
    
    // Foca no campo de busca
    const campoBusca = document.getElementById('simulacaoId');
    if (campoBusca) {
        campoBusca.focus();
    }
}

/**
 * Inicia uma nova simulação
 */
function novaSimulacao() {
    limparFormulario();
    
    // Scroll até o formulário
    if (form) {
        form.scrollIntoView({ behavior: 'smooth' });
    }
}

/**
 * Busca uma simulação existente por ID (função auxiliar)
 */
async function buscarSimulacao(id) {
    try {
        mostrarLoading(true);
        esconderAlerta();
        
        const response = await fetch(`${API_BASE_URL}/${id}`);
        const data = await response.json();
        
        if (response.ok) {
            mostrarResultado(data);
            mostrarAlerta(`✅ Simulação #${id} carregada com sucesso!`, 'success');
        } else {
            const errorMessage = data.message || 'Simulação não encontrada';
            mostrarAlerta(errorMessage, 'error');
        }
        
    } catch (error) {
        console.error('Erro ao buscar simulação:', error);
        mostrarAlerta('❌ Erro ao comunicar com o servidor.', 'error');
    } finally {
        mostrarLoading(false);
    }
}

/**
 * Verifica o status da API (função auxiliar)
 */
async function verificarHealthCheck() {
    try {
        const response = await fetch(`${API_BASE_URL}/health`);
        const text = await response.text();
        
        if (response.ok) {
            console.log('✅ API está funcionando:', text);
            return true;
        } else {
            console.error('❌ API retornou erro:', response.status);
            return false;
        }
    } catch (error) {
        console.error('❌ Erro ao verificar API:', error);
        return false;
    }
}

// Exporta funções úteis para o escopo global (para uso em console/debug)
window.SimuladorAPI = {
    criarSimulacao,
    buscarSimulacao,
    verificarHealthCheck,
    limparFormulario
};
