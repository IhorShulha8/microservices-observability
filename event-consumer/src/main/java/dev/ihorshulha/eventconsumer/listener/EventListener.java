package dev.ihorshulha.eventconsumer.listener;

import dev.ihorshulha.avro.Event;
import dev.ihorshulha.eventconsumer.repository.EventRepository;
import dev.ihorshulha.eventconsumer.model.EventEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private final EventRepository repository;

    public EventListener(EventRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "events", groupId = "event-group")
    public void listen(Event event) {
        EventEntity entity = new EventEntity();
        entity.setUid(event.getUid());
        entity.setSubject(event.getSubject());
        entity.setDescription(event.getDescription());
        repository.save(entity);
        System.out.println("Saved: " + entity);
    }
}