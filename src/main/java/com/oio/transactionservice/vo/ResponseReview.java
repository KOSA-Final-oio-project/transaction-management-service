package com.oio.transactionservice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseReview {
    private String writerNickname;

    private String receiverNickname;

    private String content;
    
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime postDate;
}
