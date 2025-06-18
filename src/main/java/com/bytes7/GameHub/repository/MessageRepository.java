package com.bytes7.GameHub.repository;

import com.bytes7.GameHub.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
