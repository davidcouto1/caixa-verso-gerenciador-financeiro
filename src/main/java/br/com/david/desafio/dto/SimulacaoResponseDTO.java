package br.com.david.desafio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para retornar os dados completos de uma simulação de financiamento.
 */
@Schema(description = "Resultado completo da simulação de financiamento com memória de cálculo")
public class SimulacaoResponseDTO {

    @JsonProperty("id")
    @Schema(
        description = "Identificador único da simulação no banco de dados",
        example = "1",
        type = SchemaType.INTEGER,
        format = "int64"
    )
    private Long id;

    @JsonProperty("valorInicial")
    @Schema(
        description = "Valor principal do financiamento em reais",
        example = "1000.00",
        type = SchemaType.NUMBER,
        format = "decimal"
    )
    private BigDecimal valorInicial;

    @JsonProperty("taxaJurosMensal")
    @Schema(
        description = "Taxa de juros mensal em percentual aplicada",
        example = "1.50",
        type = SchemaType.NUMBER,
        format = "decimal"
    )
    private BigDecimal taxaJurosMensal;

    @JsonProperty("prazoMeses")
    @Schema(
        description = "Prazo total do financiamento em meses",
        example = "12",
        type = SchemaType.INTEGER
    )
    private Integer prazoMeses;

    @JsonProperty("valorTotalFinal")
    @Schema(
        description = "Valor total ao final do financiamento (principal + juros)",
        example = "1195.63",
        type = SchemaType.NUMBER,
        format = "decimal"
    )
    private BigDecimal valorTotalFinal;

    @JsonProperty("valorTotalJuros")
    @Schema(
        description = "Valor total de juros pagos durante todo o período",
        example = "195.63",
        type = SchemaType.NUMBER,
        format = "decimal"
    )
    private BigDecimal valorTotalJuros;

    @JsonProperty("memoriaCalculos")
    @Schema(
        description = "Detalhamento mês a mês da evolução do financiamento com juros compostos",
        type = SchemaType.ARRAY
    )
    private List<MemoriaCalculoDTO> memoriaCalculos;

    public SimulacaoResponseDTO() {
    }

    public SimulacaoResponseDTO(Long id, BigDecimal valorInicial, BigDecimal taxaJurosMensal, 
                                Integer prazoMeses, BigDecimal valorTotalFinal, 
                                BigDecimal valorTotalJuros, List<MemoriaCalculoDTO> memoriaCalculos) {
        this.id = id;
        this.valorInicial = valorInicial;
        this.taxaJurosMensal = taxaJurosMensal;
        this.prazoMeses = prazoMeses;
        this.valorTotalFinal = valorTotalFinal;
        this.valorTotalJuros = valorTotalJuros;
        this.memoriaCalculos = memoriaCalculos;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public BigDecimal getTaxaJurosMensal() {
        return taxaJurosMensal;
    }

    public void setTaxaJurosMensal(BigDecimal taxaJurosMensal) {
        this.taxaJurosMensal = taxaJurosMensal;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
    }

    public BigDecimal getValorTotalFinal() {
        return valorTotalFinal;
    }

    public void setValorTotalFinal(BigDecimal valorTotalFinal) {
        this.valorTotalFinal = valorTotalFinal;
    }

    public BigDecimal getValorTotalJuros() {
        return valorTotalJuros;
    }

    public void setValorTotalJuros(BigDecimal valorTotalJuros) {
        this.valorTotalJuros = valorTotalJuros;
    }

    public List<MemoriaCalculoDTO> getMemoriaCalculos() {
        return memoriaCalculos;
    }

    public void setMemoriaCalculos(List<MemoriaCalculoDTO> memoriaCalculos) {
        this.memoriaCalculos = memoriaCalculos;
    }

    @Override
    public String toString() {
        return "SimulacaoResponseDTO{" +
                "id=" + id +
                ", valorInicial=" + valorInicial +
                ", taxaJurosMensal=" + taxaJurosMensal +
                ", prazoMeses=" + prazoMeses +
                ", valorTotalFinal=" + valorTotalFinal +
                ", valorTotalJuros=" + valorTotalJuros +
                ", memoriaCalculos=" + memoriaCalculos +
                '}';
    }
}
