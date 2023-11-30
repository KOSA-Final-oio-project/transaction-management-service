package com.oio.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oio.transactionservice.jpa.status.ReviewStatus;
import com.oio.transactionservice.jpa.status.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentedProductDto {
    private Long productNo;

    private Long rentedProductNo;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime rentStartDate;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime rentEndDate;

    private String ownerNickname;

    private String borrowerNickname;

    private Status status;

    private ReviewStatus reviewStatus;
}
