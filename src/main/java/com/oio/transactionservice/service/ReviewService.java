package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    //리뷰 작성
    ReviewDto createReview(ReviewDto reviewDto) throws Exception;

    //리뷰 삭제
    void deleteReview(Long reviewNo) throws Exception;

    //상품 번호로 해당 상품 리뷰 전체 조회
    List<ReviewDto> getProductReview(Long rentedProductNo) throws Exception;

    //리뷰 번호로 리뷰 상세 조회
    ReviewDto getReviewDetail(Long reviewNo) throws Exception;

    //사용자 리뷰 조회(status: 0 = 작성한 리뷰, 1 = 받은 리뷰)
    List<ReviewDto> getMyReview(String nickname, Long status) throws Exception;

    //리뷰 좋아요
    void updateHeart(Long reviewNo) throws Exception;

    //리뷰 좋아요 수 조회
    Long getAllHeart(String nickname) throws Exception;
}
