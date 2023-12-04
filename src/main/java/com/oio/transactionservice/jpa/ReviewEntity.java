package com.oio.transactionservice.jpa;

import com.oio.transactionservice.jpa.status.ReviewStatus;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "review")
@SequenceGenerator(
        name = "SEQ_GENERATOR",
        sequenceName = "REVIEW_SEQ",
        allocationSize = 1
)
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    private Long reviewNo;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long heart;

    @Column(nullable = false)
    private String writerNickname;

    @Column(nullable = false)
    private String receiverNickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewWriter;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime postDate;

    @ManyToOne
    @JoinColumn(name = "rentedProductNo", nullable = false)
    private RentedProductEntity rentedProductEntity;

    public void setRentedProductEntity(RentedProductEntity rentedProductEntity) {
        this.rentedProductEntity = rentedProductEntity;
    }

    public void setReviewWriter(ReviewStatus reviewWriter) {
        this.reviewWriter = reviewWriter;
    }

    public void setHeart(Long heart) {
        this.heart = heart;
    }

}