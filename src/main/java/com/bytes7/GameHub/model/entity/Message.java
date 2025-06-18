package com.bytes7.GameHub.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private String content;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = true)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = true)
    private Tournament tournament;
}
