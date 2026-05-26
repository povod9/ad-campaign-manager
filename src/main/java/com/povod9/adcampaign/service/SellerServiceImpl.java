package com.povod9.adcampaign.service;


import com.povod9.adcampaign.dto.*;
import com.povod9.adcampaign.entity.SellerEntity;
import com.povod9.adcampaign.exception.InvalidCredentialsException;
import com.povod9.adcampaign.mapper.SellerMapper;
import com.povod9.adcampaign.repository.SellerRepository;
import com.povod9.adcampaign.security.JwtCore;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

  private final SellerRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final SellerMapper mapper;
  private final JwtCore jwtCore;
  private final SecurityContextService securityContextService;

  @Override
  @Transactional
  public SellerResponse createSellerAccount(SellerRequest seller) {
    if (repository.existsByEmail(seller.email())) {
      throw new IllegalArgumentException("Email already exists: " + seller.email());
    }

    SellerEntity createdSeller =
        new SellerEntity(
            null,
            seller.sellerName(),
            seller.email(),
            passwordEncoder.encode(seller.password()),
            seller.emeraldAmountFunds());

    SellerEntity savedSeller = repository.save(createdSeller);
    return mapper.entityToResponse(savedSeller);
  }

  @Override
  public LoginResponse loginSeller(LoginRequest login) {
    SellerEntity sellerEntity =
        repository
            .findByEmail(login.email())
            .orElseThrow(
                () -> new EntityNotFoundException("Cannot find seller by email: " + login.email()));

    if (passwordEncoder.matches(login.password(), sellerEntity.getPassword())) {
      return new LoginResponse(jwtCore.generateToken(sellerEntity), "Bearer");
    } else {
      throw new InvalidCredentialsException("Wrong password or email");
    }
  }

  @Override
  @Transactional
  public SellerResponse updateSeller(SellerUpdateRequest sellerUpdateRequest) {

    PrincipalDto principalDto = securityContextService.getCurrentPrincipalOrThrow();

    SellerEntity sellerEntity =
        repository
            .findById(principalDto.id())
            .orElseThrow(
                () ->
                    new EntityNotFoundException("Cannot find seller by id: " + principalDto.id()));

    if (sellerUpdateRequest.email() != null) {
      if (sellerUpdateRequest.email().equals(sellerEntity.getEmail())) {
        throw new IllegalArgumentException(
            "Email cannot be the same: " + sellerUpdateRequest.email());
      }
      if (repository.existsByEmail(sellerUpdateRequest.email())) {
        throw new IllegalArgumentException("Email already exist:" + sellerUpdateRequest.email());
      }
      sellerEntity.setEmail(sellerUpdateRequest.email());
    }

    if (sellerUpdateRequest.password() != null) {
      if (passwordEncoder.matches(sellerUpdateRequest.password(), sellerEntity.getPassword())) {
        throw new IllegalArgumentException("Password cannot be the same");
      }
      sellerEntity.setPassword(passwordEncoder.encode(sellerUpdateRequest.password()));
    }

    mapper.updateEntityFromResponse(sellerUpdateRequest, sellerEntity);
    return mapper.entityToResponse(sellerEntity);
  }
}
