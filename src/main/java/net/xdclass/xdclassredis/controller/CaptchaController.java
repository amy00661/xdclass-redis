package net.xdclass.xdclassredis.controller;

import com.google.code.kaptcha.Producer;
import net.xdclass.xdclassredis.util.CommonUtil;
import net.xdclass.xdclassredis.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * 支持手機號、郵箱發送驗證碼
     * @return
     */
    @GetMapping("send_code")
    public JsonData sendCode(@RequestParam(value="to", required=true)String to,
                             @RequestParam(value="captcha", required=true) String captcha,
                             HttpServletRequest request){
        String key = getCaptchaKey(request);
        String cacheCaptcha = redisTemplate.opsForValue().get(key);
        if(captcha!=null && cacheCaptcha!=null && cacheCaptcha.equalsIgnoreCase(captcha)){
            redisTemplate.delete(key);  // 效驗成功，刪除存儲於redis的圖形驗證碼

            //TODO 發送驗證碼

            return JsonData.buildSuccess();
        }else{
            return JsonData.buildError("驗證碼錯誤");
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
