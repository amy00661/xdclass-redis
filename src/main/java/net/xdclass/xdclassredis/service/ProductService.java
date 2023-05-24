package net.xdclass.xdclassredis.service;

import net.xdclass.xdclassredis.model.ProductDO;

import java.util.Map;

public interface ProductService {

    int save(ProductDO productDO);


    int delById(int id);


    ProductDO updateById(ProductDO productDO);


    ProductDO findById(int id);

    /**
     * 商品分頁功能
     * @param page  第幾頁
     * @param size  每頁幾筆資料
     * @return
     */
    Map<String,Object> page(int page, int size);
}
