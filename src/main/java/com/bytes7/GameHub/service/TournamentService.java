package com.bytes7.GameHub.service;

import com.bytes7.GameHub.dto.request.CreateTournamentRequest;
import com.bytes7.GameHub.dto.response.TournamentResponse;
import com.bytes7.GameHub.model.entity.Tournament;
import com.bytes7.GameHub.model.enums.Status;
import com.bytes7.GameHub.repository.TournamentRepository;
import com.bytes7.GameHub.model.entity.User;
import com.bytes7.GameHub.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;

    public TournamentService(TournamentRepository tournamentRepository, UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
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

    public TournamentResponse getTournamentById(UUID id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        return TournamentResponse.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .maxPlayers(tournament.getMaxPlayers())
                .status(tournament.getStatus())
                .build();
    }

    public TournamentResponse joinTournament(UUID tournamentId) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User player = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (tournament.getPlayers().contains(player)) {
            throw new RuntimeException("Ya estás inscrito en este torneo");
        }

        if (tournament.getPlayers().size() >= tournament.getMaxPlayers()) {
            throw new RuntimeException("El torneo ya alcanzó el máximo de jugadores");
        }

        tournament.getPlayers().add(player);
        tournamentRepository.save(tournament);

        return TournamentResponse.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .maxPlayers(tournament.getMaxPlayers())
                .status(tournament.getStatus())
                .build();
    }
}
