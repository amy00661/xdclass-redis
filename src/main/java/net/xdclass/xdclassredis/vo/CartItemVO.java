package net.xdclass.xdclassredis.vo;

public class CartItemVO {
    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 購買數量
     */
    private Integer buyNum;

    /**
     * 商品標題
     */
    private String productTitle;

    /**
     * 圖片
     */
    private String productImg;

    /**
     * 商品單價
     */
    private int price;

    /**
     * 總價格，單價+數量
     */
    private int totalPrice;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    /**
     * 商品單價 * 購買數量
     *
     * @return
     */
    public int getTotalPrice() {

        return this.price * this.buyNum;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
