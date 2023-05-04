package net.xdclass.xdclassredis.controller;

import net.xdclass.xdclassredis.model.VideoCardDO;
import net.xdclass.xdclassredis.service.VideoCardService;
import net.xdclass.xdclassredis.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequestMapping("/api/v1/card")
@RestController
public class VideoCardController {

    @Autowired
    private VideoCardService videoCardService;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String VIDEO_CARD_CACHE_KEY= "video:card:key";

    /**
     * 有實現Redis缓存
     * @return
     */
    @GetMapping("list_cache")
    public JsonData listCardCache(){
        // 檢查輪播圖列表緩存是否已存在
        Object cacheObj = redisTemplate.opsForValue().get(VIDEO_CARD_CACHE_KEY);
        // 再次訪問則命中緩存
        if(cacheObj!= null){
            List<VideoCardDO> list = (List<VideoCardDO>)cacheObj;
            return JsonData.buildSuccess(list);
        }else{
            /**
             * 緩存不存在則查詢數據庫
             * 查詢結果放到緩存，並設置過期時間
             */
            List<VideoCardDO> list = videoCardService.list();
            redisTemplate.opsForValue().set(VIDEO_CARD_CACHE_KEY, list, 10, TimeUnit.MINUTES);
            return JsonData.buildSuccess(list);
        }
    }


    /**
     * 無緩存
     * @return
     */
    @GetMapping("list_nocache")
    public JsonData listCardNoCache(){


        List<VideoCardDO> list = videoCardService.list();

        return JsonData.buildSuccess(list);

    }
}
