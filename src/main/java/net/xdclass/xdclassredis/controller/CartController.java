package net.xdclass.xdclassredis.controller;

import net.xdclass.xdclassredis.dao.VideoDao;
import net.xdclass.xdclassredis.model.VideoDO;
import net.xdclass.xdclassredis.util.JsonData;
import net.xdclass.xdclassredis.util.JsonUtil;
import net.xdclass.xdclassredis.vo.CartItemVO;
import net.xdclass.xdclassredis.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/cart")
public class CartController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private VideoDao videoDao;

    @RequestMapping("add")
    public JsonData addCart(int videoId,int buyNum){

        //從Redis獲取購物車
        BoundHashOperations<String,Object,Object> myCart = getMyCartOps();

        // 取得此videoId的購物資訊
        Object cacheObj = myCart.get(videoId+"");

        String result = "";
        if(cacheObj!=null){
            result = (String)cacheObj;
        }

        //購物車沒這個商品
        if(cacheObj == null){

            CartItemVO cartItem = new CartItemVO();
            VideoDO videoDO = videoDao.findDetailById(videoId);

            cartItem.setBuyNum(buyNum);
            cartItem.setPrice(videoDO.getPrice());
            cartItem.setProductId(videoDO.getId());
            cartItem.setProductImg(videoDO.getImg());
            cartItem.setProductTitle(videoDO.getTitle());

            // 將Map物件轉換成Json字串後再存入redis
            myCart.put(videoId+"",JsonUtil.objectToJson(cartItem));

        }else { //商品先前已被添加進購物車中
          //增加商品購買數量
            CartItemVO cartItemVO = JsonUtil.jsonToPojo(result,CartItemVO.class);
            cartItemVO.setBuyNum(cartItemVO.getBuyNum()+buyNum);

            myCart.put(videoId+"",JsonUtil.objectToJson(cartItemVO));
        }

        return JsonData.buildSuccess();
    }

    /**
     * 查看我的購物車
     */
    @RequestMapping("mycart")
    public JsonData getMycart(){
        //獲取購物車
        BoundHashOperations<String,Object,Object> myCart = getMyCartOps();

        List<Object> itemList =  myCart.values();

        List<CartItemVO> cartItemVOList = new ArrayList<>();

        for(Object item : itemList){
            CartItemVO cartItemVO = JsonUtil.jsonToPojo((String)item,CartItemVO.class);
            cartItemVOList.add(cartItemVO);
        }

        CartVO cartVO = new CartVO();
        cartVO.setCartItems(cartItemVOList);

        return JsonData.buildSuccess(cartVO);

    }


    /**
     * 清空我的購物車
     * @return
     */
    @RequestMapping("clear")
    public JsonData clear(){

        String key = getCartKey();
        redisTemplate.delete(key);

        return JsonData.buildSuccess();
    }

    /**
     * 抽取當前用戶的購物車的通用方法
     * @return
     */
    private BoundHashOperations<String,Object,Object> getMyCartOps(){

        String key = getCartKey();
        // 返回購物車(Hash資料類型)
        return redisTemplate.boundHashOps(key);
    }


    private String getCartKey(){

        //用戶的id,從攔截器獲取
        int userId = 88;
        String cartKey = String.format("video:cart:%s",userId);
        return cartKey;

    }
}
