package com.bytes7.GameHub.model.entity;

import com.bytes7.GameHub.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "tournaments")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Tournament {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    private int maxPlayers;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany
    @JoinTable(
        name = "tournament_players",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players = new ArrayList<>();
}

