package com.bytes7.GameHub.dto.request;

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
@Schema(description = "Datos para crear un nuevo torneo")
public class CreateTournamentRequest {
    
    @Schema(description = "Nombre del torneo", example = "Torneo de Fin de Semana")
    @NotBlank(message = "El nombre del torneo no puede estar vacío")
    private String name;

    @Schema(description = "Cantidad máxima de jugadores", example = "8")
    @NotNull(message = "La cantidad máxima de jugadores no puee ser null")
    private int maxPlayers;
}
