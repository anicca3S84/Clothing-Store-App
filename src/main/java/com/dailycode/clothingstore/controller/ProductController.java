package com.dailycode.clothingstore.controller;

import com.dailycode.clothingstore.dto.ProductDto;
import com.dailycode.clothingstore.exceptions.AlreadyExistException;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Product;
import com.dailycode.clothingstore.request.ProductRequest;
import com.dailycode.clothingstore.response.ApiResponse;
import com.dailycode.clothingstore.service.product.IProductService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/products/", produces = "application/json")
public class ProductController {
    private final IProductService iProductService;

    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @GetMapping("all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products = iProductService.getAllProducts();
        List<ProductDto> productDtos = iProductService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Get all success", productDtos));
    }

    @GetMapping("product/{id}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        try {
            Product product = iProductService.getProductById(id);
            ProductDto productDto = iProductService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Get success", productDto));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("product/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductRequest request){
        try {
            Product product = iProductService.addProduct(request);
            ProductDto productDto = iProductService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Add success", productDto));
        }catch (AlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PutMapping("product/{id}/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductRequest request, @PathVariable Long id){
        try {
            Product product = iProductService.updateProduct(request, id);
            ProductDto productDto = iProductService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Update success", productDto));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
        }
    }

    @DeleteMapping("product/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id){
        try {
            iProductService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Delete success", null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try {
            List<Product> products = iProductService.getProductsByBrandAndName(brand, name);
            if (products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            List<ProductDto> productDtos = iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Get success", productDtos));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand){
        try {
            List<Product> products = iProductService.getProductsByCategoryAndBrand(category, brand);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            List<ProductDto> productDtos = iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Get success", productDtos));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("by/name")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name){
        try {
            List<Product> products = iProductService.getProductsByName(name);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            List<ProductDto> productDtos = iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Get success", productDtos));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("by/brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand){
        try {
            List<Product> products = iProductService.getProductsByBrand(brand);
            if (products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Get success", products));
            }
            List<ProductDto> productDtos = iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Get success", productDtos));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("by/category")
    public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String category){
        try {
            List<Product> products = iProductService.getProductsByCategory(category);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            List<ProductDto> productDtos = iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Get success", productDtos));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("count/by/brand-and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try {
            var count = iProductService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Quantity:", count.toString()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }







}
