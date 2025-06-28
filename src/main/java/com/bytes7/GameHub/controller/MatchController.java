package com.bytes7.GameHub.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

import com.bytes7.GameHub.dto.request.UpdateResultRequest;
import com.bytes7.GameHub.dto.response.MatchResponse;
import com.bytes7.GameHub.model.entity.Match;
import com.bytes7.GameHub.service.MatchService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "Partidos", description = "Gestión de emparejamientos y resultados")
public class MatchController {

    private final MatchService matchService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{tournamentId}/pair")
    public ResponseEntity<Match> createMatch(@PathVariable UUID tournamentId, @RequestParam UUID player1Id, @RequestParam UUID player2Id) {
        return ResponseEntity.ok(matchService.createMatch(tournamentId, player1Id, player2Id));
    }

    @Operation(summary = "Actualizar resultado de un partido", 
           description = "Actualiza el resultado de una partida. Solo accesible por ADMIN o el sistema interno.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MatchResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Partido no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/result")
    public ResponseEntity<MatchResponse> updateMatchResult(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateResultRequest request) {

        Match updatedMatch = matchService.updateMatchResult(id, request.result());

        MatchResponse response = MatchResponse.builder()
                .id(updatedMatch.getId())
                .tournamentId(updatedMatch.getTournament().getId())
                .player1Id(updatedMatch.getPlayer1().getId())
                .player2Id(updatedMatch.getPlayer2() != null ? updatedMatch.getPlayer2().getId() : null)
                .status(updatedMatch.getStatus())
                .result(updatedMatch.getResult())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<Match>> getMatches(@PathVariable UUID tournamentId) {
        return ResponseEntity.ok(matchService.getMatchesByTournament(tournamentId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/generate/{tournamentId}")
    public ResponseEntity<List<Match>> generateMatchups(@PathVariable UUID tournamentId) {
        return ResponseEntity.ok(matchService.generateMatchups(tournamentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable UUID id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

}
