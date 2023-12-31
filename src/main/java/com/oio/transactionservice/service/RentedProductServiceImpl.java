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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RentedProductServiceImpl implements RentedProductService {
    private RentedProductRepository rentedProductRepository;
    private ModelMapper mapper;

    @Autowired
    public RentedProductServiceImpl(RentedProductRepository rentedProductRepository) {
        this.rentedProductRepository = rentedProductRepository;
        this.mapper = ModelMapperConfig.modelMapper();
    }

    //대여 시작
    @Override
    public RentedProductDto startRent(RentedProductDto rentedProductDto) throws Exception {
        try {
            if (rentedProductDto.getProductNo() != null) {
                rentedProductDto.setReviewStatus(ReviewStatus.없음);
                rentedProductDto.setStatus(Status.대여중);

                RentedProductEntity rentedProductEntity = mapper.map(rentedProductDto, RentedProductEntity.class);

                rentedProductRepository.save(rentedProductEntity);

                RentedProductDto returnRentedProductDto = mapper.map(rentedProductEntity, RentedProductDto.class);

                return returnRentedProductDto;
            } else {
                throw new NullPointerException();
            }
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            log.error("DataIntegrityViolationException : " + dataIntegrityViolationException.getMessage());
            throw new DataIntegrityViolationException("대여 시작 SQL 예외 발생");
        } catch (NullPointerException nullPointerException) {
            log.error("NullPointerException : " + nullPointerException.getMessage());
            throw new NullPointerException("대여 시작 Null 예외 발생");
        } catch (Exception e) {
            log.error("Exception : " + e.getMessage());
            throw new Exception("예외 발생");
        }
    }

    //대여 완료
    @Override
    public RentedProductDto updateRentStatus(Long rentedProductNo) throws Exception {
        try {
            RentedProductEntity rentedProductEntity = rentedProductRepository.findByRentedProductNo(rentedProductNo);

            rentedProductEntity.updateStatus(Status.대여완료);

            RentedProductDto rentedProductDto = mapper.map(rentedProductEntity, RentedProductDto.class);

            rentedProductRepository.save(rentedProductEntity);

            return rentedProductDto;
        } catch (NullPointerException nullPointerException) {
            log.error("NullPointerException : " + nullPointerException.getMessage());
            throw new NullPointerException("대여 완료 예외 발생");
        } catch (Exception e) {
            log.error("Exception : " + e.getMessage());
            throw new Exception("예외 발생");
        }
    }

    //대여 관련 물품 조회(status: 0 = 빌려준, 1 = 빌린)
    @Override
    public List<RentedProductDto> getRentedProductList(String nickname, Long status) throws Exception {
        try {
            List<RentedProductEntity> rentedProductEntity;

            if (status != null) {
                if (status == 0) {
                    rentedProductEntity = rentedProductRepository.findRentedProductEntitiesOrderedByOwnerNickname(nickname);
                } else if (status == 1) {
                    rentedProductEntity = rentedProductRepository.findRentedProductEntitiesOrderedByBorrowerNickname(nickname);
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new NullPointerException();
            }

            List<RentedProductDto> returnRentedProductDto = new ArrayList<>();


            for (RentedProductEntity entity : rentedProductEntity) {
                RentedProductDto dto = mapper.map(entity, RentedProductDto.class);
                returnRentedProductDto.add(dto);
            }

            return returnRentedProductDto;
        } catch (IllegalArgumentException illegalArgumentException) {
            log.error("IllegalArgumentException : " + illegalArgumentException.getMessage());
            throw new IllegalArgumentException("대여 관련 조회 예외 발생");
        } catch (NullPointerException nullPointerException) {
            log.error("NullPointerException : " + nullPointerException.getMessage());
            throw new NullPointerException("대여 관련 조회 예외 발생");
        } catch (Exception e) {
            log.error("Exception : " + e.getMessage());
            throw new Exception("예외 발생");
        }
    }

    //대여 번호로 대여 상세 조회
    @Override
    public RentedProductDto getRentedProduct(Long rentedProductNo) throws Exception {
        try {
            RentedProductEntity rentedProductEntity = rentedProductRepository.findByRentedProductNo(rentedProductNo);
            RentedProductDto rentedProductDto = mapper.map(rentedProductEntity, RentedProductDto.class);
            return rentedProductDto;
        } catch (IllegalArgumentException illegalArgumentException) {
            log.error("IllegalArgumentException : " + illegalArgumentException.getMessage());
            throw new IllegalArgumentException("사용자 리뷰 조회 예외 발생");
        } catch (Exception e) {
            log.error("Exception : " + e.getMessage());
            throw new Exception("예외 발생");
        }
    }

}