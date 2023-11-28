package com.oio.transactionservice.controller;

import com.oio.transactionservice.dto.RentedProductDto;
import com.oio.transactionservice.dto.ReviewDto;
import com.oio.transactionservice.service.ReviewService;
import com.oio.transactionservice.vo.RequestRentedProduct;
import com.oio.transactionservice.vo.RequestReview;
import com.oio.transactionservice.vo.ResponseRentedProduct;
import com.oio.transactionservice.vo.ResponseReview;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ReviewController {
    private ReviewService reviewService;
    ModelMapper mapper = new ModelMapper();

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{rentedProductNo}/{productNo}/review")
    public ResponseEntity<ResponseReview> createToRentalerReview(@PathVariable("rentedProductNo") Long rentedProductNo, @PathVariable("productNo") Long productNo, @RequestBody RequestReview requestReview) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        requestReview.setRentedProductNo(rentedProductNo);

        requestReview.setProductNo(productNo);

        System.out.println("서비스 진입 전 requestReview : " + requestReview);

        ReviewDto reviewDto = reviewService.createToRentalerReview(mapper.map(requestReview, ReviewDto.class));

        System.out.println("서비스 요청 후 reviewDto : " + reviewDto);

        ResponseReview responseReview = mapper.map(reviewDto, ResponseReview.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }
}
