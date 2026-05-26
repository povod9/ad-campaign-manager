package com.povod9.adcampaign.mapper;

import com.povod9.adcampaign.dto.SellerResponse;
import com.povod9.adcampaign.dto.SellerUpdateRequest;
import com.povod9.adcampaign.entity.SellerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SellerMapper {

  SellerResponse entityToResponse(SellerEntity sellerEntity);

  @Mapping(target = "email", ignore = true)
  @Mapping(target = "password", ignore = true)
  void updateEntityFromResponse(
      SellerUpdateRequest sellerUpdateRequest, @MappingTarget SellerEntity sellerEntity);
}
