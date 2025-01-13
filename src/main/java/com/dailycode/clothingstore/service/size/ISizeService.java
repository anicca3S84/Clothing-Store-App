package com.dailycode.clothingstore.service.size;

import com.dailycode.clothingstore.model.Size;

import java.util.List;

public interface ISizeService {
    Size addSize(Size size);
    Size updateSize(Long id, Size request);
    void deleteSizeById(Long id);
    Size getSizeById(Long id);
    List<Size> getAllSizes();
}
