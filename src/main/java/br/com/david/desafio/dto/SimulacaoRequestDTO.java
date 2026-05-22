package br.com.david.desafio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * DTO para receber os dados de entrada da simulação de financiamento.
 */
@Schema(description = "Dados de entrada para simular um financiamento")
public class SimulacaoRequestDTO {

    @NotNull(message = "O valor inicial é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor inicial deve ser maior que zero")
    @JsonProperty("valorInicial")
    @Schema(description = "Valor principal do financiamento", example = "1000.00", required = true)
    private BigDecimal valorInicial;

    @NotNull(message = "A taxa de juros mensal é obrigatória")
    @DecimalMin(value = "0.01", message = "A taxa de juros deve ser maior que zero")
    @JsonProperty("taxaJurosMensal")
    @Schema(description = "Taxa de juros mensal em percentual", example = "1.5", required = true)
    private BigDecimal taxaJurosMensal;

    @NotNull(message = "O prazo em meses é obrigatório")
    @Min(value = 1, message = "O prazo deve ser de pelo menos 1 mês")
    @JsonProperty("prazoMeses")
    @Schema(description = "Prazo do financiamento em meses", example = "12", required = true)
    private Integer prazoMeses;

    public SimulacaoRequestDTO() {
    }

    public SimulacaoRequestDTO(BigDecimal valorInicial, BigDecimal taxaJurosMensal, Integer prazoMeses) {
        this.valorInicial = valorInicial;
        this.taxaJurosMensal = taxaJurosMensal;
        this.prazoMeses = prazoMeses;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "SimulacaoRequestDTO{" +
                "valorInicial=" + valorInicial +
                ", taxaJurosMensal=" + taxaJurosMensal +
                ", prazoMeses=" + prazoMeses +
                '}';
    }
}
