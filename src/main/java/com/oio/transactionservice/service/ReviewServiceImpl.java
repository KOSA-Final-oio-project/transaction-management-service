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

    //물건 주인에 대한 후기 작성
    @Override
    public ReviewDto createToOwnerReview(ReviewDto reviewDto) {
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        //RENTED_PRODUCT.REVIEW_STATUS만 조작
        RentedProductEntity rentedProductEntity = rentedProductRepository.findByRentedProductNo(reviewDto.getRentedProductNo());
        if (rentedProductEntity.getReviewStatus() == ReviewStatus.없음) {
            rentedProductEntity.updateReviewStatus(ReviewStatus.대여받은사람);
        } else {
            rentedProductEntity.updateReviewStatus(ReviewStatus.모두);
        }
        rentedProductRepository.save(rentedProductEntity);

        //REVIEW 생성
        ReviewEntity reviewEntity = mapper.map(reviewDto, ReviewEntity.class);
        reviewEntity.setReviewWriter(ReviewStatus.대여받은사람);
        reviewEntity.setRentedProductEntity(rentedProductEntity);
        reviewEntity.setHeart(0L);
        reviewRepository.save(reviewEntity);

        ReviewDto retunreReviewDto = mapper.map(reviewEntity, ReviewDto.class);
        retunreReviewDto.setRentedProductNo(reviewEntity.getRentedProductEntity().getRentedProductNo());

        return retunreReviewDto;
    }

    //물건 대여한 사람에 대한 후기 작성
    @Override
    public ReviewDto createToBorrowerReview(ReviewDto reviewDto) {
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        //RENTED_PRODUCT.REVIEW_STATUS만 조작
        RentedProductEntity rentedProductEntity = rentedProductRepository.findByRentedProductNo(reviewDto.getRentedProductNo());
        if (rentedProductEntity.getReviewStatus() == ReviewStatus.없음) {
            rentedProductEntity.updateReviewStatus(ReviewStatus.대여해준사람);
        } else {
            rentedProductEntity.updateReviewStatus(ReviewStatus.모두);
        }
        rentedProductRepository.save(rentedProductEntity);

        //REVIEW 생성
        ReviewEntity reviewEntity = mapper.map(reviewDto, ReviewEntity.class);
        reviewEntity.setReviewWriter(ReviewStatus.대여해준사람);
        reviewEntity.setRentedProductEntity(rentedProductEntity);
        reviewRepository.save(reviewEntity);

        ReviewDto retunreReviewDto = mapper.map(reviewEntity, ReviewDto.class);
        retunreReviewDto.setRentedProductNo(reviewEntity.getRentedProductEntity().getRentedProductNo());

        return retunreReviewDto;
    }

    //리뷰 삭제(상태값만 바뀜)
    @Override
    public ReviewDto deleteReview(Long reviewNo) {
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
                reviewEntity.getRentedProductEntity().updateReviewStatus(reviewStatus);
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
        return null;
    }

    //해당 상품 번호로 리뷰 전체 조회
    @Override
    public List<ReviewEntity> getReview(Long productNo) {
        return reviewRepository.findReviewEntitiesByRentedProductEntityProductNo(productNo);
    }

    //리뷰 상세 조회
    @Override
    public ReviewEntity getReviewDetail(Long reviewNo) {
        return reviewRepository.findByReviewNo(reviewNo);
    }

    @Override
    public ReviewDto createReviewHeart() {
        return null;
    }
}
