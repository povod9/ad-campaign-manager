package com.povod9.adcampaign.mapper;

import com.povod9.adcampaign.dto.ProductResponse;
import com.povod9.adcampaign.entity.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse entityToResponse(ProductEntity productEntity);
    List<ProductResponse> listEntitiesToResponse(List<ProductEntity> productEntities);
}
