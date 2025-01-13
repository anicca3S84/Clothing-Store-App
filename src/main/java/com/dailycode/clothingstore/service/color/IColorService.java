package com.dailycode.clothingstore.service.color;

import com.dailycode.clothingstore.model.Color;

import java.util.List;

public interface IColorService {
    Color addColor(Color color);
    Color updateColor(Long id, Color color);
    void  deleteColorById(Long id);
    Color getColorById(Long id);
    List<Color> getAllColors();
}
