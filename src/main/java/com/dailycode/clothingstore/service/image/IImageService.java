package com.dailycode.clothingstore.service.image;

import com.dailycode.clothingstore.dto.ImageDto;
import com.dailycode.clothingstore.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
    void updateImage(MultipartFile file, Long imageId);
}
