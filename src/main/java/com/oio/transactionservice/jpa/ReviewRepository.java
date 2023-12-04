package com.oio.transactionservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    //리뷰 번호로 리뷰 상세 조회
    ReviewEntity findByReviewNo(Long reviewNo);

    //상품 번호로 해당 상품 리뷰 전체 조회
    List<ReviewEntity> findReviewEntitiesByRentedProductEntityProductNo(Long productNo);

    //작성한 리뷰 목록(빌린, 빌려준 리뷰)
    List<ReviewEntity> findReviewEntitiesByWriterNickname(String nickname);

    //받은 리뷰 목록(빌린, 빌려준 리뷰)
    List<ReviewEntity> findReviewEntitiesByReceiverNickname(String nickname);

}
