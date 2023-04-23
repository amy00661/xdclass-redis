package net.xdclass.xdclassredis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfiguration {


    /**
     * @param redisConnectionFactory
     * @return
     */
    @Bean   // 宣告成1個@Bean，返回客製的RedisTemplate
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替換默認的Jdk序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);  // 設定Jackson轉換器

        // 設置key和value的序列化規則
        redisTemplate.setKeySerializer(new StringRedisSerializer());    // KEY由原先的 JDK序列化 修改為 字符串序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);  // VALUE由原先的 JDK序列化 修改為 jackson序列化器

        // 設置hashKey和hashValue的序列化規則
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());    // 修改KEY序列化器 (同上)
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);  // 修改VALUE序列化器 (同上)

        // 設置開啟支持事務 (一般不需要，先關閉)
        //redisTemplate.setEnableTransactionSupport(true);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
