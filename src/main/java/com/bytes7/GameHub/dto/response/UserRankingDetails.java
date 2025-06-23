package com.bytes7.GameHub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "UserRankingDetails", description = "Detalles del ranking de un usuario")
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserRankingDetails {
    // Atributos, nombre y puntuación del usuario
    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Schema(description = "Nombre de usuario", example = "Ugulberto")
    private String username;
    
    @NotNull(message = "Los puntos no pueden ser nulos.")
    @Schema(description = "Puntos", example = "200")
    private Integer points;
    
    // Sobrescribe un metodo de la clase Object
    @Override 
    public String toString() {
        return "Player {" + "username = \'" + username + "\'" + ", points = " + points + "}\n";
    }
}
