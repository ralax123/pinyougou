package com.jedis.demo;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-redis.xml")
public class SpringDataRedisDemo {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void strKv() {
		redisTemplate.boundValueOps("aaa".toString()).set("AAA".toString());
		redisTemplate.boundValueOps("bbb".toString()).set("12334".toString());

	}

	@Test
	public void testHash() {
		Student s = new Student("张三sss", "上海heima");
		redisTemplate.boundHashOps("student").put(1 ,s);
	}

	@Test
	public void findStudent() {
//		String object = (String) redisTemplate.boundHashOps("student").get("1");
//		System.out.println(object);
//		System.out.println("===========================");
//		Student parseObject = JSON.parseObject(object, Student.class);
//		System.out.println(parseObject);
		Student object = (Student) redisTemplate.boundHashOps("student").get(1);
		System.out.println("============="+object.getName());
	}
	@Test
	public void findStudent111() {
		System.out.println("===========================");
		Student parseObject = JSON.parseObject("{name='张三', address='上海heima'}", Student.class);
		System.out.println(parseObject);
	}

	/**
	 * hash类型
	 * 
	 */
	@Test
	public void strHash() {
		redisTemplate.boundHashOps("user1").put(3, 11);
		redisTemplate.boundHashOps("user1").put("username", "沙和尚sadf");
		redisTemplate.boundHashOps("user1").put("address", "流沙河");

		// 获取值
		List list = redisTemplate.boundHashOps("user1").values();
		String name = (java.lang.String) redisTemplate.boundHashOps("user1").get("username");

		System.out.println(list);
		System.out.println(name);

	}

}
