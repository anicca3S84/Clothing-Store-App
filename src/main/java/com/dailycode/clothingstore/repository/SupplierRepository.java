package com.dailycode.clothingstore.repository;

import com.dailycode.clothingstore.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByName(String name);
}
