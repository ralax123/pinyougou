package com.pyg.cluster.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis-cluster.xml")
public class TestCluster {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	public void testvalue(){
		redisTemplate.boundSetOps("nameset").add("zhangsan");
	}

}
