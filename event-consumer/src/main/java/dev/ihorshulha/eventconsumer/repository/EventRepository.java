package dev.ihorshulha.eventconsumer.repository;

import dev.ihorshulha.eventconsumer.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
}