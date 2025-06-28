package com.bytes7.GameHub.dto.response;

import com.bytes7.GameHub.model.enums.Status;
import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TournamentResponse {
    private UUID id;
    private String name;
    private int maxPlayers;
    private Status status;
}
