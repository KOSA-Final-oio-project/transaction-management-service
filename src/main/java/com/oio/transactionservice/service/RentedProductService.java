package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.RentedProductDto;

public interface RentedProductService {
    //대여 시작
    RentedProductDto startRent(RentedProductDto rentedProductDto);

    //대여 종료
    RentedProductDto endRent(Long rentedProductNo);

    //대여해준 목록 조회
    RentedProductDto getRentedProductByUserEmail(String email);

    //대여한 목록 조회
    RentedProductDto getBorrowedProductByUserEmail(String email);

}
