package com.payment.core.kafka;

import com.payment.core.dto.Event;
import com.payment.core.service.PaymentService;
import com.payment.core.utils.JsonUtil;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


@KafkaListener(groupId = "${kafka.consumer.group-id}")
@RequiredArgsConstructor
public class Consumer {

    private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);

    @Inject
    private PaymentService paymentService;
    @Inject
    private JsonUtil jsonUtil;

    @Topic("${kafka.topic.payment-success}")
    public void consumerPaymentSuccessEvent(String payload, @KafkaKey String key) {
        if (Objects.equals(key, "1")) {
            LOG.info("Receiving success event {} from payment-success topic", payload);
            Event event = jsonUtil.toEvent(payload);
            paymentService.realizedPayment(event);
        }
    }
    @Topic("${kafka.topic.payment-fail}")
    public void consumerPaymentFailEvent (String payload, @KafkaKey String key){
        if (Objects.equals(key, "1")) {
            LOG.info("Receiving rollback event {} from payment-fail topic", payload);
            Event event = jsonUtil.toEvent(payload);
            paymentService.realizedRefund(event);
        }
    }
}


