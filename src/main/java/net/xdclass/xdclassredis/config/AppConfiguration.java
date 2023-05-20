package net.xdclass.xdclassredis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.Duration;

@Configuration
public class AppConfiguration {


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

    // ================================= MybatisPlus配置 =================================
    /**
     * 新的分頁插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }


    // ================================= SpringCache配置 =================================
    /**
     * 1分鐘過期
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager1Minute(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = instanceConfig(60L);
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }


    /**
     * 默認是1小時
     * @param connectionFactory
     * @return
     */
    @Bean
    @Primary  // @Cacheable如未指定，則默認緩存期限1小時
    public RedisCacheManager cacheManager1Hour(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = instanceConfig(3600L);
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

    /**
     * 1天過期
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager1Day(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration config = instanceConfig(3600 * 24L);
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

    private RedisCacheConfiguration instanceConfig(Long ttl) {

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        // 去掉各種@JsonSerialize註解的解析
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        // 只針對非空的值進行序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 將類型序列化到屬性json字符串中
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(ttl))  // 指定配置過期時間以「秒」為單位
                //.disableCachingNullValues()   // 禁止緩存NULL值
                                    // 使用Jackson2JsonRedisSerialize 替換默認的Jdk序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
    }

    /**
     * 自定義緩存key的命名規則
     * @return
     */
    @Bean
    public KeyGenerator springCacheCustomKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                String key = o.getClass().getSimpleName() + "_" + method.getName() + "_" + StringUtils.arrayToDelimitedString(objects, "_");
                System.out.println(key);
                return key;
            }
        };
    }
}
