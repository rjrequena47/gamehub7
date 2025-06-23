package com.bytes7.GameHub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Schema(description = "Datos para registro")
public class RegisterRequest {
    @Schema(description = "Nombre de usuario", example = "usuario123")
    private String username;
    @Schema(description = "Correo electrónico del usuario", example = "email@mail.com")
    private String email;
    @Schema(description = "Contraseña del usuario", example = "claveSegura123")
    private String password;
}
