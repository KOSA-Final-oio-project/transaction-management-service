package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.ReviewDto;
import com.oio.transactionservice.jpa.ReviewEntity;

import java.util.List;

public interface ReviewService {
    //물건 주인에 대한 후기 작성
    ReviewDto createToOwnerReview(ReviewDto reviewDto);

    //물건 대여한 사람에 대한 후기 작성
    ReviewDto createToBorrowerReview(ReviewDto reviewDto);

    //리뷰 삭제(상태값만 바뀜)
    ReviewDto deleteReview(Long reviewNo);

    //해당 상품 번호로 리뷰 전체 조회
    List<ReviewEntity> getReview(Long rentedProductNo);

    //상품 리뷰 상세 조회
    ReviewEntity getReviewDetail(Long reviewNo);

    //리뷰 좋아요
    ReviewDto createReviewHeart();
}
