package com.bytes7.GameHub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Schema(description = "Datos para iniciar sesión")
public class LoginRequest {
    @Schema(description = "Correo electrónico del usuario", example = "email@mail.com")
    private String email;
    @Schema(description = "Contraseña del usuario", example = "claveSegura123")
    private String password;
}
