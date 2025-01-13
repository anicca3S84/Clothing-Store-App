package com.dailycode.clothingstore.service.cart;

import com.dailycode.clothingstore.dto.CartDto;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Cart;
import com.dailycode.clothingstore.model.User;
import com.dailycode.clothingstore.repository.CartItemRepository;
import com.dailycode.clothingstore.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Slf4j
@Service
public class CartService implements ICartService{


    @Autowired
    private CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;
    private final Logger logger = Logger.getLogger(CartService.class.getName());

    public CartService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }


    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.clearCart();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    @Transactional
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public CartDto convertToDto(Cart cart) {
        return modelMapper.map(cart, CartDto.class) ;
    }


    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
