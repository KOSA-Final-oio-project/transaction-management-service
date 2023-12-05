package com.oio.transactionservice.kafka.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oio.transactionservice.dto.RentedProductDto;
import com.oio.transactionservice.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ReviewDto sendKafkaReview(String topic, ReviewDto reviewDto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(reviewDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);

        log.info("Kafka Producer sent data from the Review microservice : " + reviewDto);

        return reviewDto;
    }

    public RentedProductDto sendKafkaRentedProduct(String topic, RentedProductDto rentedProductDto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(rentedProductDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);

        log.info("Kafka Producer sent data from the RentedProduct microservice : " + rentedProductDto);

        return rentedProductDto;
    }

}