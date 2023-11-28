package com.oio.transactionservice.service;

import com.oio.transactionservice.dto.RentedProductDto;
import com.oio.transactionservice.jpa.RentedProductEntity;
import com.oio.transactionservice.jpa.RentedProductRepository;
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
public class RentedProductServiceImpl implements RentedProductService {
    RentedProductRepository rentedProductRepository;

    ModelMapper mapper = new ModelMapper();

    @Autowired
    public RentedProductServiceImpl(RentedProductRepository rentedProductRepository) {
        this.rentedProductRepository = rentedProductRepository;

    }

    //대여 시작
    @Override
    public RentedProductDto startRent(RentedProductDto rentedProductDto) {
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        rentedProductDto.setReviewStatus(ReviewStatus.없음);
        rentedProductDto.setStatus(Status.대여중);

        RentedProductEntity rentedProductEntity = mapper.map(rentedProductDto, RentedProductEntity.class);

        rentedProductRepository.save(rentedProductEntity);

        RentedProductDto returnRentedProductDto = mapper.map(rentedProductEntity, RentedProductDto.class);

        return returnRentedProductDto;

    }

    //대여 종료
    @Override
    public RentedProductDto endRent(Long rentedProductNo) {
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        RentedProductEntity rentedProductEntity = rentedProductRepository.findByRentedProductNo(rentedProductNo);
        rentedProductEntity.updateStatus(Status.대여완료);
        rentedProductRepository.save(rentedProductEntity);

        RentedProductDto rentedProductDto = mapper.map(rentedProductEntity, RentedProductDto.class);

        return rentedProductDto;

    }

    //대여 물품 삭제(상태값 대여종료로 바뀜)
    @Override
    public RentedProductDto deleteRent(Long rentedProductNo) {
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        RentedProductEntity rentedProductEntity = rentedProductRepository.findByRentedProductNo(rentedProductNo);

        rentedProductEntity.updateStatus(Status.대여종료);

        rentedProductRepository.save(rentedProductEntity);

        RentedProductDto rentedProductDto = mapper.map(rentedProductEntity, RentedProductDto.class);

        return rentedProductDto;
    }

    @Override
    public RentedProductDto getRentedProductByUserEmail(String email) {
        return null;
    }

    @Override
    public RentedProductDto getBorrowedProductByUserEmail(String email) {
        return null;
    }
}
