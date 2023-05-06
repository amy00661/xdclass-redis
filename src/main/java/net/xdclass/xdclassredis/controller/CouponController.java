package net.xdclass.xdclassredis.controller;

import net.xdclass.xdclassredis.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 原生分佈式鎖開始
     * 1、原子加鎖設置過期時間，防止宕機死鎖
     * 2、原子解鎖：需要判斷是不是自己的鎖
     * @param couponId 搶領的優惠券Id
     * @return
     */
    @GetMapping("add")
    public JsonData saveCoupon(@RequestParam(value = "coupon_id",required = true) int couponId){
        // 用於識別線程，防止線程誤刪彼此加的鎖
        String uuid = UUID.randomUUID().toString().split("-")[0];

        lock(uuid, couponId);

        return JsonData.buildSuccess();
    }

    private void lock(String uuid, int couponId){
        String lockKey = "lock:coupon:" + couponId;

        // lua腳本
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        Boolean nativeLock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, uuid, Duration.ofSeconds(30));
        System.out.println(uuid + "加鎖狀態：" + nativeLock);
        if(nativeLock){
            // 加鎖成功
            try {
                // TODO 搶優惠券業務邏輯
                TimeUnit.SECONDS.sleep(15L);
            } catch (InterruptedException e) {

            } finally{
                // 解鎖
                Long result = stringRedisTemplate.execute(new DefaultRedisScript<>(script,Long.class), Arrays.asList(lockKey), uuid);
                System.out.println(uuid + "解鎖狀態：" + result);
            }
        }else{
            // 遞迴操作
            try {
                System.out.println(uuid+"加鎖失敗，睡眠5秒，進入遞迴操作");
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {}
            // 睡眠一會再嘗試獲取鎖
            lock(uuid, couponId);
        }
    }
}
