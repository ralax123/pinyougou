package com.pyg.seckill.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.pyg.pojo.TbSeckillGoods;

public class getRedis {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void get() {
		TbSeckillGoods object = (TbSeckillGoods) redisTemplate.boundHashOps(TbSeckillGoods.class.getClass().getSimpleName()).get(1);
		System.out.println(object);
	}
}
