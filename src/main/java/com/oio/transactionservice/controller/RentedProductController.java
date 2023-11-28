package com.oio.transactionservice.controller;

import com.oio.transactionservice.dto.RentedProductDto;
import com.oio.transactionservice.service.RentedProductService;
import com.oio.transactionservice.vo.RequestRentedProduct;
import com.oio.transactionservice.vo.ResponseRentedProduct;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class RentedProductController {
    private RentedProductService rentedProductService;
    ModelMapper mapper = new ModelMapper();

    public RentedProductController(RentedProductService rentedProductService) {
        this.rentedProductService = rentedProductService;
    }

    //대여 시작
    @PostMapping("/{productNo}/start")
    public ResponseEntity<ResponseRentedProduct> startRent(@PathVariable("productNo") Long productNo, @RequestBody RequestRentedProduct requestRentedProduct) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        requestRentedProduct.setProductNo(productNo);

        RentedProductDto rentedProductDto = rentedProductService.startRent(mapper.map(requestRentedProduct, RentedProductDto.class));

        ResponseRentedProduct responseRentedProduct = mapper.map(rentedProductDto, ResponseRentedProduct.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseRentedProduct);
    }

    //대여 종료
    @PutMapping("/{rentedProductNo}/end")
    public ResponseEntity<ResponseRentedProduct> endRent(@PathVariable("rentedProductNo") Long rentedProductNo) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        RentedProductDto rentedProductDto = rentedProductService.endRent(rentedProductNo);

        ResponseRentedProduct responseRentedProduct = mapper.map(rentedProductDto, ResponseRentedProduct.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseRentedProduct);
    }

    //대여 물품 삭제(상태값 대여종료로 바뀜)
    @PutMapping("/{rentedProductNo}/delete")
    public ResponseEntity<ResponseRentedProduct> deleteRent(@PathVariable("rentedProductNo") Long rentedProductNo) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        RentedProductDto rentedProductDto = rentedProductService.deleteRent(rentedProductNo);

        ResponseRentedProduct responseRentedProduct = mapper.map(rentedProductDto, ResponseRentedProduct.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseRentedProduct);
    }

}
