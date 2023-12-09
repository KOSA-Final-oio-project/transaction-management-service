package com.oio.transactionservice.controller;

import com.oio.transactionservice.config.ModelMapperConfig;
import com.oio.transactionservice.dto.RentedProductDto;
import com.oio.transactionservice.messagequeue.RentedProductProducer;
import com.oio.transactionservice.service.RentedProductService;
import com.oio.transactionservice.vo.RequestRentedProduct;
import com.oio.transactionservice.vo.ResponseRentedProduct;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rent")
public class RentedProductController {
    private RentedProductService rentedProductService;
    private ModelMapper mapper;
    private RentedProductProducer rentedProductProducer;

    Map map = new HashMap();

    public RentedProductController(RentedProductService rentedProductService, RentedProductProducer rentedProductProducer) {
        this.rentedProductService = rentedProductService;
        this.mapper = ModelMapperConfig.modelMapper();
        this.rentedProductProducer = rentedProductProducer;
    }

    //대여 시작
    @PostMapping("/{productNo}")
    public ResponseEntity<Map<String, String>> startRent(@PathVariable("productNo") Long productNo, @RequestBody RequestRentedProduct requestRentedProduct) throws Exception {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        requestRentedProduct.setProductNo(productNo);

        RentedProductDto rentedProductDto = rentedProductService.startRent(mapper.map(requestRentedProduct, RentedProductDto.class));
        if (rentedProductDto != null) {
//            rentedProductProducer.send("RENTED_PRODUCT", rentedProductDto);
            map.put("msg", "success");
        } else {
            map.put("msg", "fail");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    //대여 완료
    @PutMapping("/{rentedProductNo}")
    public ResponseEntity<Map<String, String>> updateRentStatus(@PathVariable("rentedProductNo") Long rentedProductNo) throws Exception {
        RentedProductDto rentedProductDto = rentedProductService.updateRentStatus(rentedProductNo);
        if (rentedProductDto != null) {
//            rentedProductProducer.send("RENTED_PRODUCT", rentedProductDto);
            map.put("msg", "success");
        } else {
            map.put("msg", "fail");
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    //대여 관련 물품 조회(status: 0 = 빌려준, 1 = 빌린)
    @GetMapping("/{status}")
    public ResponseEntity<List<ResponseRentedProduct>> getRentedProduct(@RequestParam String nickname, @PathVariable Long status) throws Exception {
        List<RentedProductDto> rentedProductDto = rentedProductService.getRentedProduct(nickname, status);

        List<ResponseRentedProduct> responseRentedProduct = new ArrayList<>();

        for (RentedProductDto dto : rentedProductDto) {
            ResponseRentedProduct response = mapper.map(dto, ResponseRentedProduct.class);
            responseRentedProduct.add(response);
        }

        if (responseRentedProduct.size() == 0) {
            throw new NoSuchElementException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseRentedProduct);
    }
}
