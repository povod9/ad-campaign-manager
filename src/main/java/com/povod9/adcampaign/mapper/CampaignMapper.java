package com.povod9.adcampaign.mapper;

import com.povod9.adcampaign.dto.CampaignResponse;
import com.povod9.adcampaign.dto.CampaignUpdateRequest;
import com.povod9.adcampaign.entity.CampaignEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

    @Mapping(source = "product.productId", target = "productId")
    CampaignResponse entityToResponse(CampaignEntity campaignEntity);
    List<CampaignResponse> listEntityToResponse(List<CampaignEntity> campaignEntities);
    void updateEntityFromRequest(CampaignUpdateRequest campaignUpdateRequest, @MappingTarget CampaignEntity campaignEntity);
}
