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

    //렌트 시작
    @PostMapping("/{productNo}/start")
    public ResponseEntity<ResponseRentedProduct> startRent(@PathVariable("productNo") Long productNo, @RequestBody RequestRentedProduct requestRentedProduct) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        requestRentedProduct.setProductNo(productNo);

        RentedProductDto rentedProductDto = rentedProductService.startRent(mapper.map(requestRentedProduct, RentedProductDto.class));

        ResponseRentedProduct responseRentedProduct = mapper.map(rentedProductDto, ResponseRentedProduct.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseRentedProduct);
    }

    //렌트 종료
    @PutMapping("/{rentedProductNo}/end")
    public ResponseEntity<ResponseRentedProduct> endRent(@PathVariable("rentedProductNo") Long rentedProductNo) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        RentedProductDto rentedProductDto = rentedProductService.endRent(rentedProductNo);

        ResponseRentedProduct responseRentedProduct = mapper.map(rentedProductDto, ResponseRentedProduct.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseRentedProduct);
    }

}
