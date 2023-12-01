package com.oio.transactionservice.controller;

import com.oio.transactionservice.config.ModelMapperConfig;
import com.oio.transactionservice.dto.ReviewDto;
import com.oio.transactionservice.service.ReviewService;
import com.oio.transactionservice.vo.RequestReview;
import com.oio.transactionservice.vo.ResponseReview;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private ReviewService reviewService;
    private ModelMapper mapper;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
        this.mapper = ModelMapperConfig.modelMapper();
    }

    //리뷰 작성
    @PostMapping("/{productNo}/{rentedProductNo}")
    public ResponseEntity<ResponseReview> createReview(@PathVariable("rentedProductNo") Long rentedProductNo, @PathVariable("productNo") Long productNo, @RequestBody RequestReview requestReview) {
        requestReview.setRentedProductNo(rentedProductNo);
        requestReview.setProductNo(productNo);

        ReviewDto reviewDto = reviewService.createReview(mapper.map(requestReview, ReviewDto.class));

        ResponseReview responseReview = mapper.map(reviewDto, ResponseReview.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewNo}")
    public void deleteReview(@PathVariable("reviewNo") Long reviewNo) {
        reviewService.deleteReview(reviewNo);
    }

    //상품 번호로 해당 상품 리뷰 전체 조회
    @GetMapping("/reviews/{productNo}")
    public ResponseEntity<List<ResponseReview>> getProductReview(@PathVariable("productNo") Long productNo) {
        List<ReviewDto> reviewDto = reviewService.getProductReview(productNo);
        List<ResponseReview> responseReview = new ArrayList<>();

        for (ReviewDto dto : reviewDto) {
            ResponseReview review = mapper.map(dto, ResponseReview.class);
            responseReview.add(review);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

    //리뷰 번호로 리뷰 상세 조회
    @GetMapping("/{reviewNo}")
    public ResponseEntity<ResponseReview> getReviewDetail(@PathVariable("reviewNo") Long reviewNo) {
        ReviewDto reviewDto = reviewService.getReviewDetail(reviewNo);
        ResponseReview responseReview = mapper.map(reviewDto, ResponseReview.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

    //사용자 리뷰 조회(status: 0 = 작성한 리뷰, 1 = 받은 리뷰)
    @GetMapping("/myreviews/{status}")
    public ResponseEntity<List<ResponseReview>> getMyReview(@RequestParam String nickname, @PathVariable Long status) throws FindException {
        List<ReviewDto> reviewDto = reviewService.getMyReview(nickname, status);
        List<ResponseReview> responseReview = new ArrayList<>();

        for (ReviewDto dto : reviewDto) {
            ResponseReview review = mapper.map(dto, ResponseReview.class);
            responseReview.add(review);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

    //리뷰 좋아요
    @PutMapping("/{reviewNo}")
    public void updateHeart(@PathVariable Long reviewNo) {
        reviewService.updateHeart(reviewNo);
    }

}
