package com.oio.transactionservice.controller;

import com.oio.transactionservice.dto.ReviewDto;
import com.oio.transactionservice.service.ReviewService;
import com.oio.transactionservice.vo.RequestReview;
import com.oio.transactionservice.vo.ResponseReview;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class ReviewController {
    private ReviewService reviewService;
    ModelMapper mapper = new ModelMapper();

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //리뷰 작성
    @PostMapping("/{rentedProductNo}/{productNo}/review")
    public ResponseEntity<ResponseReview> createReview(@PathVariable("rentedProductNo") Long rentedProductNo, @PathVariable("productNo") Long productNo, @RequestBody RequestReview requestReview) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

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
    public ResponseEntity<List<ResponseReview>> getReview(@PathVariable("productNo") Long productNo) {
        List<ReviewDto> reviewDto = reviewService.getReview(productNo);
        List<ResponseReview> responseReview = new ArrayList<>();

        for (ReviewDto dto : reviewDto) {
            ResponseReview review = mapper.map(dto, ResponseReview.class);
            responseReview.add(review);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

    //리뷰 번호로 리뷰 상세 조회
    @GetMapping("/review/{reviewNo}")
    public ResponseEntity<ResponseReview> getReviewDetail(@PathVariable("reviewNo") Long reviewNo) {
        ReviewDto reviewDto = reviewService.getReviewDetail(reviewNo);
        ResponseReview responseReview = mapper.map(reviewDto, ResponseReview.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

    //
    @GetMapping("/{status}/reviews")
    public ResponseEntity<List<ResponseReview>> getMyReview(@RequestParam String nickname, @PathVariable Long status) throws FindException {
        List<ReviewDto> reviewDto = reviewService.getWriteReview(nickname, status);
        List<ResponseReview> responseReview = new ArrayList<>();

        for (ReviewDto dto : reviewDto) {
            ResponseReview review = mapper.map(dto, ResponseReview.class);
            responseReview.add(review);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

}
