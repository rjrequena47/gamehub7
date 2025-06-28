package com.bytes7.GameHub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentRankingDTO {
    private String playerId;
    private String username;
    private int victories;
    private int defeats;
    private int matchesPlayed;
    private int points;
}
