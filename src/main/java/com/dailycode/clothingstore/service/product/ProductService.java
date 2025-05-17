package com.dailycode.clothingstore.service.product;
import com.dailycode.clothingstore.dto.ImageDto;
import com.dailycode.clothingstore.dto.ProductDto;
import com.dailycode.clothingstore.exceptions.AlreadyExistException;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.*;
import com.dailycode.clothingstore.repository.*;
import com.dailycode.clothingstore.request.ProductRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;


    public ProductService(ProductRepository productRepository, ModelMapper modelMapper, ImageRepository imageRepository, CategoryRepository categoryRepository, ColorRepository colorRepository, SizeRepository sizeRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
    }


    @Override
    public Product addProduct(ProductRequest request) {

        if (productRepository.existsByNameAndBrand(request.getName(), request.getBrand())) {
            throw new AlreadyExistException("Product with name " + request.getName() + " already exists");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(ProductRequest request, Category category){
        List<Color> listColor = new ArrayList<>();
        for(Color item : request.getColors()){
            Color newColor = Optional.ofNullable(colorRepository.findByName(item.getName()))
                    .orElseGet(() -> {
                        Color color = new Color(item.getName(), item.getImageUrl());
                        return colorRepository.save(color);
                    });
            listColor.add(newColor);
        }

        List<Size> listSize = new ArrayList<>();
        for(Size item : request.getSizes()){
            Size newSize = Optional.ofNullable(sizeRepository.findByName(item.getName()))
                    .orElseGet(() -> {
                        Size size = new Size(item.getName());
                        return sizeRepository.save(size);
                    });
            listSize.add(newSize);
        }
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category,
                listSize,
                listColor
        );
    }

    @Override
    public Product updateProduct(ProductRequest request, Long id) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        return productRepository.findById(id)
                .map(existingProduct -> updateExistingProduct(existingProduct, request, category))
                .map(productRepository::save)
                .orElseThrow(() -> new NotFoundException("Product not found")) ;
    }

    private Product updateExistingProduct(Product existingProduct, ProductRequest request, Category category){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(
                productRepository::delete, () -> {
                    throw new NotFoundException("Product not found");
                }
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }

}
