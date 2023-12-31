package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.RentedProductDto;

import java.util.List;

public interface RentedProductService {
    //대여 시작
    RentedProductDto startRent(RentedProductDto rentedProductDto) throws Exception;

    //대여 완료
    RentedProductDto updateRentStatus(Long rentedProductNo) throws Exception;

    //대여 관련 물품 조회(status: 0 = 빌려준, 1 = 빌린)
    List<RentedProductDto> getRentedProductList(String nickname, Long status) throws Exception;

    //대여 번호로 대여 상세 조회
    RentedProductDto getRentedProduct(Long rentedProductNo) throws Exception;
}
