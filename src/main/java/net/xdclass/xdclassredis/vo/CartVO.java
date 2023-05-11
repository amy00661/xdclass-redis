package net.xdclass.xdclassredis.vo;

import java.util.List;

public class CartVO {

    /**
     * 購物項
     */
    private List<CartItemVO> cartItems;


    /**
     * 購物車總價格
     */
    private Integer totalAmount;



    /**
     * 總價格
     * @return
     */
    public int getTotalAmount() {
        return cartItems.stream().mapToInt(CartItemVO::getTotalPrice).sum();
    }



    public List<CartItemVO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemVO> cartItems) {
        this.cartItems = cartItems;
    }
}
