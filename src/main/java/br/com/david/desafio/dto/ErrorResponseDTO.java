package br.com.david.desafio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para padronizar as respostas de erro da API.
 */
@Schema(description = "Estrutura padronizada de resposta de erro da API")
public class ErrorResponseDTO {

    @JsonProperty("timestamp")
    @Schema(
        description = "Data e hora em que o erro ocorreu (ISO 8601)",
        example = "2026-05-25T19:00:00",
        type = SchemaType.STRING,
        format = "date-time"
    )
    private LocalDateTime timestamp;

    @JsonProperty("status")
    @Schema(
        description = "Código HTTP do erro",
        example = "400",
        type = SchemaType.INTEGER
    )
    private Integer status;

    @JsonProperty("error")
    @Schema(
        description = "Descrição resumida do tipo de erro",
        example = "Bad Request",
        type = SchemaType.STRING
    )
    private String error;

    @JsonProperty("message")
    @Schema(
        description = "Mensagem detalhada do erro",
        example = "Validation failed",
        type = SchemaType.STRING
    )
    private String message;

    @JsonProperty("path")
    @Schema(
        description = "Caminho da requisição que gerou o erro",
        example = "/api/simulacoes",
        type = SchemaType.STRING
    )
    private String path;

    @JsonProperty("errors")
    @Schema(
        description = "Lista de erros específicos de validação (quando aplicável)",
        example = "[\"O valor inicial deve ser maior que zero\"]",
        type = SchemaType.ARRAY
    )
    private List<String> errors;

    public ErrorResponseDTO() {
        this.timestamp = LocalDateTime.now();
        this.errors = new ArrayList<>();
    }

    public ErrorResponseDTO(Integer status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    // Getters and Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
