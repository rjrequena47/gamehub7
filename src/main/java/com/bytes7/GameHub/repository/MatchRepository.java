package com.bytes7.GameHub.repository;

import com.bytes7.GameHub.model.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {
}

