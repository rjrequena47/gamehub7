package com.bytes7.GameHub.service;

import com.bytes7.GameHub.dto.response.TournamentRankingDTO;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Transactional(readOnly = true)
    public List<TournamentRankingDTO> getTournamentRanking(UUID tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado"));

        Map<UUID, PlayerStats> stats = new HashMap<>();

        List<Match> matches = matchRepository.findByTournamentId(tournamentId);

        for (Match match : matches) {
            if (match.getPlayer1() != null) {
                stats.putIfAbsent(match.getPlayer1().getId(), new PlayerStats(match.getPlayer1().getUsername()));
            }
            if (match.getPlayer2() != null) {
                stats.putIfAbsent(match.getPlayer2().getId(), new PlayerStats(match.getPlayer2().getUsername()));
            }

            if (match.getStatus() == MatchStatus.FINISHED) {
                if (match.getPlayer1() != null) stats.get(match.getPlayer1().getId()).incrementMatchesPlayed();
                if (match.getPlayer2() != null) stats.get(match.getPlayer2().getId()).incrementMatchesPlayed();

                if (match.getResult() == Result.PLAYER1_WON && match.getPlayer1() != null) {
                    stats.get(match.getPlayer1().getId()).incrementVictories();
                } else if (match.getResult() == Result.PLAYER2_WON && match.getPlayer2() != null) {
                    stats.get(match.getPlayer2().getId()).incrementVictories();
                }
            }
        }

        return stats.entrySet().stream()
            .map(e -> TournamentRankingDTO.builder()
                    .playerId(e.getKey().toString())
                    .username(e.getValue().getUsername())
                    .victories(e.getValue().getVictories())
                    .matchesPlayed(e.getValue().getMatchesPlayed())
                    .build())
            .sorted(Comparator.comparingInt(TournamentRankingDTO::getVictories).reversed())
            .toList();
    }

    public List<TournamentRankingDTO> calculateTournamentRanking(UUID tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado"));

        Map<UUID, PlayerStats> stats = new HashMap<>();

        for (Match match : matchRepository.findByTournamentId(tournament.getId())) {

            if (match.getStatus() != MatchStatus.FINISHED) continue;

            UUID player1Id = match.getPlayer1().getId();
            UUID player2Id = match.getPlayer2() != null ? match.getPlayer2().getId() : null;

            stats.putIfAbsent(player1Id, new PlayerStats(match.getPlayer1().getUsername()));
            stats.get(player1Id).incrementMatchesPlayed();

            if (player2Id != null) {
                stats.putIfAbsent(player2Id, new PlayerStats(match.getPlayer2().getUsername()));
                stats.get(player2Id).incrementMatchesPlayed();
            }

            if (match.getResult() == Result.PLAYER1_WON) {
                stats.get(player1Id).incrementVictories();
                if (player2Id != null) stats.get(player2Id).incrementDefeats();

            } else if (match.getResult() == Result.PLAYER2_WON && player2Id != null) {
                stats.get(player2Id).incrementVictories();
                stats.get(player1Id).incrementDefeats();
            }
        }

        return stats.entrySet().stream()
                .map(e -> TournamentRankingDTO.builder()
                        .playerId(e.getKey().toString())
                        .username(e.getValue().getUsername())
                        .victories(e.getValue().getVictories())
                        .defeats(e.getValue().getDefeats())
                        .matchesPlayed(e.getValue().getMatchesPlayed())
                        .points(e.getValue().getPoints())
                        .build())
                .sorted(Comparator.comparingInt(TournamentRankingDTO::getPoints).reversed())
                .toList();
    }

    // Clase auxiliar interna
    private static class PlayerStats {

        private final String username;
        private int victories;
        private int defeats;
        private int matchesPlayed;
        private int points;

        public PlayerStats(String username) {
            this.username = username;
            this.victories = 0;
            this.defeats = 0;
            this.matchesPlayed = 0;
            this.points = 0;
        }

        public void incrementVictories() {
            this.victories++;
            this.points += 3; // Sistema clásico: 3 puntos por victoria
        }

        public void incrementDefeats() {
            this.defeats++;
            // 0 puntos por derrota
        }

        public void incrementMatchesPlayed() {
            this.matchesPlayed++;
        }

        public String getUsername() {
            return username;
        }

        public int getVictories() {
            return victories;
        }

        public int getDefeats() {
            return defeats;
        }

        public int getMatchesPlayed() {
            return matchesPlayed;
        }

        public int getPoints() {
            return points;
        }
    }

}
