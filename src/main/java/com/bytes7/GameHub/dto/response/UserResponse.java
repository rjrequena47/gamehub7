package com.bytes7.GameHub.dto.response;

import java.util.UUID;

import com.bytes7.GameHub.model.enums.Role;

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
@Schema(description = "Respuesta del buscador de usuarios")
public class UserResponse {
    @NotNull(message = "Ocurrió un error al devolver la id.")
    @Schema(description = "ID del usuario", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @NotBlank(message = "Ocurrió un error al devolver el nombre de usuario.")
    @Schema(description = "Nombre del usuario", example = "Ugulberto")
    private String username;

    @NotBlank(message = "Ocurrió un error al devolver el email del usuario.")
    @Schema(description = "Email del usuario", example = "email@mail.com")
    private String email;

    @NotNull(message = "Ocurrió un error al devolver el rol del usuario.")
    @Schema(description = "Rol del usuario", example = "ADMIN")
    private Role role;

    @NotNull(message = "Ocurrió un error al devolver los puntos del usuario.")
    @Schema(description = "Puntos del usuario", example = "27")
    private int points;
}
