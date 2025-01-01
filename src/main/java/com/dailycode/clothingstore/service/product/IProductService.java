package com.dailycode.clothingstore.service.product;

import com.dailycode.clothingstore.dto.ProductDto;
import com.dailycode.clothingstore.model.Product;
import com.dailycode.clothingstore.request.ProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(ProductRequest request);
    Product updateProduct(ProductRequest request, Long id);
    void deleteProductById(Long id);

    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);
    ProductDto convertToDto(Product product);

}
