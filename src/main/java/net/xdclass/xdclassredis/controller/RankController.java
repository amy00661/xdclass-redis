package net.xdclass.xdclassredis.controller;

import net.xdclass.xdclassredis.model.VideoDO;
import net.xdclass.xdclassredis.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/rank")
public class RankController {

    @Autowired
    RedisTemplate redisTemplate;

    public static final String DAILY_RANK_KEY = "video:rank:daily";

    @RequestMapping("daily_rank")
    public JsonData videoDailyRank() {
        /**
         * 0 是列表裡的第一個元素（表頭），1 是第二個元素，以此類推；
         * 也可以用負數來表示與表尾的偏移量，比如-1 表示列表裡的最後一個元素， -2 表示倒數第二個，等等。
         */
        List<VideoDO> list = redisTemplate.opsForList().range(DAILY_RANK_KEY, 0, -1);    // 取得第1個~最後1個元素
        return JsonData.buildSuccess(list);
    }
}
