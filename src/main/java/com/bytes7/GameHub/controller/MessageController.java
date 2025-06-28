package com.bytes7.GameHub.controller;

import com.bytes7.GameHub.dto.request.SendMessageRequest;
import com.bytes7.GameHub.model.entity.Message;
import com.bytes7.GameHub.service.MessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments/{tournamentId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    @PostMapping
    @Operation(summary = "Enviar mensaje al torneo", description = "Permite a jugadores o administradores enviar un mensaje al torneo")
    public ResponseEntity<Message> sendMessage(
            @PathVariable UUID tournamentId,
            @RequestBody @Valid SendMessageRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Message savedMessage = messageService.sendTournamentMessage(tournamentId, request.getContent(), token);

        return ResponseEntity.ok(savedMessage);
    }
}
