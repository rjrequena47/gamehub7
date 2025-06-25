package com.bytes7.GameHub.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bytes7.GameHub.dto.request.CreateTournamentRequest;
import com.bytes7.GameHub.dto.response.TournamentResponse;
import com.bytes7.GameHub.dto.response.UserRankingDetails;
import com.bytes7.GameHub.dto.response.UserStats;
import com.bytes7.GameHub.model.entity.Match;
import com.bytes7.GameHub.model.entity.Tournament;
import com.bytes7.GameHub.model.entity.User;
import com.bytes7.GameHub.model.enums.Result;
import com.bytes7.GameHub.model.enums.Status;
import com.bytes7.GameHub.repository.TournamentRepository;
import com.bytes7.GameHub.repository.UserRepository;

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

    public List<UserRankingDetails> getRanking(UUID tournamentID) {
        // Obtener el torneo de la base de datos en tipo Optional
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentID);

        // Verificar que el torneo exista, si no existe lanzar excepcion
        if (tournament.isEmpty()) {
            throw new RuntimeException("No existe el torneo con la ID introducida.");
        }

        // Obtenemios la lista de usuarios
        List<User> users = tournament.get().getPlayers();
        
        // Mas a menos
        users.sort(Comparator.comparing(User::getPoints).reversed());

        // Crear DTO que tenga atributo nombre y puntos list<EseEjemplo>
        List<UserRankingDetails> userRanking = new ArrayList<>();
        
        // Recorrer la lista de usuarios ordenados y añadirlos a userRanking

        for (User user : users) {
            UserRankingDetails userRankingDetails = new UserRankingDetails().builder()
                .username(user.getUsername())
                .points(user.getPoints())
                .build();
            userRanking.add(userRankingDetails);
        }

        // Devolvemos la lista de información de users
        return userRanking;
    }

    public List<UserStats> getStats(UUID tournamentID) {
        // Obtenemos el torneo de la base de datos
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentID);
        
        // Verificamos que el torneo exista, si no existe lanzamos una excepción
        if (tournament.isEmpty()) {
            throw new RuntimeException("No existe el torneo con la ID introducida.");
        }

        // Obtenemos los usuarios que pertenecen al torneo
        List<User> users = tournament.get().getPlayers();
    
        // Creamos una lista para almacenar las estadísticas de los usuarios
        List<UserStats> userStatsList = new ArrayList<>();
        // Obtenemos los matches de cada usuario
        for (User user : users) {
            List<Match> userMatches = user.getAllMatches();
            
            Integer wins = 0;
            Integer loses = 0;

            for (Match match : userMatches) {
                User player1 = match.getPlayer1();
                User player2 = match.getPlayer2();

                Result result = match.getResult();
                if (result == Result.PLAYER1_WON) {
                    if (player1.getId() == user.getId()) wins++;
                } else if (result == Result.PLAYER2_WON) {
                    if (player2.getId() == user.getId()) wins++;
                } else if (result != Result.PENDING) {
                    loses++;
                }
            }
            
            UserStats userStats = new UserStats().builder()
                .username(user.getUsername())
                .points(user.getPoints())
                .wonMatches(wins)
                .lostMatches(loses)
                .build();

            userStatsList.add(userStats);
        }
        
        // Ordenamos la lista de estadísticas de usuarios por victorias
        userStatsList.sort(Comparator.comparing(UserStats::getWonMatches).reversed());
        
        // Devolvemos la lista ordenada
        return userStatsList;
    }
}
