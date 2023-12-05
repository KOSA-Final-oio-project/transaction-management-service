package com.oio.transactionservice.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class KafkaRentedProductDto implements Serializable {
    private Schema schema;
    private Payload payload;

}
