package com.oio.transactionservice.service;

import com.oio.transactionservice.config.ModelMapperConfig;
import com.oio.transactionservice.dto.ReviewDto;
import com.oio.transactionservice.jpa.RentedProductEntity;
import com.oio.transactionservice.jpa.RentedProductRepository;
import com.oio.transactionservice.jpa.ReviewEntity;
import com.oio.transactionservice.jpa.ReviewRepository;
import com.oio.transactionservice.jpa.status.ReviewStatus;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;
    RentedProductRepository rentedProductRepository;
    private ModelMapper mapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             RentedProductRepository rentedProductRepository) {
        this.reviewRepository = reviewRepository;
        this.rentedProductRepository = rentedProductRepository;
        this.mapper = ModelMapperConfig.modelMapper();
    }

    //리뷰 작성
    @Override
    public ReviewDto createReview(ReviewDto reviewDto) throws Exception {
        try {
            RentedProductEntity rentedProductEntity = rentedProductRepository.findByRentedProductNo(reviewDto.getRentedProductNo());

            ReviewEntity reviewEntity = mapper.map(reviewDto, ReviewEntity.class);

            String owner = rentedProductEntity.getOwnerNickname();
            String writer = reviewEntity.getWriterNickname();

            //RENTED_PRODUCT.REVIEW_STATUS 값만 변경
            switch (rentedProductEntity.getReviewStatus()) {
                case 없음:
                    if (writer.equals(owner)) {
                        rentedProductEntity.updateReviewStatus(ReviewStatus.대여해준사람);
                        reviewEntity.setReviewWriter(ReviewStatus.대여해준사람);
                    } else {
                        rentedProductEntity.updateReviewStatus(ReviewStatus.대여받은사람);
                        reviewEntity.setReviewWriter(ReviewStatus.대여받은사람);
                    }
                    break;

                case 대여해준사람:
                    rentedProductEntity.updateReviewStatus(ReviewStatus.모두);
                    reviewEntity.setReviewWriter(ReviewStatus.대여받은사람);
                    break;

                case 대여받은사람:
                    rentedProductEntity.updateReviewStatus(ReviewStatus.모두);
                    reviewEntity.setReviewWriter(ReviewStatus.대여해준사람);
                    break;
            }
            rentedProductRepository.save(rentedProductEntity);

            //REVIEW 생성
            reviewEntity.setRentedProductEntity(rentedProductEntity);
            reviewEntity.setHeart(0L);
            reviewRepository.save(reviewEntity);

            ReviewDto returnReviewDto = mapper.map(reviewEntity, ReviewDto.class);
            returnReviewDto.setRentedProductNo(reviewEntity.getRentedProductEntity().getRentedProductNo());

            return returnReviewDto;
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            dataIntegrityViolationException.printStackTrace();
            throw new Exception("리뷰 작성 SQL 예외 발생");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("예외 발생");

        }
    }

    //리뷰 삭제
    @Override
    public void deleteReview(Long reviewNo) throws Exception {
        try {
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
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            throw new NullPointerException("리뷰 삭제 예외 발생");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("예외 발생");
        }
    }

    //상품 번호로 해당 상품 리뷰 전체 조회
    @Override
    public List<ReviewDto> getProductReview(Long productNo) throws Exception {
        try {
            List<ReviewEntity> reviewEntity = reviewRepository.findReviewEntitiesByRentedProductEntityProductNo(productNo);

            List<ReviewDto> returnReviewDto = new ArrayList<>();

            for (ReviewEntity entity : reviewEntity) {
                ReviewDto dto = mapper.map(entity, ReviewDto.class);
                returnReviewDto.add(dto);
            }
            return returnReviewDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("예외 발생");
        }
    }

    //리뷰 번호로 리뷰 상세 조회
    @Override
    public ReviewDto getReviewDetail(Long reviewNo) throws Exception {
        try {
            ReviewEntity reviewEntity = reviewRepository.findByReviewNo(reviewNo);

            ReviewDto returnReviewDto = mapper.map(reviewEntity, ReviewDto.class);

            return returnReviewDto;
        } catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
            throw new IllegalAccessException("리뷰 상세 조회 예외 발생");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("예외 발생");
        }
    }

    //사용자 리뷰 조회(status: 0 = 작성한 리뷰, 1 = 받은 리뷰)
    @Override
    public List<ReviewDto> getMyReview(String nickname, Long status) throws Exception {
        try {
            List<ReviewEntity> reviewEntity;

            if (status != null) {
                if (status == 0) {
                    reviewEntity = reviewRepository.findReviewEntitiesByWriterNickname(nickname);
                } else if (status == 1) {
                    reviewEntity = reviewRepository.findReviewEntitiesByReceiverNickname(nickname);
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new NullPointerException();
            }

            List<ReviewDto> returnReviewDto = new ArrayList<>();

            for (ReviewEntity entity : reviewEntity) {
                ReviewDto dto = mapper.map(entity, ReviewDto.class);
                returnReviewDto.add(dto);
            }
            return returnReviewDto;
        } catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
            throw new IllegalArgumentException("사용자 리뷰 조회 예외 발생");
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            throw new NullPointerException("사용자 리뷰 조회 예외 발생");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("예외 발생");
        }
    }

    //리뷰 좋아요
    @Override
    public void updateHeart(Long reviewNo) throws Exception {
        try {
            ReviewEntity reviewEntity = reviewRepository.findByReviewNo(reviewNo);

            if (reviewEntity.getHeart() == 0) {
                reviewEntity.setHeart(1L);
            } else {
                reviewEntity.setHeart(0L);
            }

            reviewRepository.save(reviewEntity);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            throw new NullPointerException("리뷰 좋아요 예외 발생");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("예외 발생");
        }
    }

}
