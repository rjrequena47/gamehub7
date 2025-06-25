package com.bytes7.GameHub.dto.request;

import com.bytes7.GameHub.model.enums.Result;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado del partido, valores permitidos: PLAYER1_WON, PLAYER2_WON, PENDING", example = "PLAYER1_WON", required = true)
public record UpdateResultRequest(@NotNull Result result) { }
