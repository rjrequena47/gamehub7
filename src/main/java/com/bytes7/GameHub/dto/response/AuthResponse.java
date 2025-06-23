package com.bytes7.GameHub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta del inicio de sesión")
public class AuthResponse {
    @NotBlank(message = "Ocurrió un error al devolver el token. El token actual está vacío o es nulo.")
    @Schema(description = "Token de autorización del inicio de sesión", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}

