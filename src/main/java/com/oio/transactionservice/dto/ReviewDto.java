package com.oio.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private Long rentedProductNo;

    private String writerNickname;

    private String receiverNickname;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime postDate;

    private Long heart;

}
