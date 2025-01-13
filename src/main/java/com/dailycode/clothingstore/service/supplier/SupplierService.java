package com.dailycode.clothingstore.service.supplier;

import com.dailycode.clothingstore.exceptions.AlreadyExistException;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Supplier;
import com.dailycode.clothingstore.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService implements ISupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier addSupplier(Supplier supplier) {
        return Optional.of(supplier).filter(s -> !supplierRepository.existsByName(s.getName()))
                .map(supplierRepository::save)
                .orElseThrow(() -> new AlreadyExistException(supplier.getName() + " already exists"));
    }

    @Override
    public Supplier updateSupplier(Long id, Supplier request) {
        return Optional.ofNullable(getSupplierById(id)).map(oldSupplier -> {
            oldSupplier.setName(request.getName());
            oldSupplier.setAddress(request.getAddress());
            oldSupplier.setEmail(request.getEmail());
            oldSupplier.setPhone(request.getPhone());
            oldSupplier.setTax(request.getTax());
            return supplierRepository.save(oldSupplier);
        }).orElseThrow(() -> new NotFoundException("Supplier not found"));
    }

    @Override
    public void deleteSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        if(!supplier.getProducts().isEmpty()){
            throw new IllegalStateException("Cannot delete supplier. Please delete all product related first");
        }
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("Supplier not found"));
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
}
