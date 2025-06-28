package com.bytes7.GameHub.controller;

import com.bytes7.GameHub.dto.request.CreateTournamentRequest;
import com.bytes7.GameHub.dto.response.TournamentResponse;
import com.bytes7.GameHub.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments")
@Tag(name = "Torneos", description = "Gestión de torneos de GameHub7")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nuevo torneo", description = "Solo el ADMIN puede crear torneos")
    public ResponseEntity<TournamentResponse> createTournament(@RequestBody CreateTournamentRequest request) {
        return ResponseEntity.ok(tournamentService.createTournament(request));
    }

    @GetMapping
    @Operation(summary = "Listar torneos", description = "Obtiene la lista de todos los torneos disponibles. (Acceso público).")
    public ResponseEntity<List<TournamentResponse>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de torneo", description = "Obtiene la información de un torneo específico. (Acceso público).")
    public ResponseEntity<TournamentResponse> getTournamentById(@PathVariable UUID id) {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }

    @PreAuthorize("hasRole('PLAYER')")
    @PostMapping("/{id}/join")
    @Operation(summary = "Unirse a un torneo", description = "Permite a un jugador unirse a un torneo si hay cupo disponible")
    public ResponseEntity<TournamentResponse> joinTournament(@PathVariable UUID id) {
        return ResponseEntity.ok(tournamentService.joinTournament(id));
    }
}
