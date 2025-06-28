package com.bytes7.GameHub.service;

import com.bytes7.GameHub.exception.ResourceNotFoundException;
import com.bytes7.GameHub.model.entity.Message;
import com.bytes7.GameHub.model.entity.Tournament;
import com.bytes7.GameHub.model.entity.User;
import com.bytes7.GameHub.repository.MessageRepository;
import com.bytes7.GameHub.repository.TournamentRepository;
import com.bytes7.GameHub.security.JwtUtil;

import lombok.RequiredArgsConstructor;
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
    private final JwtUtil jwtUtil;

    public List<Message> getMessagesByTournament(UUID tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResourceNotFoundException("Torneo no encontrado");
        }
        return messageRepository.findByTournamentIdOrderByTimestampAsc(tournamentId);
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
}
