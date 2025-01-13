package com.dailycode.clothingstore.service.size;

import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Size;
import com.dailycode.clothingstore.repository.SizeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeService implements ISizeService{

    private final SizeRepository sizeRepository;

    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Override
    public Size addSize(Size size) {
        return Optional.of(size).filter(s -> !sizeRepository.existsByName(size.getName()))
                .map(sizeRepository::save)
                .orElseThrow(() -> new NotFoundException(size.getName() + " already exists"));
    }

    @Override
    public Size updateSize(Long id, Size request) {
        return Optional.ofNullable(getSizeById(id)).map(oldSize -> {
            oldSize.setName(request.getName());
            return sizeRepository.save(oldSize);
        }).orElseThrow(() -> new NotFoundException("Size not found"));
    }

    @Override
    public void deleteSizeById(Long id) {
        sizeRepository.findById(id).ifPresentOrElse(sizeRepository::delete, () -> {
            throw new NotFoundException("Size not found");
        });
    }

    @Override
    public Size getSizeById(Long id) {
        return sizeRepository.findById(id).orElseThrow(() -> new NotFoundException("Size not found"));
    }

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }
}
