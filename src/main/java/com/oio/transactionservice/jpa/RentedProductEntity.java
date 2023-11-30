package com.oio.transactionservice.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oio.transactionservice.jpa.status.ReviewStatus;
import com.oio.transactionservice.jpa.status.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "rentedProduct")
@SequenceGenerator(
        name = "SEQ_GENERATOR",
        sequenceName = "RENTED_PRODUCT_SEQ",
        allocationSize = 1
)
public class RentedProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    private Long rentedProductNo;

    @Column(nullable = false)
    private Long productNo;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime rentStartDate;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime rentEndDate;

    @Column(nullable = false)
    private String ownerNickname;

    @Column(nullable = false)
    private String borrowerNickname;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @OneToMany(mappedBy = "rentedProductEntity")
    private List<ReviewEntity> reviews;

    public void setRentedProductNo(Long rentedProductNo) {
        this.rentedProductNo = rentedProductNo;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
}
