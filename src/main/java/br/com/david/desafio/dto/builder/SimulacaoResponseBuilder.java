package br.com.david.desafio.dto.builder;

import br.com.david.desafio.dto.MemoriaCalculoDTO;
import br.com.david.desafio.dto.SimulacaoResponseDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder para construção fluente de SimulacaoResponseDTO.
 * 
 * Padrão GoF: Builder Pattern
 * Benefícios: Construção clara e fluente de objetos complexos
 * 
 * @author David
 * @since 1.0
 */
public class SimulacaoResponseBuilder {
    
    private Long id;
    private BigDecimal valorInicial;
    private BigDecimal taxaJurosMensal;
    private Integer prazoMeses;
    private BigDecimal valorTotalFinal;
    private BigDecimal valorTotalJuros;
    private List<MemoriaCalculoDTO> memoriaCalculos = new ArrayList<>();
    
    private SimulacaoResponseBuilder() {
    }
    
    /**
     * Cria uma nova instância do Builder.
     * 
     * @return Nova instância do Builder
     */
    public static SimulacaoResponseBuilder builder() {
        return new SimulacaoResponseBuilder();
    }
    
    /**
     * Define o ID da simulação.
     * 
     * @param id ID da simulação
     * @return Builder atual para chamadas encadeadas
     */
    public SimulacaoResponseBuilder id(Long id) {
        this.id = id;
        return this;
    }
    
    /**
     * Define o valor inicial.
     * 
     * @param valorInicial Valor inicial do financiamento
     * @return Builder atual para chamadas encadeadas
     */
    public SimulacaoResponseBuilder valorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
        return this;
    }
    
    /**
     * Define a taxa de juros mensal.
     * 
     * @param taxaJurosMensal Taxa de juros em percentual
     * @return Builder atual para chamadas encadeadas
     */
    public SimulacaoResponseBuilder taxaJurosMensal(BigDecimal taxaJurosMensal) {
        this.taxaJurosMensal = taxaJurosMensal;
        return this;
    }
    
    /**
     * Define o prazo em meses.
     * 
     * @param prazoMeses Prazo do financiamento
     * @return Builder atual para chamadas encadeadas
     */
    public SimulacaoResponseBuilder prazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
        return this;
    }
    
    /**
     * Define o valor total final.
     * 
     * @param valorTotalFinal Valor final do financiamento
     * @return Builder atual para chamadas encadeadas
     */
    public SimulacaoResponseBuilder valorTotalFinal(BigDecimal valorTotalFinal) {
        this.valorTotalFinal = valorTotalFinal;
        return this;
    }
    
    /**
     * Define o valor total de juros.
     * 
     * @param valorTotalJuros Total de juros pagos
     * @return Builder atual para chamadas encadeadas
     */
    public SimulacaoResponseBuilder valorTotalJuros(BigDecimal valorTotalJuros) {
        this.valorTotalJuros = valorTotalJuros;
        return this;
    }
    
    /**
     * Define a memória de cálculos.
     * 
     * @param memoriaCalculos Lista de memória de cálculos
     * @return Builder atual para chamadas encadeadas
     */
    public SimulacaoResponseBuilder memoriaCalculos(List<MemoriaCalculoDTO> memoriaCalculos) {
        this.memoriaCalculos = memoriaCalculos;
        return this;
    }
    
    /**
     * Adiciona um item à memória de cálculos.
     * 
     * @param memoriaCalculo Item de memória de cálculo
     * @return Builder atual para chamadas encadeadas
     */
    public SimulacaoResponseBuilder addMemoriaCalculo(MemoriaCalculoDTO memoriaCalculo) {
        this.memoriaCalculos.add(memoriaCalculo);
        return this;
    }
    
    /**
     * Constrói a instância final de SimulacaoResponseDTO.
     * 
     * @return Instância construída
     */
    public SimulacaoResponseDTO build() {
        return new SimulacaoResponseDTO(
            id,
            valorInicial,
            taxaJurosMensal,
            prazoMeses,
            valorTotalFinal,
            valorTotalJuros,
            memoriaCalculos
        );
    }
}
