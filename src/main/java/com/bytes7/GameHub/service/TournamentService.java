package com.bytes7.GameHub.service;

import com.bytes7.GameHub.dto.request.CreateTournamentRequest;
import com.bytes7.GameHub.dto.response.TournamentResponse;
import com.bytes7.GameHub.model.entity.Tournament;
import com.bytes7.GameHub.model.enums.Status;
import com.bytes7.GameHub.repository.TournamentRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public TournamentResponse createTournament(CreateTournamentRequest request) {
        Tournament tournament = new Tournament();
        tournament.setName(request.getName());
        tournament.setMaxPlayers(request.getMaxPlayers());
        tournament.setStatus(Status.CREATED);

        tournament = tournamentRepository.save(tournament);

        return TournamentResponse.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .maxPlayers(tournament.getMaxPlayers())
                .status(tournament.getStatus())
                .build();
    }

    public List<TournamentResponse> getAllTournaments() {
        return tournamentRepository.findAll()
                .stream()
                .map(tournament -> TournamentResponse.builder()
                        .id(tournament.getId())
                        .name(tournament.getName())
                        .maxPlayers(tournament.getMaxPlayers())
                        .status(tournament.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
