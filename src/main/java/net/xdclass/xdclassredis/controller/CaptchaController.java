package net.xdclass.xdclassredis.controller;

import com.google.code.kaptcha.Producer;
import net.xdclass.xdclassredis.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/captcha")
public class CaptchaController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Producer captchaProducer;   // 根據名稱注入@Qualifier("captchaProducer")

    /**
     * 獲取圖形驗證碼
     * @param request
     * @param response
     */
    @GetMapping("get_captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response){

        //1. 隨機生成Kaptcha圖形驗證碼
        String captchaText = captchaProducer.createText();

        //2. 使用Redis存儲，並限制10分鐘到期
        String key = getCaptchaKey(request);
        redisTemplate.opsForValue().set(key, captchaText,10, TimeUnit.MINUTES);

        // 3. 返回圖形驗證碼圖檔
        // 取得圖形驗證碼圖檔
        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);

        ServletOutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage,"jpg",outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 獲取緩存的key
     * @param request
     * @return
     */
    private String getCaptchaKey(HttpServletRequest request){
        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");
        // 避免相同ip下多台電腦同時請求，key值使用(ip+userAgent)作為識別
        // 避免key長度過長，將(ip+userAgent)使用MD5轉換
        String key = "user-service:captcha:"+CommonUtil.MD5(ip+userAgent);
        return key;
    }
}
