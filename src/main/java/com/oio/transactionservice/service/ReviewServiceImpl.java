package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.ReviewDto;
import com.oio.transactionservice.jpa.RentedProductEntity;
import com.oio.transactionservice.jpa.RentedProductRepository;
import com.oio.transactionservice.jpa.ReviewEntity;
import com.oio.transactionservice.jpa.ReviewRepository;
import com.oio.transactionservice.jpa.status.ReviewStatus;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;
    RentedProductRepository rentedProductRepository;
    ModelMapper mapper = new ModelMapper();

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             RentedProductRepository rentedProductRepository) {
        this.reviewRepository = reviewRepository;
        this.rentedProductRepository = rentedProductRepository;
    }

    void mapper() {
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
    }

    //리뷰 작성
    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        mapper();

        RentedProductEntity rentedProductEntity = rentedProductRepository.findByRentedProductNo(reviewDto.getRentedProductNo());

        ReviewEntity reviewEntity = mapper.map(reviewDto, ReviewEntity.class);

        String owner = rentedProductEntity.getOwnerNickname();
        String writer = reviewEntity.getWriterNickname();

        //RENTED_PRODUCT.REVIEW_STATUS 값만 변경
        switch (rentedProductEntity.getReviewStatus()) {
            case 없음:
                if (writer.equals(owner)) {
                    rentedProductEntity.updateReviewStatus(ReviewStatus.대여해준사람);
                } else {
                    rentedProductEntity.updateReviewStatus(ReviewStatus.대여받은사람);
                }
                break;

            case 대여해준사람:
                rentedProductEntity.updateReviewStatus(ReviewStatus.모두);
                break;

            case 대여받은사람:
                rentedProductEntity.updateReviewStatus(ReviewStatus.모두);
                break;
        }
        rentedProductRepository.save(rentedProductEntity);

        //REVIEW 생성
        if (rentedProductEntity.getReviewStatus() == ReviewStatus.대여해준사람) {
            reviewEntity.setReviewWriter(ReviewStatus.대여해준사람);
        } else {
            reviewEntity.setReviewWriter(ReviewStatus.대여받은사람);
        }

        reviewEntity.setRentedProductEntity(rentedProductEntity);
        reviewEntity.setHeart(0L);
        reviewRepository.save(reviewEntity);

        ReviewDto returnReviewDto = mapper.map(reviewEntity, ReviewDto.class);
        returnReviewDto.setRentedProductNo(reviewEntity.getRentedProductEntity().getRentedProductNo());

        return returnReviewDto;
    }

    //리뷰 삭제
    @Override
    public void deleteReview(Long reviewNo) {
        ReviewEntity reviewEntity = reviewRepository.findByReviewNo(reviewNo);

        ReviewStatus reviewStatus = reviewEntity.getRentedProductEntity().getReviewStatus();
        ReviewStatus reviewWriter = reviewEntity.getReviewWriter();

        switch (reviewStatus) {
            case 모두:
                if (reviewWriter == ReviewStatus.대여해준사람) {
                    reviewStatus = ReviewStatus.대여받은사람;
                } else {
                    reviewStatus = ReviewStatus.대여해준사람;
                }
                break;

            case 대여받은사람:
                reviewStatus = ReviewStatus.없음;
                break;

            case 대여해준사람:
                reviewStatus = ReviewStatus.없음;
                break;
        }
        reviewEntity.getRentedProductEntity().updateReviewStatus(reviewStatus);
        reviewRepository.delete(reviewEntity);
    }

    //상품 번호로 해당 상품 리뷰 전체 조회
    @Override
    public List<ReviewDto> getReview(Long productNo) {
        mapper();

        List<ReviewEntity> reviewEntity = reviewRepository.findReviewEntitiesByRentedProductEntityProductNo(productNo);

        List<ReviewDto> returnReviewDto = new ArrayList<>();

        for (ReviewEntity entity : reviewEntity) {
            ReviewDto dto = mapper.map(entity, ReviewDto.class);
            returnReviewDto.add(dto);
        }
        return returnReviewDto;
    }

    //리뷰 번호로 리뷰 상세 조회
    @Override
    public ReviewDto getReviewDetail(Long reviewNo) {
        mapper();

        ReviewEntity reviewEntity = reviewRepository.findByReviewNo(reviewNo);

        ReviewDto returnReviewDto = mapper.map(reviewEntity, ReviewDto.class);

        return returnReviewDto;
    }

    //작성한 리뷰 목록
    @Override
    public List<ReviewDto> getWriteReview(String nickname, Long status) throws FindException {
        mapper();

        List<ReviewEntity> reviewEntity;

        if (status == 0) {
         reviewEntity = reviewRepository.findReviewEntitiesByWriterNickname(nickname);
        } else if (status == 1) {
            reviewEntity = reviewRepository.findReviewEntitiesByReceiverNickname(nickname);
        } else {
            throw new FindException("예외발생");
        }

        List<ReviewDto> returnReviewDto = new ArrayList<>();

        for (ReviewEntity entity : reviewEntity) {
            ReviewDto dto = mapper.map(entity, ReviewDto.class);
            returnReviewDto.add(dto);
        }
        return returnReviewDto;
    }

    //받은 리뷰 목록
    public List<ReviewDto> getReceiveReview(String nickname) {
        mapper();

        List<ReviewEntity> reviewEntity = reviewRepository.findReviewEntitiesByReceiverNickname(nickname);

        List<ReviewDto> returnReviewDto = new ArrayList<>();

        for (ReviewEntity entity : reviewEntity) {
            ReviewDto dto = mapper.map(entity, ReviewDto.class);
            returnReviewDto.add(dto);
        }
        return returnReviewDto;
    }

    @Override
    public ReviewDto createReviewHeart() {
        return null;
    }
}
