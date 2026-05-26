package com.povod9.adcampaign.service;

import static com.povod9.adcampaign.helper_method.SecurityUtil.getCurrentPrincipalOrThrow;

import com.povod9.adcampaign.dto.*;
import com.povod9.adcampaign.entity.CampaignEntity;
import com.povod9.adcampaign.entity.ProductEntity;
import com.povod9.adcampaign.entity.SellerEntity;
import com.povod9.adcampaign.exception.AccessDeniedException;
import com.povod9.adcampaign.mapper.CampaignMapper;
import com.povod9.adcampaign.repository.CampaignRepository;
import com.povod9.adcampaign.repository.ProductRepository;
import com.povod9.adcampaign.repository.SellerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

  private final CampaignRepository campaignRepository;
  private final ProductRepository productRepository;
  private final SellerRepository sellerRepository;
  private final CampaignMapper mapper;

  @Override
  public CampaignResponse campaignById(Long id) {
    PrincipalDto principalDto = getCurrentPrincipalOrThrow();
    CampaignEntity campaignEntity = getCampaignOrThrow(id);
    checkCampaignOwnerOrThrow(campaignEntity, principalDto);
    return mapper.entityToResponse(campaignEntity);
  }

  @Override
  public List<CampaignResponse> allCampaigns() {
    PrincipalDto principalDto = getCurrentPrincipalOrThrow();
    List<CampaignEntity> campaignEntities =
        campaignRepository.findByProduct_Seller_SellerId(principalDto.id());
    return mapper.listEntityToResponse(campaignEntities);
  }

  @Override
  @Transactional
  public CampaignResponse createCampaign(CampaignRequest campaignRequest) {
    PrincipalDto principalDto = getCurrentPrincipalOrThrow();

    ProductEntity productEntity =
        productRepository
            .findById(campaignRequest.productId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Cannot find product by id: " + campaignRequest.productId()));

    SellerEntity sellerEntity =
        sellerRepository
            .findById(principalDto.id())
            .orElseThrow(
                () ->
                    new EntityNotFoundException("Cannot find seller by id: " + principalDto.id()));

    if (!productEntity.getSeller().getSellerId().equals(principalDto.id())) {
      throw new AccessDeniedException("Forbidden");
    }

    if (sellerEntity.getEmeraldAmountFunds().compareTo(campaignRequest.campaignFund()) < 0) {
      throw new IllegalArgumentException(
          "Not enough emeralds! Your balance: " + sellerEntity.getEmeraldAmountFunds());
    }

    BigDecimal newBalance =
        sellerEntity.getEmeraldAmountFunds().subtract(campaignRequest.campaignFund());
    sellerEntity.setEmeraldAmountFunds(newBalance);
    sellerRepository.save(sellerEntity);

    CampaignEntity campaignEntity =
        new CampaignEntity(
            null,
            campaignRequest.name(),
            campaignRequest.keywords(),
            campaignRequest.bidAmount(),
            campaignRequest.campaignFund(),
            campaignRequest.status(),
            campaignRequest.town(),
            campaignRequest.radius(),
            productEntity);

    campaignRepository.save(campaignEntity);

    return mapper.entityToResponse(campaignEntity);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    PrincipalDto principalDto = getCurrentPrincipalOrThrow();
    CampaignEntity campaignEntity = getCampaignOrThrow(id);
    checkCampaignOwnerOrThrow(campaignEntity, principalDto);
    campaignRepository.delete(campaignEntity);
  }

  @Override
  @Transactional
  public CampaignResponse updateById(Long id, CampaignUpdateRequest campaignUpdateRequest) {
    PrincipalDto principalDto = getCurrentPrincipalOrThrow();
    CampaignEntity campaignEntity = getCampaignOrThrow(id);
    checkCampaignOwnerOrThrow(campaignEntity, principalDto);
    if(campaignUpdateRequest.campaignFund() != null){
      SellerEntity sellerEntity = campaignEntity.getProduct().getSeller();
      if(sellerEntity.getEmeraldAmountFunds().compareTo(campaignUpdateRequest.campaignFund()) < 0){
        throw new IllegalArgumentException("Not enough emeralds! Your balance: " + sellerEntity.getEmeraldAmountFunds());
      }
      BigDecimal newSellerEmeraldAmountFunds = sellerEntity.getEmeraldAmountFunds().subtract(campaignUpdateRequest.campaignFund());
      sellerEntity.setEmeraldAmountFunds(newSellerEmeraldAmountFunds);
      BigDecimal newCampaignEmeraldAmountFunds = campaignEntity.getCampaignFund().add(campaignUpdateRequest.campaignFund());
      campaignEntity.setCampaignFund(newCampaignEmeraldAmountFunds);
    }
    mapper.updateEntityFromRequest(campaignUpdateRequest, campaignEntity);
    return mapper.entityToResponse(campaignEntity);
  }

  private void checkCampaignOwnerOrThrow(CampaignEntity campaignEntity, PrincipalDto principalDto) {
    if (!campaignEntity.getProduct().getSeller().getSellerId().equals(principalDto.id())) {
      throw new AccessDeniedException("Forbidden");
    }
  }

  private CampaignEntity getCampaignOrThrow(Long id) {
    return campaignRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cannot find campaign by id: " + id));
  }
}
