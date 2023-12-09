package com.oio.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oio.transactionservice.jpa.status.ReviewStatus;
import com.oio.transactionservice.jpa.status.Status;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class RentedProductDto {
    private Long productNo;

    @NotEmpty(message = "대여 시작 날짜를 설정해주세요.")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime rentStartDate;

    @NotEmpty(message = "대여 종료 날짜를 설정해주세요.")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime rentEndDate;

    private String ownerNickname;

    private String borrowerNickname;

    private Status status;

    private ReviewStatus reviewStatus;
}
