package com.oio.transactionservice.dto;

import com.oio.transactionservice.jpa.RentedProductEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private Long rentedProductNo;

    private Long productNo;

    private String writerNickname;

    private String receiverNickname;

    private String content;

    private Long heart;

    private LocalDateTime postDate;
}
