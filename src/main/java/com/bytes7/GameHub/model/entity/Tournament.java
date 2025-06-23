package com.bytes7.GameHub.model.entity;

import com.bytes7.GameHub.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "tournaments")
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tournament_id", unique = true)
    private UUID id;

    @Column(name = "tournament_name", nullable = false)
    private String name;

    @Column(name = "tournament_max_players")
    private int maxPlayers;

    @Enumerated(EnumType.STRING)
    @Column(name = "tournament_status")
    private Status status;

    @ManyToMany
    @JoinTable(
        name = "tournament_players",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players = new ArrayList<>();
}

