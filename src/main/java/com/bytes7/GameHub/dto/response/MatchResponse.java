package com.bytes7.GameHub.dto.response;

import java.util.UUID;

import com.bytes7.GameHub.model.enums.MatchStatus;
import com.bytes7.GameHub.model.enums.Result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResponse {

    private UUID id;
    private UUID tournamentId;
    private UUID player1Id;
    private UUID player2Id;
    private MatchStatus status;
    private Result result;
}
