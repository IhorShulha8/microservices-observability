package dev.ihorshulha.eventconsumer.listener;

import dev.ihorshulha.avro.Event;
import dev.ihorshulha.eventconsumer.repository.EventRepository;
import dev.ihorshulha.eventconsumer.model.EventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    private final EventRepository repository;

    public EventListener(EventRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "events", groupId = "event-group")
    public void listen(Event event) {
        try {
            // MDC fields for structured logs
            MDC.put("uid", event.getUid());
            MDC.put("subject", event.getSubject());
            MDC.put("description", event.getDescription());

            log.info("Received event");

            EventEntity entity = new EventEntity();
            entity.setUid(event.getUid());
            entity.setSubject(event.getSubject());
            entity.setDescription(event.getDescription());

            repository.save(entity);

            log.info("Saved entity");
        } finally {
            MDC.clear(); // clear MDC after logging
        }
    }
}