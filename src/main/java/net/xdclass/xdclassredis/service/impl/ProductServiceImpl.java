package net.xdclass.xdclassredis.service.impl;

import net.xdclass.xdclassredis.dao.ProductMapper;
import net.xdclass.xdclassredis.model.ProductDO;
import net.xdclass.xdclassredis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;


    @Override
    public int save(ProductDO productDO) {
        return productMapper.insert(productDO);
    }

    @Override
    public int delById(int id) {
        return productMapper.deleteById(id);
    }

    @Override
    public int updateById(ProductDO productDO) {
        return productMapper.updateById(productDO);
    }

    @Override
    public ProductDO findById(int id) {
        return productMapper.selectById(id);
    }

}
