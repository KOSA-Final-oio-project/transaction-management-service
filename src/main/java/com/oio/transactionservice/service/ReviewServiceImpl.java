package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.RentedProductDto;
import com.oio.transactionservice.dto.ReviewDto;
import com.oio.transactionservice.jpa.RentedProductEntity;
import com.oio.transactionservice.jpa.RentedProductRepository;
import com.oio.transactionservice.jpa.ReviewEntity;
import com.oio.transactionservice.jpa.ReviewRepository;
import com.oio.transactionservice.jpa.status.ReviewStatus;
import com.oio.transactionservice.jpa.status.Status;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //대여해준 사람에 대한 후기 작성
    @Override
    public ReviewDto createToRentalerReview(ReviewDto reviewDto) {
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        //RENTED_PRODUCT.REVIEW_STATUS만 조작
        RentedProductEntity rentedProductEntity = rentedProductRepository.findByRentedProductNo(reviewDto.getRentedProductNo());
        rentedProductEntity.updateReviewStatus(ReviewStatus.대여받은사람);
        rentedProductRepository.save(rentedProductEntity);
        System.out.println(rentedProductEntity.getProductNo());
        System.out.println(rentedProductEntity.getRentedProductNo());

        //REVIEW 생성
        ReviewEntity reviewEntity = mapper.map(reviewDto, ReviewEntity.class);

        reviewEntity.setRentedProductNo(rentedProductEntity);

        reviewRepository.save(reviewEntity);

        System.out.println("변환 할 Dto : " + reviewDto);

        ReviewDto retunreReviewDto = mapper.map(reviewEntity, ReviewDto.class);

        System.out.println("리턴 reviewDto : " + retunreReviewDto);

        return retunreReviewDto;
    }

    @Override
    public ReviewDto createToBorrowerReview(ReviewDto reviewDto) {
        return null;
    }

    @Override
    public ReviewDto deleteReview() {
        return null;
    }

    @Override
    public ReviewDto getReview() {
        return null;
    }

    @Override
    public ReviewDto getReviewByProduct() {
        return null;
    }

    @Override
    public ReviewDto createReviewHeart() {
        return null;
    }
}
