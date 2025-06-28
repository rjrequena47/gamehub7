package com.bytes7.GameHub.service;

import com.bytes7.GameHub.exception.ResourceNotFoundException;
import com.bytes7.GameHub.model.entity.Match;
import com.bytes7.GameHub.model.entity.Tournament;
import com.bytes7.GameHub.model.entity.User;
import com.bytes7.GameHub.model.enums.Result;
import com.bytes7.GameHub.model.enums.MatchStatus;
import com.bytes7.GameHub.repository.MatchRepository;
import com.bytes7.GameHub.repository.TournamentRepository;
import com.bytes7.GameHub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;

    public Match createMatch(UUID tournamentId, UUID player1Id, UUID player2Id) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado"));

        User player1 = userRepository.findById(player1Id)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador 1 no encontrado"));

        User player2 = userRepository.findById(player2Id)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador 2 no encontrado"));

        Match match = new Match();
        match.setTournament(tournament);
        match.setPlayer1(player1);
        match.setPlayer2(player2);
        match.setStatus(MatchStatus.PENDING);

        return matchRepository.save(match);
    }

    @Transactional
    public Match updateMatchResult(UUID matchId, Result result) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Partido no encontrado"));

        if (match.getStatus() == MatchStatus.FINISHED) {
            throw new IllegalStateException("El partido ya finalizó");
        }

        match.setResult(result);
        match.setStatus(MatchStatus.FINISHED);

        return matchRepository.save(match);
    }


    public List<Match> getMatchesByTournament(UUID tournamentId) {
        return matchRepository.findByTournamentId(tournamentId);
    }

    public Match getMatchById(UUID id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partido no encontrado con ID: " + id));
    }

    @Transactional
    public List<Match> generateMatchups(UUID tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado"));

        List<User> players = tournament.getPlayers();

        if (players.size() < 2) {
            throw new IllegalStateException("Se necesitan al menos 2 jugadores para emparejar");
        }

        Collections.shuffle(players); // Aleatorizar orden

        // Si es impar, asignamos un Bye aleatorio
        if (players.size() % 2 != 0) {
            User byePlayer = players.remove(0); // Saca al primer jugador tras mezclar

            Match byeMatch = Match.builder()
                    .tournament(tournament)
                    .player1(byePlayer)
                    .player2(null)
                    .status(MatchStatus.FINISHED)
                    .result(Result.PLAYER1_WON)
                    .build();

            matchRepository.save(byeMatch);
        }

        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < players.size() - 1; i += 2) {
            Match match = Match.builder()
                .tournament(tournament)
                .player1(players.get(i))
                .player2(players.get(i + 1))
                .status(MatchStatus.PENDING)
                .build();
            matches.add(matchRepository.save(match));
        }

        return matches;
    }

}
