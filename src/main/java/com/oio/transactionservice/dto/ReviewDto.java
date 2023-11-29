package com.oio.transactionservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private Long rentedProductNo;

    private String writerNickname;

    private String receiverNickname;

    private String content;

    private Long heart;

    private LocalDateTime postDate;

}
