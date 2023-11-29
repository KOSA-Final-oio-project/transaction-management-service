package com.oio.transactionservice.controller;

import com.oio.transactionservice.dto.ReviewDto;
import com.oio.transactionservice.jpa.ReviewEntity;
import com.oio.transactionservice.service.ReviewService;
import com.oio.transactionservice.vo.RequestReview;
import com.oio.transactionservice.vo.ResponseReview;
import feign.Response;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //물건 주인에 대한 후기 작성
    @PostMapping("/{rentedProductNo}/{productNo}/ownerreview")
    public ResponseEntity<ResponseReview> createRentalerReview(@PathVariable("rentedProductNo") Long rentedProductNo, @PathVariable("productNo") Long productNo, @RequestBody RequestReview requestReview) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        requestReview.setRentedProductNo(rentedProductNo);
        requestReview.setProductNo(productNo);

        ReviewDto reviewDto = reviewService.createToOwnerReview(mapper.map(requestReview, ReviewDto.class));

        ResponseReview responseReview = mapper.map(reviewDto, ResponseReview.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

    //물건 빌린 사람에 대한 후기 작성
    @PostMapping("/{rentedProductNo}/{productNo}/borrowerreview")
    public ResponseEntity<ResponseReview> createBorrowerReview(@PathVariable("rentedProductNo") Long rentedProductNo, @PathVariable("productNo") Long productNo, @RequestBody RequestReview requestReview) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        requestReview.setRentedProductNo(rentedProductNo);
        requestReview.setProductNo(productNo);

        ReviewDto reviewDto = reviewService.createToBorrowerReview(mapper.map(requestReview, ReviewDto.class));

        ResponseReview responseReview = mapper.map(reviewDto, ResponseReview.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

    //리뷰 삭제(상태값만 바뀜)
    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<ResponseReview> deleteReview(@PathVariable("reviewNo") Long reviewNo) {
    reviewService.deleteReview(reviewNo);
    return null;
    }

    //해당 상품 번호로 리뷰 전체 조회
    @GetMapping("/reviews/{productNo}")
    public ResponseEntity<List<ResponseReview>> getReview(@PathVariable("productNo") Long productNo) {
        List<ReviewEntity> reviewList = reviewService.getReview(productNo);
        List<ResponseReview> result = new ArrayList<>();

        reviewList.forEach(v -> {
            result.add(mapper.map(v, ResponseReview.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/review/{reviewNo}")
    public ResponseEntity<ResponseReview> getReviewDetail(@PathVariable("reviewNo") Long reviewNo) {
        ReviewEntity reviewEntity = reviewService.getReviewDetail(reviewNo);
        ResponseReview responseReview = mapper.map(reviewEntity, ResponseReview.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

}
