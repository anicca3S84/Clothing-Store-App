package com.dailycode.clothingstore.service.supplier;

import com.dailycode.clothingstore.model.Supplier;

import java.util.List;

public interface ISupplierService {
    Supplier addSupplier(Supplier supplier);
    Supplier updateSupplier(Long id, Supplier request);
    void deleteSupplierById(Long id);
    Supplier getSupplierById(Long id);
    List<Supplier> getAllSuppliers();
}
