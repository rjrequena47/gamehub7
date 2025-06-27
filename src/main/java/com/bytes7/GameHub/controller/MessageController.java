package com.bytes7.GameHub.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bytes7.GameHub.dto.request.MessageRequest;
import com.bytes7.GameHub.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Mensajes", description = "Operaciones relacionadas a los mensajes")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/tournaments/{id}")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    @Operation(summary = "Obtener mensajes de un torneo", description = "Muestra todos los mensajes de un torneo")
    public ResponseEntity<?> getTournamentMessages(@PathVariable UUID id, @RequestParam(required = false) Boolean fancyStyle) {
        if (Boolean.TRUE.equals(fancyStyle)) {
            return ResponseEntity.ok(messageService.getTournamentMessages(id).toString());
        } else {
            return ResponseEntity.ok(messageService.getTournamentMessages(id));
        }
    }

    @PostMapping("/tournaments")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    @Operation(summary = "Enviar mensaje a un torneo", description = "Envía un mensaje a un torneo")
    public ResponseEntity<?> postMessageTournament(@RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.postMessageTournament(request));
    }

    @GetMapping("/matches/{id}")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    @Operation(summary = "Obtener mensajes de un partido", description = "Muestra todos los mensajes de un partido")
    public ResponseEntity<?> getMatchMessages(@PathVariable UUID id, @RequestParam(required = false) Boolean fancyStyle) {
        if (Boolean.TRUE.equals(fancyStyle)) {
            return ResponseEntity.ok(messageService.getMatchMessages(id).toString());
        } else {
            return ResponseEntity.ok(messageService.getMatchMessages(id));
        }
    }

    @PostMapping("/matches")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    @Operation(summary = "Enviar mensaje a un partido", description = "Envía un mensaje a un partido")
    public ResponseEntity<?> postMessageMatch(@RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.postMessageMatch(request));
    }
} 
