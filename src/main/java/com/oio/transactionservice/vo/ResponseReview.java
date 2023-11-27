package com.oio.transactionservice.vo;

import lombok.Data;

@Data
public class ResponseReview {
    private String writerNickname;
    private String receiverNickname;
    private String content;
}
