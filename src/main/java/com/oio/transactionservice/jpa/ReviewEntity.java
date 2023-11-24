package com.oio.transactionservice.jpa;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

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
    private String writerId;

    @Column(nullable = false)
    private String receiverId;

    @ColumnDefault(value = "SYSDATE")
    @Column(nullable = false)
    private Date postDate;

    @ManyToOne
    @JoinColumn(name = "rentedProductNo", nullable = false)
    private RentedProductEntity rentedProductEntity;

}
