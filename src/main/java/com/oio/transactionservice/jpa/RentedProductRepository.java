package com.oio.transactionservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RentedProductRepository extends JpaRepository<RentedProductEntity, Long> {
    //대여해준 목록 조회
    RentedProductEntity findByOwnerNickname(String ownerNickname);

    //대여한 목록 조회
    RentedProductEntity findByBorrowerNickname(String BorrowerNickname);

    //
    RentedProductEntity findByRentedProductNo(Long rentedProductNo);
}
