package com.example.config.kafka;

import com.example.core.enums.ETopic;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

@Factory
@RequiredArgsConstructor
public class KafkaConfig {

    private static final int PARTITIONS_COUNT = 1;
    private static final int REPLICA_COUNT = 1;

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;
    @Value("${kafka.consumer.group-id}")
    private String groupId;
    @Value("${kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;


    @Singleton
    @Bean
    public KafkaConsumer<String, String> kafkaConsumer() {
        return new KafkaConsumer<>(consumerProps());
    }
    private Map<String, Object> consumerProps() {
        Map<String,Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return props;
    }

    @Bean
    @Singleton
    public KafkaProducer<String, Object> kafkaProducer() {
        return new KafkaProducer<>(producerProps());
    }

    private Map<String, Object> producerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    private NewTopic buildTopic(String name){
        NewTopic topic =  new NewTopic(name, PARTITIONS_COUNT, (short) REPLICA_COUNT);
        return topic;
    }
    @Bean
    public NewTopic startTopic(){
        return buildTopic(ETopic.START.getTopic());
    }
    @Bean
    public NewTopic orchestratorTopic(){
        return buildTopic(ETopic.ORCHESTRATOR.getTopic());
    }
    @Bean
    public NewTopic notifyTopic(){
        return buildTopic(ETopic.NOTIFY.getTopic());
    }
    @Bean
    public NewTopic finish_successTopic(){
        return buildTopic(ETopic.FINISH_SUCCESS.getTopic());
    }
    @Bean
    public NewTopic finish_failTopic(){
        return buildTopic(ETopic.FINISH_FAIL.getTopic());
    }
    @Bean
    public NewTopic product_validation_successTopic(){
        return buildTopic(ETopic.PRODUCT_VALIDATION_SUCCESS.getTopic());
    }
    @Bean
    public NewTopic product_validation_failTopic(){
        return buildTopic(ETopic.PRODUCT_VALIDATION_FAIL.getTopic());
    }
    @Bean
    public NewTopic inventory_successTopic(){
        return buildTopic(ETopic.INVENTORY_SUCCESS.getTopic());
    }
    @Bean
    public NewTopic inventory_failTopic(){
        return buildTopic(ETopic.INVENTORY_FAIL.getTopic());
    }
    @Bean
    public NewTopic payment_successTopic(){
        return buildTopic(ETopic.PAYMENT_SUCCESS.getTopic());
    }
    @Bean
    public NewTopic payment_failTopic(){
        return buildTopic(ETopic.PAYMENT_FAIL.getTopic());
    }
}
