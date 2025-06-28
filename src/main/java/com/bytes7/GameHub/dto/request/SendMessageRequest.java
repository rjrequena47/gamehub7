package com.bytes7.GameHub.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SendMessageRequest {

    @NotBlank(message = "El contenido no puede estar vacío")
    private String content;
}
