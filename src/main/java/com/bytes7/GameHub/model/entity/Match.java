package com.bytes7.GameHub.model.entity;

import com.bytes7.GameHub.model.enums.Result;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import com.bytes7.GameHub.model.enums.MatchStatus;

@Entity
@Table(name = "matches")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "player1_id")
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private User player2;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Enumerated(EnumType.STRING)
    private Result result;

    private int round;
}

