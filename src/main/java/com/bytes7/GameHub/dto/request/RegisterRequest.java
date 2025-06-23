package com.bytes7.GameHub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Datos para registro")
public class RegisterRequest {
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Schema(description = "Nombre de usuario", example = "usuario123")
    private String username;

    @NotBlank(message = "El email no puede estar vacío")
    @Schema(description = "Correo electrónico del usuario", example = "email@mail.com")
    private String email;
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Schema(description = "Contraseña del usuario", example = "claveSegura123")
    private String password;
}
