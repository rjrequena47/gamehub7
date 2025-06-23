package com.bytes7.GameHub.service.use_cases;

import com.bytes7.GameHub.dto.request.CreateTournamentRequest;
import com.bytes7.GameHub.dto.response.TournamentResponse;

import java.util.List;
import java.util.UUID;

public interface TournamentUseCase {
    TournamentResponse createTournament(CreateTournamentRequest request);
    List<TournamentResponse> getAllTournaments();
    TournamentResponse getTournament(UUID tournamentId);
    void joinTournament(UUID tournamentId);

}
