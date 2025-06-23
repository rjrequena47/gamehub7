package com.bytes7.GameHub.model.entity;

import com.bytes7.GameHub.model.enums.Result;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)@Column(name = "match_id", unique = true)
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
    @Column(name = "result", unique = true)
    private Result result;

    @Column(name = "round")
    private int round;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();
}

