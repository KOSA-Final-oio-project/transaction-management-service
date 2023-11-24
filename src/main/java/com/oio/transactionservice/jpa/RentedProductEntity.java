package com.oio.transactionservice.jpa;

import com.oio.transactionservice.jpa.status.ReviewStatus;
import com.oio.transactionservice.jpa.status.Status;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;
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
    private Date rentStartDate;

    @Column(nullable = false)
    private Date rentEndDate;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String borrowerId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @OneToMany(mappedBy = "rentedProductEntity")
    private List<ReviewEntity> reviews;

}
