package com.oio.transactionservice.kafka.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oio.transactionservice.jpa.status.ReviewStatus;
import com.oio.transactionservice.jpa.status.Status;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
public class Payload {
    private Long productNo;

    private Long rentedProductNo;

    private LocalDateTime rentStartDate;

    private LocalDateTime rentEndDate;

    private String ownerNickname;

    private String borrowerNickname;

    private Status status;

    private ReviewStatus reviewStatus;

}
