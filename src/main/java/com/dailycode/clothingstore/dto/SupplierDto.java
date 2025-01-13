package com.dailycode.clothingstore.dto;

import lombok.Data;

@Data
public class SupplierDto {
    private Long id;
    private String name;

    public SupplierDto() {
    }

    public SupplierDto(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
