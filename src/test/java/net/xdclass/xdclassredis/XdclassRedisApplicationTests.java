package net.xdclass.xdclassredis;

import net.xdclass.xdclassredis.model.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class XdclassRedisApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	void testStringSet() {

		//ValueOperations valueOperations = redisTemplate.opsForValue();
		redisTemplate.opsForValue().set("name","xdclass.net");

		stringRedisTemplate.opsForValue().set("str_name","xdclass.net+str_name");


	}

	@Test
	void testStringGet() {

		//ValueOperations valueOperations = redisTemplate.opsForValue();

		String str1 = (String)redisTemplate.opsForValue().get("name");
		System.out.println(str1);

		String str2 = stringRedisTemplate.opsForValue().get("str_name");
		System.out.println(str2);

	}

	@Test
	public void testSeria(){

		UserDO userDO = new UserDO();
		userDO.setId(1);
		userDO.setName("二當家小D");
		userDO.setPwd("123");

		redisTemplate.opsForValue().set("user-service:user:1",userDO);

	}
}
