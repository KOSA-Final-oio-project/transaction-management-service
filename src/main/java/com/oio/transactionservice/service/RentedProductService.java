package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.RentedProductDto;

import java.util.List;

public interface RentedProductService {
    //대여 시작
    RentedProductDto startRent(RentedProductDto rentedProductDto);

    //대여 종료
    RentedProductDto endRent(Long rentedProductNo);

    //대여 물품 삭제(상태값만 대여종료로 바뀜)
    RentedProductDto deleteRent(Long rentedProductNo);

    //대여 관련 목록 조회
    List<RentedProductDto> getRentedProduct(String nickname, Long status);

    //대여한 목록 조회
//    RentedProductDto getBorrowedProduct(String nickname);

}
