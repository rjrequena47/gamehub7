package com.bytes7.GameHub.service;

import com.bytes7.GameHub.exception.ResourceNotFoundException;
import com.bytes7.GameHub.model.entity.Match;
import com.bytes7.GameHub.model.entity.Message;
import com.bytes7.GameHub.model.entity.Tournament;
import com.bytes7.GameHub.model.entity.User;
import com.bytes7.GameHub.repository.MatchRepository;
import com.bytes7.GameHub.repository.MessageRepository;
import com.bytes7.GameHub.repository.TournamentRepository;
import com.bytes7.GameHub.repository.UserRepository;
import com.bytes7.GameHub.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public List<Message> getMessagesByTournament(UUID tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResourceNotFoundException("Torneo no encontrado");
        }
        return messageRepository.findByTournamentIdOrderByTimestampAsc(tournamentId);
    }

    public List<Message> getMessagesByMatch(UUID matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Partida no encontrada"));

        return messageRepository.findByMatch_IdOrderByTimestampAsc(matchId);
    }

    @Transactional
    public Message sendTournamentMessage(UUID tournamentId, String content, String token) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        String username = jwtUtil.getUsername(token);
        User sender = tournament.getPlayers().stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El usuario no pertenece al torneo"));

        Message message = Message.builder()
                .tournament(tournament)
                .sender(sender)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }

    @Transactional
    public Message sendMessageToMatch(UUID matchId, String content) {
        
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Partida no encontrada"));

        //User sender = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User sender = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Message message = Message.builder()
                .content(content)
                .timestamp(LocalDateTime.now())
                .sender(sender)
                .match(match)
                .build();

        return messageRepository.save(message);
    }
}
