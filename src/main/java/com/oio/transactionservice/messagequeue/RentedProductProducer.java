package com.oio.transactionservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oio.transactionservice.dto.RentedProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentedProductProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public RentedProductDto send(String topic, RentedProductDto rentedProductDto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(rentedProductDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        System.out.println("JSON" + jsonInString);
        System.out.println("반환값" + rentedProductDto);
        log.info("RentedProduct Producer sent data from the RentedProduct microservice : " + rentedProductDto);

        return rentedProductDto;
    }

}