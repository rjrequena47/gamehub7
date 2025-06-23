package com.bytes7.GameHub.dto.response;

import java.util.UUID;

import com.bytes7.GameHub.model.enums.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@Schema(description = "Respuesta del controlador de torneos")
public class TournamentResponse {
    @NotNull(message = "Ocurrió un error al devolver la ID. La ID actual es nula")
    @Schema(description = "ID del torneo", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @NotBlank(message = "Ocurrió un error al devolver el nombre del torneo, actualmente está vacío")
    @Schema(description = "Nombre del torneo", example = "Torneo de Fin de Semana")
    private String name;

    @NotNull(message = "Ocurrió un error al devolver el número máximo de jugadores")
    @Schema(description = "Máximo de jugadores del torneo", example = "50")
    private int maxPlayers;

    @NotBlank(message = "Ocurrió un error al devolver el estado actual del torneo")
    @Schema(description = "Estado actual del torneo", example = "FINISHED")
    private Status status;
}
