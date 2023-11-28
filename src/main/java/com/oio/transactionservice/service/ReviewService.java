package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.ReviewDto;

public interface ReviewService {
    //대여해준 사람에 대한 후기 작성
    ReviewDto createToRentalerReview(ReviewDto reviewDto);

    //대여한 사람에 대한 후기 작성
    ReviewDto createToBorrowerReview(ReviewDto reviewDto);

    //후기 삭제
    ReviewDto deleteReview();

    //후기 전체 조회
    ReviewDto getReview();

    //후기 상세 조회
    ReviewDto getReviewByProduct();

    //후기 좋아요
    ReviewDto createReviewHeart();
}
