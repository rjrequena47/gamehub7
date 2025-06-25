package com.bytes7.GameHub.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

import com.bytes7.GameHub.dto.request.UpdateResultRequest;
import com.bytes7.GameHub.model.entity.Match;
import com.bytes7.GameHub.service.MatchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{tournamentId}/pair")
    public ResponseEntity<Match> createMatch(@PathVariable UUID tournamentId, @RequestParam UUID player1Id, @RequestParam UUID player2Id) {
        return ResponseEntity.ok(matchService.createMatch(tournamentId, player1Id, player2Id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/result")
    public ResponseEntity<Void> updateMatchResult(@PathVariable UUID id, @RequestBody @Valid UpdateResultRequest request) {
        matchService.updateMatchResult(id, request.result());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<Match>> getMatches(@PathVariable UUID tournamentId) {
        return ResponseEntity.ok(matchService.getMatchesByTournament(tournamentId));
    }
}
