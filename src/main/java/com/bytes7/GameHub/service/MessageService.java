package com.bytes7.GameHub.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bytes7.GameHub.dto.request.MessageRequest;
import com.bytes7.GameHub.dto.response.MessageResponse;
import com.bytes7.GameHub.model.entity.Match;
import com.bytes7.GameHub.model.entity.Message;
import com.bytes7.GameHub.model.entity.Tournament;
import com.bytes7.GameHub.model.entity.User;
import com.bytes7.GameHub.repository.MatchRepository;
import com.bytes7.GameHub.repository.MessageRepository;
import com.bytes7.GameHub.repository.TournamentRepository;
import com.bytes7.GameHub.repository.UserRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    public MessageService(MessageRepository messageRepository, TournamentRepository tournamentRepository,
            UserRepository userRepository, MatchRepository matchRepository) {
        this.messageRepository = messageRepository;
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    public List<MessageResponse> getTournamentMessages(UUID tournamentID) {
        Tournament tournament = tournamentRepository.findById(tournamentID)
                .orElseThrow(() -> new IllegalArgumentException("El torneo no existe"));

        List<MessageResponse> messageDTO = new ArrayList<>();
        for (Message message : tournament.getMessages()) {
            messageDTO.add(MessageResponse.builder()
                    .sender(message.getSender().getUsername())
                    .content(message.getContent())
                    .timestamp(message.getTimestamp())
                    .match(message.getMatch() != null ? message.getMatch().getId() : null)
                    .tournament(message.getTournament().getId())
                    .build());
        }

        return messageDTO;
    }

    public MessageResponse postMessageTournament(MessageRequest request) {
        if (messageRepository.findById(request.getId()).isPresent()) {
            throw new IllegalArgumentException("El mensaje ya existe");
        }

        User sender = userRepository.findByUsername(request.getSender())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Tournament tournament = tournamentRepository.findByName(request.getTournamentName())
                .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado"));

        Message message = Message.builder()
                .id(request.getId())
                .sender(sender)
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .match(null)
                .tournament(tournament)
                .build();

        tournament.getMessages().add(message);
        messageRepository.save(message);
        tournamentRepository.save(tournament);

        return MessageResponse.builder()
                .sender(sender.getUsername())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .match(null)
                .tournament(tournament.getId())
                .build();
    }

    public List<MessageResponse> getMatchMessages(UUID matchID) {
        Match match = matchRepository.findById(matchID)
                .orElseThrow(() -> new IllegalArgumentException("El partido no existe"));

        List<MessageResponse> messageDTO = new ArrayList<>();
        for (Message message : match.getMessages()) {
            messageDTO.add(MessageResponse.builder()
                    .sender(message.getSender().getUsername())
                    .content(message.getContent())
                    .timestamp(message.getTimestamp())
                    .match(match.getId())
                    .tournament(message.getTournament() != null ? message.getTournament().getId() : null)
                    .build());
        }

        return messageDTO;
    }

    public MessageResponse postMessageMatch(MessageRequest request) {
        if (messageRepository.findById(request.getId()).isPresent()) {
            throw new IllegalArgumentException("El mensaje ya existe");
        }

        User sender = userRepository.findByUsername(request.getSender())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Match match = matchRepository.findById(request.getMatchID())
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));

        Message message = Message.builder()
                .id(request.getId())
                .sender(sender)
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .match(match)
                .tournament(match.getTournament())
                .build();

        match.getMessages().add(message);
        messageRepository.save(message);
        matchRepository.save(match);

        return MessageResponse.builder()
                .sender(sender.getUsername())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .match(match.getId())
                .tournament(match.getTournament() != null ? match.getTournament().getId() : null)
                .build();
    }

}