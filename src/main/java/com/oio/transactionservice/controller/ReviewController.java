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

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private ReviewService reviewService;
    private ModelMapper mapper;
    Map map = new HashMap();


    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
        this.mapper = ModelMapperConfig.modelMapper();
    }

    //리뷰 작성
    @PostMapping("/{productNo}/{rentedProductNo}")
    public ResponseEntity<Map<String, String>> createReview(@PathVariable("rentedProductNo") Long rentedProductNo, @PathVariable("productNo") Long productNo, @Valid @RequestBody RequestReview requestReview) throws Exception {
        requestReview.setRentedProductNo(rentedProductNo);
        requestReview.setProductNo(productNo);

        reviewService.createReview(mapper.map(requestReview, ReviewDto.class));

        map.put("msg", "success");

        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable("reviewNo") Long reviewNo) throws Exception {
        reviewService.deleteReview(reviewNo);

        map.put("msg", "success");

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    //상품 번호로 해당 상품 리뷰 전체 조회
    @GetMapping("/reviews/{productNo}")
    public ResponseEntity<List<ResponseReview>> getProductReview(@PathVariable("productNo") Long productNo) throws Exception {
        List<ReviewDto> reviewDto = reviewService.getProductReview(productNo);

        List<ResponseReview> responseReview = new ArrayList<>();

        for (ReviewDto dto : reviewDto) {
            ResponseReview review = mapper.map(dto, ResponseReview.class);
            responseReview.add(review);
        }

        if (responseReview.size() == 0) {
            throw new NoSuchElementException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

    //리뷰 번호로 리뷰 상세 조회
    @GetMapping("/{reviewNo}")
    public ResponseEntity<ResponseReview> getReviewDetail(@PathVariable("reviewNo") Long reviewNo) throws Exception {
        ReviewDto reviewDto = reviewService.getReviewDetail(reviewNo);
        ResponseReview responseReview = mapper.map(reviewDto, ResponseReview.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseReview);
    }

    //사용자 리뷰 조회(status: 0 = 작성한 리뷰, 1 = 받은 리뷰)
    @GetMapping("/myreviews/{status}")
    public ResponseEntity<List<ResponseReview>> getMyReview(@RequestParam String nickname, @PathVariable Long status) throws Exception {
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
    public ResponseEntity<Map<String, String>> updateHeart(@PathVariable Long reviewNo) throws Exception {
        reviewService.updateHeart(reviewNo);

        map.put("msg", "success");

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    //내가 받은 하트 전체 조회
    @GetMapping("/heart")
    public ResponseEntity<Map<String, Long>> getAllHeart(@RequestParam String nickname) throws Exception {
        reviewService.getAllHeart(nickname);

        map.put("msg", reviewService.getAllHeart(nickname));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

}
