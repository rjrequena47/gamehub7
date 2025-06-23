package com.bytes7.GameHub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Datos para crear un nuevo torneo")
public class CreateTournamentRequest {
    
    @Schema(description = "Nombre del torneo", example = "Torneo de Fin de Semana")
    private String name;

    @Schema(description = "Cantidad máxima de jugadores", example = "8")
    private int maxPlayers;
}
