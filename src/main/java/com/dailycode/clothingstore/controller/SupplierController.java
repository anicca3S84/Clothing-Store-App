package com.dailycode.clothingstore.controller;

import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Supplier;
import com.dailycode.clothingstore.response.ApiResponse;
import com.dailycode.clothingstore.service.supplier.ISupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/suppliers", produces = "application/json")
public class SupplierController {
    private final ISupplierService iSupplierService;

    public SupplierController(ISupplierService iSupplierService) {
        this.iSupplierService = iSupplierService;
    }

    @PostMapping("supplier/add")
    public ResponseEntity<ApiResponse> addSupplier(@RequestBody Supplier supplier) {
        try {
            Supplier newSupplier = iSupplierService.addSupplier(supplier);
            return ResponseEntity.ok(new ApiResponse("Add Success", newSupplier));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("supplier/{id}/update")
    public ResponseEntity<ApiResponse> updateSupplier(@RequestBody Supplier supplier, @PathVariable Long id) {
        try {
            Supplier newSupplier = iSupplierService.updateSupplier(id, supplier);
            return ResponseEntity.ok(new ApiResponse("Update success", newSupplier));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("supplier/{id}/delete")
    public ResponseEntity<ApiResponse> deleteSupplier(@PathVariable Long id) {
        try {
            iSupplierService.deleteSupplierById(id);
            return ResponseEntity.ok(new ApiResponse("Delete success", null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("supplier/{id}")
    public ResponseEntity<ApiResponse> getSupplierById(@PathVariable Long id) {
        try {
            Supplier supplier = iSupplierService.getSupplierById(id);
            return ResponseEntity.ok(new ApiResponse("Get success", supplier));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("all")
    public ResponseEntity<ApiResponse> getAllSuppliers() {
        try {
            List<Supplier> suppliers = iSupplierService.getAllSuppliers();
            return ResponseEntity.ok(new ApiResponse("Get success", suppliers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
