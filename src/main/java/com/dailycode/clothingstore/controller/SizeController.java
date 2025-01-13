package com.dailycode.clothingstore.controller;

import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Size;
import com.dailycode.clothingstore.response.ApiResponse;
import com.dailycode.clothingstore.service.size.ISizeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/sizes", produces = "application/json")
public class SizeController {
    private final ISizeService iSizeService;

    public SizeController(ISizeService iSizeService) {
        this.iSizeService = iSizeService;
    }

    @PostMapping("size/add")
    public ResponseEntity<ApiResponse> addSize(@RequestBody Size size){
        try {
            Size newSize = iSizeService.addSize(size);
            return ResponseEntity.ok(new ApiResponse("Add success", newSize));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("size/{id}/update")
    public ResponseEntity<ApiResponse> updateSize( @PathVariable Long id, @RequestBody Size size){
        try {
            Size newSize = iSizeService.updateSize(id, size);
            return ResponseEntity.ok(new ApiResponse("Update success", newSize));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("size/{id}/delete")
    public ResponseEntity<ApiResponse> deleteSize(@PathVariable Long id){
        try {
            iSizeService.deleteSizeById(id);
            return ResponseEntity.ok(new ApiResponse("Delete success", null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("size/{id}")
    public ResponseEntity<ApiResponse> getSizeById(@PathVariable Long id){
        try {
            Size size = iSizeService.getSizeById(id);
            return ResponseEntity.ok(new ApiResponse("Get success", size));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Delete success", null));
        }
    }

    @GetMapping("all")
    public ResponseEntity<ApiResponse> getAllSizes(){
        try {
            List<Size> sizes = iSizeService.getAllSizes();
            return ResponseEntity.ok(new ApiResponse("Get success", sizes));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
