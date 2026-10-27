package service;

import discount.*;
import exception.InsufficientStockException;
import exception.ProductNotFoundException;
import model.Cart;

public class CartService {
    private final ProductService productService;

    public CartService(ProductService productService) { this.productService = productService; }

    public void add(Cart cart, int productId, int qty)
            throws ProductNotFoundException, InsufficientStockException {
        cart.addItem(productService.getProduct(productId), qty);
    }

    public void remove(Cart cart, int productId) throws ProductNotFoundException { cart.removeItem(productId); }

    public void updateQuantity(Cart cart, int productId, int qty)
            throws ProductNotFoundException, InsufficientStockException {
        cart.updateQuantity(productId, qty);
    }

    /** Coupons: SAVE10 = 10% off, FLAT500 = Rs.500 off on carts of Rs.2000 or more. */
    public boolean applyCoupon(Cart cart, String code) {
        switch (code.trim().toUpperCase()) {
            case "SAVE10":  cart.setDiscount(new PercentageDiscount(10)); return true;
            case "FLAT500": cart.setDiscount(new FlatDiscount(500, 2000)); return true;
            default: return false;
        }
    }
}
