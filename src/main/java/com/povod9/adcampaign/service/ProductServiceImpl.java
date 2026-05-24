package com.povod9.adcampaign.service;

import com.povod9.adcampaign.dto.PrincipalDto;
import com.povod9.adcampaign.dto.ProductRequest;
import com.povod9.adcampaign.dto.ProductResponse;
import com.povod9.adcampaign.entity.ProductEntity;
import com.povod9.adcampaign.entity.SellerEntity;
import com.povod9.adcampaign.exception.AccessDeniedException;
import com.povod9.adcampaign.mapper.ProductMapper;
import com.povod9.adcampaign.repository.ProductRepository;
import com.povod9.adcampaign.repository.SellerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.povod9.adcampaign.helper_method.SecurityUtil.getCurrentPrincipalOrThrow;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final ProductMapper mapper;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        SellerEntity sellerEntity = getCurrentSellerOrThrow();

        ProductEntity productEntity = new ProductEntity(
                null,
                productRequest.name(),
                sellerEntity
        );

        ProductEntity savedProduct = productRepository.save(productEntity);
        return mapper.entityToResponse(savedProduct);
    }

    @Override
    public ProductResponse productById(Long id) {
        PrincipalDto principalDto = getCurrentPrincipalOrThrow();
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find product by id: " + id));
        checkProductOwnerOrThrow(productEntity, principalDto);
        return mapper.entityToResponse(productEntity);
    }

    @Override
    public List<ProductResponse> allProduct() {
        PrincipalDto principalDto = getCurrentPrincipalOrThrow();

        List<ProductEntity> productEntities = productRepository.findBySeller_SellerId(principalDto.id());

        return mapper.listEntitiesToResponse(productEntities);
    }

    @Override
    public ProductResponse updateProductById(Long id, ProductRequest productRequest) {
        PrincipalDto principalDto = getCurrentPrincipalOrThrow();
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find product by id: " + id));
        checkProductOwnerOrThrow(productEntity, principalDto);

        if(productRequest.name() != null){
            productEntity.setProductName(productRequest.name());
        }
        ProductEntity savedProduct = productRepository.save(productEntity);

        return mapper.entityToResponse(savedProduct);
    }

    private SellerEntity getCurrentSellerOrThrow(){
        PrincipalDto principalDto = getCurrentPrincipalOrThrow();
        return sellerRepository.findById(principalDto.id())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find by id: " + principalDto.id()));
    }

    private void checkProductOwnerOrThrow(ProductEntity product, PrincipalDto principal){
        if (!product.getSeller().getSellerId().equals(principal.id())) {
            throw new AccessDeniedException("Forbidden");
        }
    }
}
