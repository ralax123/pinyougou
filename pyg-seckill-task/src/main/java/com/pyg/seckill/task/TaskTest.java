package com.pyg.seckill.task;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbSeckillGoodsMapper;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.pojo.TbSeckillGoodsExample;
import com.pyg.pojo.TbSeckillGoodsExample.Criteria;

@Component
public class TaskTest {
	// 注入redis数据库
	@Autowired
	private RedisTemplate redisTemplate;
	// 注入tbseckillgodsmapper 接口代理对象
	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	
	@Value("${SECKILL_QUEUE}")
	private String SECKILL_QUEUE;

	/**
	 * 只要这个服务启动,这个类就会根据时间设置的间隔,不断的执行 从而到达定时功能
	 * 
	 */
	@Scheduled(cron = "30 * * * * *")
	public void test1() {
		// 完成吧数据库秒杀商品同步到redis数据库里
		TbSeckillGoodsExample example = new TbSeckillGoodsExample();
		Criteria criteria = example.createCriteria();
		// 条件一:审核通过的
		criteria.andStatusEqualTo("1");
		//库存>0
		criteria.andStockCountGreaterThan(0);
		// 条件2:秒杀抢购开始时间要比现在时间大
		Date date = new Date();
		criteria.andStartTimeGreaterThan(date);
		// 条件三:秒杀结束时间要大于现在时间
		criteria.andEndTimeGreaterThan(date);
		// 查询出这样商品
		List<TbSeckillGoods> seckillgoodsList = seckillGoodsMapper.selectByExample(example);
		// 把秒杀商品数据循环添加到redis服务器里
		for (TbSeckillGoods tbSeckillGoods : seckillgoodsList) {
//			Object json = JSON.toJSON(tbSeckillGoods);
//			String jsonString = JSON.toJSONString(tbSeckillGoods);
			redisTemplate.boundHashOps(TbSeckillGoods.class.getClass().getSimpleName()).put(tbSeckillGoods.getId()+"", tbSeckillGoods);
			//给每个商品添加一个redis队列,队列名是一个固定字符串加商品id, 队列长度就是
			//该商品的库存数,队列里存储的值可以都是商品id
			
			//获取商品的库存数
			for(int i =0;i<tbSeckillGoods.getStockCount();i++){
				redisTemplate.boundListOps(SECKILL_QUEUE+tbSeckillGoods.getId()).leftPush(tbSeckillGoods.getId());
			}
		}
		System.out.println("同步redis服务器成功过");

	}
	
	
	
	@Test
	public void geht(){
		TbSeckillGoods object = (TbSeckillGoods) redisTemplate.boundHashOps(TbSeckillGoods.class.getClass().getSimpleName()).get("1");
		System.out.println(object);
	}
	
	
	
	
	
	
	
	
}
