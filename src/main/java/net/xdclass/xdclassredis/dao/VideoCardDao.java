package net.xdclass.xdclassredis.dao;

import net.xdclass.xdclassredis.model.VideoCardDO;
import net.xdclass.xdclassredis.model.VideoDO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class VideoCardDao {


    public List<VideoCardDO> list(){

        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 以下模擬DB的輪播圖列表資料
         */
        List<VideoCardDO> cardDOList = new ArrayList<>();

        VideoCardDO videoCardDO = new VideoCardDO();
        videoCardDO.setId(1);
        videoCardDO.setTitle("熱門視頻");

        VideoDO videoDO1 = new VideoDO(1,"springcloud微服務視頻","xdclass.net",43);
        VideoDO videoDO2 = new VideoDO(2,"Paas工業級項目實戰","xdclass.net",32);
        VideoDO videoDO3 = new VideoDO(3,"面試專題視頻","xdclass.net",43);
        VideoDO videoDO4 = new VideoDO(4,"spring源碼實戰","xdclass.net",4);
        List<VideoDO> videoDOS = new ArrayList<>();
        videoDOS.add(videoDO1);
        videoDOS.add(videoDO2);
        videoDOS.add(videoDO3);
        videoDOS.add(videoDO4);
        videoCardDO.setList(videoDOS);


        VideoCardDO videoCardDO2 = new VideoCardDO();
        videoCardDO2.setId(2);
        videoCardDO2.setTitle("項目實戰視頻");

        VideoDO videoDO5 = new VideoDO(1,"springcloud微服務視頻項目","xdclass.net",43);
        VideoDO videoDO6 = new VideoDO(2,"Paas工業級項目實戰項目","xdclass.net",32);
        VideoDO videoDO7 = new VideoDO(3,"面試專題視頻項目","xdclass.net",43);
        VideoDO videoDO8 = new VideoDO(4,"設計模式視頻","xdclass.net",4);
        List<VideoDO> videoDOS2 = new ArrayList<>();
        videoDOS2.add(videoDO5);
        videoDOS2.add(videoDO6);
        videoDOS2.add(videoDO7);
        videoDOS2.add(videoDO8);
        videoCardDO2.setList(videoDOS2);



        cardDOList.add(videoCardDO);
        cardDOList.add(videoCardDO2);

        return cardDOList;

    }

}
