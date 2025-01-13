package com.dailycode.clothingstore.controller;

import com.dailycode.clothingstore.dto.CartDto;
import com.dailycode.clothingstore.dto.CartItemDto;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Cart;
import com.dailycode.clothingstore.response.ApiResponse;
import com.dailycode.clothingstore.service.cart.ICartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "api/v1/carts/", produces = "application/json")
public class CartController {
    private final ICartService iCartService;

    public CartController(ICartService iCartService) {
        this.iCartService = iCartService;
    }

    @GetMapping("cart/{id}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long id) {
        try {
            Cart cart = iCartService.getCart(id);
            CartDto cartDto = iCartService.convertToDto(cart);
            return ResponseEntity.ok(new ApiResponse("Get success", cartDto));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("cart/{id}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id) {
        try {
            iCartService.clearCart(id);
            return ResponseEntity.ok(new ApiResponse("Clear all success", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("cart/{id}/total-price")
    public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long id) {
        try {
            BigDecimal totalPrice = iCartService.getTotalPrice(id);
            return ResponseEntity.ok(new ApiResponse("Get success", totalPrice));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("cart/by/{userId}")
    public ResponseEntity<ApiResponse> getCartByUserId(@PathVariable Long userId) {
        try {
            Cart cart = iCartService.getCartByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Get success", cart));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
