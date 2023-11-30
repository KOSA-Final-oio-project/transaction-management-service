package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.ReviewDto;

import java.lang.module.FindException;
import java.util.List;

public interface ReviewService {
    //리뷰 작성
    ReviewDto createReview(ReviewDto reviewDto);

    //리뷰 삭제
    void deleteReview(Long reviewNo);

    //상품 번호로 해당 상품 리뷰 전체 조회
    List<ReviewDto> getReview(Long rentedProductNo);

    //리뷰 번호로 리뷰 상세 조회
    ReviewDto getReviewDetail(Long reviewNo);

    List<ReviewDto> getWriteReview(String nickname, Long status) throws FindException;

    //리뷰 좋아요
    ReviewDto createReviewHeart();
}
