package com.bytes7.GameHub.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    UUID senderId,
    String content,
    LocalDateTime timestamp
) {}