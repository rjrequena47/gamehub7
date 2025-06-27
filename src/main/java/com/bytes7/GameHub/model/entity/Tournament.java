package com.bytes7.GameHub.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bytes7.GameHub.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany
    @JoinColumn(name = "tournament_id")
    private List<Message> messages = new ArrayList<>();
}