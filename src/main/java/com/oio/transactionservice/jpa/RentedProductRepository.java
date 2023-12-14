package com.oio.transactionservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentedProductRepository extends JpaRepository<RentedProductEntity, Long> {
    //대여해준 목록 조회
    List<RentedProductEntity> findRentedProductEntitiesOrderedByOwnerNickname(String nickname);

    //대여한 목록 조회
    List<RentedProductEntity> findRentedProductEntitiesOrderedByBorrowerNickname(String nickname);

    //물품 번호로 물품 조회
    RentedProductEntity findByRentedProductNo(Long rentedProductNo);

    List<RentedProductEntity> findByProductNo(Long productNo);
}
