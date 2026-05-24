package com.povod9.adcampaign.service;


import com.povod9.adcampaign.dto.ProductRequest;
import com.povod9.adcampaign.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse productById(Long id);

    List<ProductResponse> allProduct();

    ProductResponse updateProductById(Long id, ProductRequest productRequest);
}
