package com.bytes7.GameHub.controller;

import com.bytes7.GameHub.dto.response.MessageResponse;
import com.bytes7.GameHub.model.entity.Message;
import com.bytes7.GameHub.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Tag(name = "Torneos", description = "Gestión de torneos de GameHub7 (Mensajes)")
public class TournamentMessageController {

    private final MessageService messageService;

    @PreAuthorize("hasAnyRole('PLAYER','ADMIN')")
    @GetMapping("/{id}/messages")
    @Operation(summary = "Listar mensajes del torneo", description = "Retorna los mensajes publicados en el torneo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mensajes obtenidos exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable("id") UUID tournamentId) {
        List<Message> messages = messageService.getMessagesByTournament(tournamentId);

        List<MessageResponse> response = messages.stream()
            .map(msg -> new MessageResponse(
                msg.getId(),
                msg.getSender().getId(),
                msg.getContent(),
                msg.getTimestamp()
            )).toList();

        return ResponseEntity.ok(response);
    }
}