package com.oio.transactionservice.controller;

import com.oio.transactionservice.config.ModelMapperConfig;
import com.oio.transactionservice.dto.RentedProductDto;
import com.oio.transactionservice.service.RentedProductService;
import com.oio.transactionservice.vo.RequestRentedProduct;
import com.oio.transactionservice.vo.ResponseRentedProduct;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rent")
public class RentedProductController {
    private RentedProductService rentedProductService;
    private ModelMapper mapper;

    public RentedProductController(RentedProductService rentedProductService) {
        this.rentedProductService = rentedProductService;
        this.mapper = ModelMapperConfig.modelMapper();
    }

    //대여 시작
    @PostMapping("/{productNo}")
    public ResponseEntity<ResponseRentedProduct> startRent(@PathVariable("productNo") Long productNo, @RequestBody RequestRentedProduct requestRentedProduct) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        requestRentedProduct.setProductNo(productNo);

        RentedProductDto rentedProductDto = rentedProductService.startRent(mapper.map(requestRentedProduct, RentedProductDto.class));

        ResponseRentedProduct responseRentedProduct = mapper.map(rentedProductDto, ResponseRentedProduct.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseRentedProduct);
    }

    //대여 완료
    @PutMapping("/{rentedProductNo}")
    public void updateRentStatus(@PathVariable("rentedProductNo") Long rentedProductNo) {
        rentedProductService.updateRentStatus(rentedProductNo);
    }

    //대여 관련 물품 조회(status: 0 = 빌려준, 1 = 빌린)
    @GetMapping("/{status}")
    public ResponseEntity<List<ResponseRentedProduct>> getRentedProduct(@RequestParam String nickname, @PathVariable Long status) {
        List<RentedProductDto> rentedProductDto = rentedProductService.getRentedProduct(nickname, status);
        List<ResponseRentedProduct> responseRentedProduct = new ArrayList<>();

        for (RentedProductDto dto : rentedProductDto) {
            ResponseRentedProduct response = mapper.map(dto, ResponseRentedProduct.class);
            responseRentedProduct.add(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseRentedProduct);
    }
}
