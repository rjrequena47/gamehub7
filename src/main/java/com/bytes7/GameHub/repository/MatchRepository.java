package com.bytes7.GameHub.repository;

import com.bytes7.GameHub.model.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

    List<Match> findByTournamentId(UUID tournamentId);
}
