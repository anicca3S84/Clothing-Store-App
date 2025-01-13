package com.dailycode.clothingstore.repository;

import com.dailycode.clothingstore.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Long> {
    Size findByName(String name);

    boolean existsByName(String name);
}
