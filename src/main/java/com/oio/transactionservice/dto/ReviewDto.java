package com.oio.transactionservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewDto {
    private String writerId;

    private String receiverId;

    private String content;

    private Long heart;

    private Date postDate;
}
