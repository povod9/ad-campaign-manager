package com.povod9.adcampaign.repository;

import com.povod9.adcampaign.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<CampaignEntity, Long> {
}
