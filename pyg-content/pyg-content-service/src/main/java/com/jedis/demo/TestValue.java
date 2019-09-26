package com.jedis.demo;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestValue {

	@Test
	public void demo() {
		// 创建jedis客户端对象
		Jedis jedis = new Jedis("192.168.25.104", 6379);
		jedis.set("heima", "黑马");
		String value = jedis.get("heima");
		System.out.println(value);

	}

	@Test
	public void demo2() {
		// 创建连接池对象
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接池
		config.setMaxTotal(3000);
		// 最大闲数
		config.setMaxIdle(300);

		// 创建连接池对象
		JedisPool pool = new JedisPool(config, "192.168.25.104", 6379);

		// 从连接池里面娶一个redis对象
		Jedis jedis = pool.getResource();

		System.out.println(jedis.get("shm"));
	}
}
