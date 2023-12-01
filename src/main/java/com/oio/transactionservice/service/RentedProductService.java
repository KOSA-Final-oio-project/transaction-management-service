package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.RentedProductDto;

import java.util.List;

public interface RentedProductService {
    //대여 시작
    RentedProductDto startRent(RentedProductDto rentedProductDto);

    void updateRentStatus(Long rentedProductNo);

    //대여 관련 물품 조회(status: 0 = 빌려준, 1 = 빌린)
    List<RentedProductDto> getRentedProduct(String nickname, Long status);

}
