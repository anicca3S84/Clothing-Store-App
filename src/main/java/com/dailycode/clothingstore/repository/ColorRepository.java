package com.dailycode.clothingstore.repository;

import com.dailycode.clothingstore.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {
    boolean existsByName(String name);

    Color findByName(String name);
}
