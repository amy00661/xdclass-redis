package net.xdclass.xdclassredis.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.xdclass.xdclassredis.dao.ProductMapper;
import net.xdclass.xdclassredis.model.ProductDO;
import net.xdclass.xdclassredis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
    @Cacheable(value = {"product"},key = "#root.args[0]", cacheManager = "cacheManager1Minute")
    public ProductDO findById(int id) {
        return productMapper.selectById(id);
    }

    @Override
    @Cacheable(value = {"product_page"},key = "#root.methodName+'_'+#page+'_'+#size")
    public Map<String, Object> page(int page, int size) {
        Page pageInfo = new Page<>(page,size);

        IPage<ProductDO> iPage = productMapper.selectPage(pageInfo,null);

        Map<String,Object> pageMap = new HashMap<>(3);

        pageMap.put("total_record",iPage.getTotal());   // 總筆數
        pageMap.put("total_page",iPage.getPages());     // 總頁數
        pageMap.put("current_data",iPage.getRecords()); // 指定頁面的多筆ProductDO

        return pageMap;
    }

}
