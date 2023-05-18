package net.xdclass.xdclassredis;

import net.xdclass.xdclassredis.controller.RankController;
import net.xdclass.xdclassredis.model.UserDO;
import net.xdclass.xdclassredis.model.VideoDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

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

	@Test
	public void saveRank(){
		VideoDO video1 = new VideoDO(3,"PaaS工業級微服務大課","xdclass.net",1099);
		VideoDO video2 = new VideoDO(5,"AlibabaCloud全家桶實戰","xdclass.net",59);
		VideoDO video3 = new VideoDO(53,"SpringBoot2.X+Vue3綜合實戰","xdclass.net",49);
		VideoDO video4 = new VideoDO(15,"玩轉23種設計模式+最近實戰","xdclass.net",49);
		VideoDO video5 = new VideoDO(45,"Nginx網關+LVS+KeepAlive","xdclass.net",89);

		/**
		 * LPUSH命令:
		 * 元素是從最左端的到最右端的、一個接一個被插入到list 的頂端。
		 * 所以對於這個命令例子LPUSH mylist a b c，返回的列表是c 為第一個元素， b 為第二個元素， a 為第三個元素。
		 */
		redisTemplate.opsForList().leftPushAll(RankController.DAILY_RANK_KEY,video5,video4,video3,video2,video1);
	}

	/**
	 * 替換榜單第二名
	 */
	@Test
	public void replaceRank(){
		VideoDO video = new VideoDO(5432,"小滴課堂面試專題第一季","xdclass.net",323);
		redisTemplate.opsForList().set(RankController.DAILY_RANK_KEY, 1, video);
	}

	/**
	 * [Set數據結構]用戶畫像標籤去重
	 */
	@Test
	public void userProfile(){
		BoundSetOperations operations = redisTemplate.boundSetOps("user:tags:1");
		// Set類型資料添加
		operations.add("car","student","rich","dog","taipei","rich");

		Set<String> set1 = operations.members();
		System.out.println(set1);

		// Set類型資料移除
		operations.remove("dog");
		Set<String> set2 = operations.members();
		System.out.println(set2);
	}

	/**
	 * 社交應用-關注粉絲列表
	 */
	@Test
	public void testSocial(){
		BoundSetOperations operationsLW = redisTemplate.boundSetOps("user:lw");
		operationsLW.add("A","B","C","D","E");
		System.out.println("老王的粉絲："+operationsLW.members());


		BoundSetOperations operationsXD = redisTemplate.boundSetOps("user:xd");
		operationsXD.add("A","B","F","G","H","K","J","W");
		System.out.println("小d的粉絲："+operationsXD.members());

		//差集
		Set lwSet = operationsLW.diff("user:xd");
		System.out.println("老王的專屬用戶："+lwSet);

		//差集
		Set xdSet = operationsXD.diff("user:lw");
		System.out.println("小D的專屬用戶："+xdSet);


		//交集
		Set interSet = operationsLW.intersect("user:xd");
		System.out.println("同時關注了兩個人的用戶："+interSet);

		//聯集
		Set unionSet = operationsLW.union("user:xd");
		System.out.println("兩個人的並集："+unionSet);


		//判斷A用戶是不是老王的粉絲
		boolean flag = operationsLW.isMember("A");
		System.out.println("A用戶是不是老王的粉絲:"+flag);
	}
}
