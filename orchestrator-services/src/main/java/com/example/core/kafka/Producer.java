package com.example.core.kafka;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@AllArgsConstructor
public class Producer {

    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);

    @Inject
    private KafkaProducer kafkaProducer;

    public void sendEvent(String payload, String topic){
        try {
            LOG.info("Sending event to topic {} with data {}", topic, payload);
            String key = "1";
            ProducerRecord<Object, String> record = new ProducerRecord<>(topic,key, payload);
            kafkaProducer.send(record);
        } catch (Exception e) {
            LOG.error("Error trying to send data to topic {} with data {}", topic, payload, e);
        }
    }
    public void sendEventProduct(String payload, String topic){
        try {
            LOG.info("Sending event product to topic {} with data {}", topic, payload);
            String key = "2";
            ProducerRecord<Object, String> record = new ProducerRecord<>(topic,key, payload);
            kafkaProducer.send(record);
        } catch (Exception e) {
            LOG.error("Error trying to send data to topic {} with data {}", topic, payload, e);
        }
    }
}
