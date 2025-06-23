package com.bytes7.GameHub.service;

import com.bytes7.GameHub.constant.ExceptionMessages;
import com.bytes7.GameHub.dto.request.CreateTournamentRequest;
import com.bytes7.GameHub.dto.response.TournamentResponse;
import com.bytes7.GameHub.exception.custom.ResourceNotFoundException;
import com.bytes7.GameHub.exception.custom.TournamentFullException;
import com.bytes7.GameHub.exception.custom.UserAlreadyJoinedException;
import com.bytes7.GameHub.model.entity.Tournament;
import com.bytes7.GameHub.model.entity.User;
import com.bytes7.GameHub.model.enums.Status;
import com.bytes7.GameHub.repository.TournamentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.bytes7.GameHub.repository.UserRepository;
import com.bytes7.GameHub.service.use_cases.TournamentUseCase;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TournamentService implements TournamentUseCase {

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

    @Override
    public TournamentResponse getTournament(UUID tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ExceptionMessages.TOURNAMENT_NOT_FOUND, tournamentId)));

        return TournamentResponse.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .maxPlayers(tournament.getMaxPlayers())
                .status(tournament.getStatus())
                .build();
    }

    @Override
    public void joinTournament(UUID tournamentId) {
        // Obtener el username del usuario autenticado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Buscar usuario
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ExceptionMessages.USER_NOT_FOUND, username))
        );

        // Buscar torneo
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ExceptionMessages.TOURNAMENT_NOT_FOUND, tournamentId))
                );

        // Validar si ya está inscrito
        if (tournament.getPlayers().contains(user)) {
            throw new UserAlreadyJoinedException(ExceptionMessages.USER_ALREADY_JOINED);
        }

        // Validar si hay cupos
        if (tournament.getPlayers().size() >= tournament.getMaxPlayers()) {
            throw new TournamentFullException(ExceptionMessages.TOURNAMENT_FULL);
        }

        // Agregar al jugador
        tournament.getPlayers().add(user);
        tournamentRepository.save(tournament);
    }
}
