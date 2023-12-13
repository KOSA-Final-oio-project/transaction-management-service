package com.oio.transactionservice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class RequestReview {
    private Long rentedProductNo;

    private Long productNo;

    private String writerNickname;

    private String receiverNickname;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime postDate;

    @NotNull(message = "작성된 내용이 존재하지 않습니다.")
    private String content;

    private Long heart;
}
