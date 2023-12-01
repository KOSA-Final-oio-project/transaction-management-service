package com.oio.transactionservice.service;

import com.oio.transactionservice.config.ModelMapperConfig;
import com.oio.transactionservice.dto.RentedProductDto;
import com.oio.transactionservice.jpa.RentedProductEntity;
import com.oio.transactionservice.jpa.RentedProductRepository;
import com.oio.transactionservice.jpa.status.ReviewStatus;
import com.oio.transactionservice.jpa.status.Status;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RentedProductServiceImpl implements RentedProductService {
    RentedProductRepository rentedProductRepository;

    private ModelMapper mapper;

    @Autowired
    public RentedProductServiceImpl(RentedProductRepository rentedProductRepository) {
        this.rentedProductRepository = rentedProductRepository;
        this.mapper = ModelMapperConfig.modelMapper();
    }

    //대여 시작
    @Override
    public RentedProductDto startRent(RentedProductDto rentedProductDto) {
        rentedProductDto.setReviewStatus(ReviewStatus.없음);
        rentedProductDto.setStatus(Status.대여중);

        RentedProductEntity rentedProductEntity = mapper.map(rentedProductDto, RentedProductEntity.class);

        rentedProductRepository.save(rentedProductEntity);

        RentedProductDto returnRentedProductDto = mapper.map(rentedProductEntity, RentedProductDto.class);

        return returnRentedProductDto;
    }

    //대여 완료
    @Override
    public void updateRentStatus(Long rentedProductNo) {
        RentedProductEntity rentedProductEntity = rentedProductRepository.findByRentedProductNo(rentedProductNo);

        rentedProductEntity.updateStatus(Status.대여완료);

        rentedProductRepository.save(rentedProductEntity);
    }

    //대여 관련 물품 조회(status: 0 = 빌려준, 1 = 빌린)
    @Override
    public List<RentedProductDto> getRentedProduct(String nickname, Long status) {
        List<RentedProductEntity> rentedProductEntity;

        if (status == 0) {
            rentedProductEntity = rentedProductRepository.findRentedProductEntitiesByOwnerNickname(nickname);
        } else if (status == 1) {
            rentedProductEntity = rentedProductRepository.findRentedProductEntitiesByBorrowerNickname(nickname);
        } else {
            throw new FindException("예외발생");
        }

        List<RentedProductDto> returnRentedProductDto = new ArrayList<>();

        for (RentedProductEntity entity : rentedProductEntity) {
            RentedProductDto dto = mapper.map(entity, RentedProductDto.class);
            returnRentedProductDto.add(dto);
        }
        return returnRentedProductDto;
    }

}
