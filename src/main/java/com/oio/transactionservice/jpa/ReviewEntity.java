package com.oio.transactionservice.jpa;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

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

    private Long heart;

    @Column(nullable = false)
    private String writerNickname;

    @Column(nullable = false)
    private String receiverNickname;

    @ColumnDefault(value = "SYSDATE")
    @Column(nullable = false)
    private LocalDateTime postDate;

    @ManyToOne
    @JoinColumn(name = "rentedProductNo", nullable = false)
    private RentedProductEntity rentedProductEntity;

    public void setRentedProductNo(RentedProductEntity rentedProductEntity) {
        this.rentedProductEntity = rentedProductEntity;
    }

}