package com.povod9.adcampaign.service;

import com.povod9.adcampaign.dto.PrincipalDto;
import com.povod9.adcampaign.entity.CampaignEntity;
import com.povod9.adcampaign.entity.ProductEntity;
import com.povod9.adcampaign.entity.SellerEntity;

public interface SecurityContextService {
    PrincipalDto getCurrentPrincipalOrThrow();
    void verifyProductOwner(ProductEntity product);
    void verifyCampaignOwner(CampaignEntity campaign);
    SellerEntity getCurrentSellerOrThrow();
}
