package com.dailycode.clothingstore.controller;

import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Color;
import com.dailycode.clothingstore.response.ApiResponse;
import com.dailycode.clothingstore.service.color.IColorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/colors/", produces = "application/json")
public class ColorController {
    private final IColorService iColorService;

    public ColorController(IColorService iColorService) {
        this.iColorService = iColorService;
    }

    @PostMapping("color/add")
    public ResponseEntity<ApiResponse> addColor(@RequestBody Color color){
        try {
            Color newColor = iColorService.addColor(color);
            return ResponseEntity.ok(new ApiResponse("Add success", newColor));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("color/{id}/update")
    public ResponseEntity<ApiResponse> updateColor(@PathVariable Long id, @RequestBody Color color){
        try {
            Color newColor = iColorService.updateColor(id, color);
            return ResponseEntity.ok(new ApiResponse("Update success", newColor));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("color/{id}/delete")
    public ResponseEntity<ApiResponse> deleteColor(@PathVariable Long id){
        try {
            iColorService.deleteColorById(id);
            return ResponseEntity.ok(new ApiResponse("Delete success", null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("color/{id}")
    public ResponseEntity<ApiResponse> getColorById(@PathVariable Long id){
        try {
            Color color = iColorService.getColorById(id);
            return ResponseEntity.ok(new ApiResponse("Get success", color));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("all")
    public ResponseEntity<ApiResponse> getAllColors(){
        try {
            List<Color> colors = iColorService.getAllColors();
            if(colors == null){
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Color not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Get success", colors));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
