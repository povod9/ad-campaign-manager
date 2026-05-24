package com.povod9.adcampaign.repository;

import com.povod9.adcampaign.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignEntity, Long> {
    List<CampaignEntity> findByProduct_Seller_SellerId(Long sellerId);
}
