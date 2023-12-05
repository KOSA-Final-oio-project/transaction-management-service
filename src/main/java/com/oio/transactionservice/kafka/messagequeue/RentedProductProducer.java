package com.oio.transactionservice.kafka.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oio.transactionservice.config.ModelMapperConfig;
import com.oio.transactionservice.dto.RentedProductDto;
import com.oio.transactionservice.kafka.dto.Field;
import com.oio.transactionservice.kafka.dto.KafkaRentedProductDto;
import com.oio.transactionservice.kafka.dto.Payload;
import com.oio.transactionservice.kafka.dto.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentedProductProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fields = Arrays.asList(
            new Field("Long", true, "productNo"),
            new Field("Long", true, "rentedProductNo"),
            new Field("LocalDateTime", true, "rentStartDate"),
            new Field("LocalDateTime", true, "rentEndDate"),
            new Field("String", true, "ownerNickname"),
            new Field("String", true, "borrowerNickname"),
            new Field("Status", true, "status"),
            new Field("ReviewStatus", true, "reviewStatus"))
            ;

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("rentedproduct")
            .build();

    public RentedProductDto send(String topic, RentedProductDto rentedProductDto) {
        Payload payload = Payload.builder()
                .productNo(rentedProductDto.getProductNo())
                .rentedProductNo(rentedProductDto.getRentedProductNo())
                .rentStartDate(rentedProductDto.getRentStartDate())
                .rentEndDate(rentedProductDto.getRentEndDate())
                .ownerNickname(rentedProductDto.getOwnerNickname())
                .borrowerNickname(rentedProductDto.getBorrowerNickname())
                .status(rentedProductDto.getStatus())
                .reviewStatus(rentedProductDto.getReviewStatus())
                .build();

        KafkaRentedProductDto kafkaRentedProductDto = new KafkaRentedProductDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaRentedProductDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);

        log.info("Kafka Producer sent data from the Order microservice : " + kafkaRentedProductDto);

        return rentedProductDto;
    }

}