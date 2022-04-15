package com.kodilla.ecommercee.controller;

import com.kodilla.ecommercee.Exceptions.CartNotFoundException;
import com.kodilla.ecommercee.domain.Cart;
import com.kodilla.ecommercee.domain.Product;
import com.kodilla.ecommercee.dto.CartDto;
import com.kodilla.ecommercee.dto.ProductDto;
import com.kodilla.ecommercee.mapper.CartMapper;
import com.kodilla.ecommercee.mapper.ProductMapper;
import com.kodilla.ecommercee.service.DbCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final DbCartService service;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCart(@RequestBody CartDto cartDto) {
        Cart cart = cartMapper.mapToCart(cartDto);
        service.saveCart(cart);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long cartId) throws CartNotFoundException {
        return ResponseEntity.ok(cartMapper.mapToCartDto(service.getCartById(cartId)));
    }

    @GetMapping
    public ResponseEntity<List<CartDto>> getAllCarts() {
        List<Cart> carts = service.getAllCarts();
        return ResponseEntity.ok(cartMapper.mapToCartDtoList(carts));
    }

    @PutMapping
    public ResponseEntity<CartDto> editCart(@RequestBody CartDto cartDto) {
        Cart cart = cartMapper.mapToCart(cartDto);
        Cart editedCart = service.saveCart(cart);
        return ResponseEntity.ok(cartMapper.mapToCartDto(editedCart));
    }

    @PutMapping(value = "/add/{cartId}")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable Long cartId, @RequestBody ProductDto productDto) throws CartNotFoundException {
        Cart cart = service.getCartById(cartId);
        Product product = productMapper.mapToProduct(productDto);
        cart.getProducts().add(product);
        Cart updatedCart = service.saveCart(cart);
        return ResponseEntity.ok(cartMapper.mapToCartDto(updatedCart));
    }

    @DeleteMapping(value = "/delete/{cartId}")
    public ResponseEntity<Void> deleteProductFromCart(@PathVariable Long cartId, @RequestParam("item") int item) throws CartNotFoundException {
        Cart cart = service.getCartById(cartId);
        cart.getProducts().remove(item);
        service.saveCart(cart);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) throws CartNotFoundException {
        Cart cart = service.getCartById(cartId);
        List<Product> products = cart.getProducts();
        cart.getProducts().removeAll(products);
        service.saveCart(cart);
        return ResponseEntity.ok().build();
    }
}