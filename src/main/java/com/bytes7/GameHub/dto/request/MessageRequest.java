package com.bytes7.GameHub.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta del controlador de torneos")
public class MessageRequest {
    @Schema(description = "ID del mensaje", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "El ID del mensaje no puede ser nulo")
    private UUID id;

    @NotBlank(message = "El remitente no puede estar vacío")
    @Schema(description = "Remitente del mensaje", example = "usuario123")
    private String sender;

    @Schema(description = "Contenido del mensaje", example = "¡Hola a todos!")
    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    private String content;
    
    @Schema(description = "Marca de tiempo del mensaje", example = "2023-10-01T12:00:00")
    @NotNull(message = "La marca de tiempo no puede ser nula")
    private LocalDateTime timestamp;

    @Schema(description = "Nombre del torneo", example = "Torneo de Primavera")
    @NotBlank(message = "El nombre del torneo no puede estar vacío")
    private String tournamentName;

    @Schema(description = "ID del emparejamiento al que pertenece el mensaje", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "El ID del emparejamiento no puede ser nulo")
    private UUID matchID;
}