package com.bytes7.GameHub.dto.request;

import com.bytes7.GameHub.model.enums.Result;
import jakarta.validation.constraints.NotNull;

public record UpdateResultRequest(@NotNull Result result) { }
