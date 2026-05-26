package com.povod9.adcampaign.service;


import com.povod9.adcampaign.dto.PrincipalDto;
import com.povod9.adcampaign.dto.ProductRequest;
import com.povod9.adcampaign.dto.ProductResponse;
import com.povod9.adcampaign.entity.ProductEntity;
import com.povod9.adcampaign.entity.SellerEntity;
import com.povod9.adcampaign.mapper.ProductMapper;
import com.povod9.adcampaign.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper mapper;
  private final SecurityContextService securityContextService;

  @Override
  @Transactional
  public ProductResponse createProduct(ProductRequest productRequest) {
    SellerEntity sellerEntity = securityContextService.getCurrentSellerOrThrow();

    ProductEntity productEntity = new ProductEntity(null, productRequest.name(), sellerEntity);

    ProductEntity savedProduct = productRepository.save(productEntity);
    return mapper.entityToResponse(savedProduct);
  }

  @Override
  public ProductResponse productById(Long id) {
    ProductEntity productEntity =
        productRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find product by id: " + id));
    securityContextService.verifyProductOwner(productEntity);
    return mapper.entityToResponse(productEntity);
  }

  @Override
  public List<ProductResponse> allProduct() {
    PrincipalDto principalDto = securityContextService.getCurrentPrincipalOrThrow();

    List<ProductEntity> productEntities =
        productRepository.findBySeller_SellerId(principalDto.id());

    return mapper.listEntitiesToResponse(productEntities);
  }

  @Override
  @Transactional
  public ProductResponse updateProductById(Long id, ProductRequest productRequest) {

    ProductEntity productEntity =
        productRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find product by id: " + id));
    securityContextService.verifyProductOwner(productEntity);

    if (productRequest.name() != null) {
      productEntity.setProductName(productRequest.name());
    }
    ProductEntity savedProduct = productRepository.save(productEntity);

    return mapper.entityToResponse(savedProduct);
  }

}
