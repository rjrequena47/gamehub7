package com.bytes7.GameHub.repository;

import com.bytes7.GameHub.model.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TournamentRepository extends JpaRepository<Tournament, UUID> {
}
