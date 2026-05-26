package com.povod9.adcampaign.service;

import com.povod9.adcampaign.dto.PrincipalDto;
import com.povod9.adcampaign.entity.CampaignEntity;
import com.povod9.adcampaign.entity.ProductEntity;
import com.povod9.adcampaign.entity.SellerEntity;
import com.povod9.adcampaign.exception.AccessDeniedException;
import com.povod9.adcampaign.exception.InvalidCredentialsException;
import com.povod9.adcampaign.repository.SellerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityContextServiceImpl implements SecurityContextService{

    private final SellerRepository sellerRepository;

    public  PrincipalDto getCurrentPrincipalOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new InvalidCredentialsException("You are unauthorized");
        }
        return (PrincipalDto) authentication.getPrincipal();
    }

    @Override
    public void verifyProductOwner(ProductEntity product) {
        PrincipalDto principal = getCurrentPrincipalOrThrow();
        if (product.getSeller() == null || !product.getSeller().getSellerId().equals(principal.id())) {
            throw new AccessDeniedException("Forbidden: You do not own this product");
        }
    }

    @Override
    public void verifyCampaignOwner(CampaignEntity campaign) {
        PrincipalDto principal = getCurrentPrincipalOrThrow();
        if (campaign.getProduct() == null ||
                campaign.getProduct().getSeller() == null ||
                !campaign.getProduct().getSeller().getSellerId().equals(principal.id())) {

            throw new AccessDeniedException("Forbidden: You do not own this campaign");
        }
    }

    @Override
    public SellerEntity getCurrentSellerOrThrow() {
        PrincipalDto principal = getCurrentPrincipalOrThrow();
        return sellerRepository.findById(principal.id())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find seller by id: " + principal.id()));
    }

}
