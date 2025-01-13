package com.dailycode.clothingstore.service.cart;

import com.dailycode.clothingstore.dto.CartDto;
import com.dailycode.clothingstore.dto.CartItemDto;
import com.dailycode.clothingstore.model.Cart;
import com.dailycode.clothingstore.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User user);
    Cart getCartByUserId(Long userId);
    CartDto convertToDto(Cart cart);
}
