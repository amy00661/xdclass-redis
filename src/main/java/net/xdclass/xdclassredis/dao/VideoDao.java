package net.xdclass.xdclassredis.dao;

import net.xdclass.xdclassredis.model.VideoDO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class VideoDao {
    private static Map<Integer, VideoDO> mockMap = new HashMap<>();

    static {
        mockMap.put(1, new VideoDO(1,"工業級PaaS雲平台+SpringCloudAlibaba 綜合項目實戰(完結)","https://xdclass.net",1099));
        mockMap.put(2,new VideoDO(2,"玩轉新版高性能RabbitMQ容器化分佈式集群實戰","https://xdclass.net",79));
        mockMap.put(3,new VideoDO(3,"新版後端提效神器MybatisPlus+SwaggerUI3.X+Lombok","https://xdclass.net",49));
        mockMap.put(4,new VideoDO(4,"玩轉Nginx分佈式架構實戰教程 零基礎到高級","https://xdclass.net",49));
        mockMap.put(5,new VideoDO(5,"ssm新版SpringBoot2.3/spring5/mybatis3","https://xdclass.net",49));
        mockMap.put(6,new VideoDO(6,"新一代微服務全家桶AlibabaCloud+SpringCloud實戰","https://xdclass.net",59));
    }


    /**
     * 模擬從數據庫找
     * @param videoId
     * @return
     */
    public VideoDO findDetailById(int videoId) {
        return mockMap.get(videoId);
    }

}
