package net.xdclass.xdclassredis.service;

import net.xdclass.xdclassredis.model.ProductDO;

public interface ProductService {

    int save(ProductDO productDO);


    int delById(int id);


    int updateById(ProductDO productDO);


    ProductDO findById(int id);
}
