package com.dailycode.clothingstore.service.color;

import com.dailycode.clothingstore.exceptions.AlreadyExistException;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Color;
import com.dailycode.clothingstore.repository.ColorRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ColorService implements IColorService {
    private final ColorRepository colorRepository;

    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public Color addColor(Color color) {
        return Optional.of(color).filter(c -> !colorRepository.existsByName(c.getName()))
                .map(colorRepository::save)
                .orElseThrow(() -> new AlreadyExistException("Color + " + color.getName() + " is already exists"));
    }

    @Override
    public Color updateColor(Long id, Color color) {
        return Optional.ofNullable(getColorById(id)).map(oldColor -> {
            oldColor.setName(color.getName());
            oldColor.setImageUrl(color.getImageUrl());
            return colorRepository.save(oldColor);
        }).orElseThrow(() -> new NotFoundException("Color not found"));
    }

    @Override
    public void deleteColorById(Long id) {
        colorRepository.findById(id).ifPresentOrElse(colorRepository::delete, () -> {
            throw new NotFoundException("Color not found");
        });
    }

    @Override
    public Color getColorById(Long id) {
        return colorRepository.findById(id).orElseThrow(() -> new NotFoundException("Color not found"));
    }

    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }
}
