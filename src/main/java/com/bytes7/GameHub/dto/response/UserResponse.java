package com.bytes7.GameHub.dto.response;

import com.bytes7.GameHub.model.enums.Role;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private Role role;
    private int points;
}
