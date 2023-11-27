package com.oio.transactionservice.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestReview {
    @NotNull(message = "작성된 내용이 존재하지 않습니다.")
    private String content;
}
