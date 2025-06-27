package com.bytes7.GameHub.dto.response;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta del controlador de torneos")
public class MessageResponse {
    @NotBlank(message = "El remitente no puede estar vacío")
    @Schema(description = "Remitente del mensaje", example = "usuario123")
    private String sender;

    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    @Schema(description = "Contenido del mensaje", example = "¡Hola a todos!")
    private String content;

    @NotBlank(message = "La marca de tiempo no puede ser nula")
    @Schema(description = "Marca de tiempo del mensaje", example = "2023-10-01T12:00:00")
    private LocalDateTime timestamp;
    
    @NotBlank(message = "El ID del emparejamiento no puede estar vacío")
    @Schema(description = "ID del emparejamiento al que pertenece el mensaje", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID match;

    @NotBlank(message = "El ID del torneo no puede estar vacío")
    @Schema(description = "ID del torneo al que pertenece el mensaje", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID tournament;
    
    @Override
    public String toString() {
        return "Mensaje enviado por " + sender + " en la fecha " + timestamp.toString() + ": " + content + "\n";
    }
}
