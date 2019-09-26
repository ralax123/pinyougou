package com.pyg.seckill.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbSeckillGoodsMapper;
import com.pyg.mapper.TbSeckillOrderMapper;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbAddressExample.Criteria;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.pojo.TbSeckillOrder;
import com.pyg.pojo.TbSeckillOrderExample;
import com.pyg.seckill.service.SeckillOrderService;
import com.pyg.utils.IdWorker;
import com.pyg.utils.PygResult;
import com.pyg.vo.OrderRecode;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

	@Autowired
	private TbSeckillOrderMapper seckillOrderMapper;

	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillOrder> findAll() {
		return seckillOrderMapper.selectByExample(null);
	}

	/**
	 * 从redis服务器里面查询改商品信息
	 */
	public TbSeckillGoods showSeckillgood(Long id) {
		TbSeckillGoods selectByPrimaryKey = seckillGoodsMapper.selectByPrimaryKey(id);
		return selectByPrimaryKey;
	}

	/**
	 * 抢购成功,创建订单 参数:商品id,买家名字 返回值:pygresult 步骤:1.从redis服务器里面找到这个商品 2.判断商品状态 库存>0
	 * 3.生成这个买家的订单,存入redis服务器里 4.将商品库存数-1,更新到redis服务器里
	 * 5.再次判断商品库存数,如果数量为0,则同步到MySQL数据库,并把redis服务器里这个商品删除
	 */
	@Value("${SECKILL_QUEUE}")
	private String SECKILL_QUEUE;
	
	//用户排队的表示
	@Value("${SECKILL_USER}")
	private String SECKILL_USER;
	
	//秒杀商品抢购人数
	@Value("${SECKILL_COUNT_NUM}")
	private String SECKILL_COUNT_NUM;
	
	public PygResult createOrder(Long id, String username) {
		try {
			
			
			
			//直接从队列里面取出一个数,判断商品存在不存在
			Long goodsId = (Long) redisTemplate.boundListOps(SECKILL_QUEUE+id).rightPop();
			//判断商品是否存在
			if(goodsId ==null){
				//商品卖完了
				throw new RuntimeException("商品售罄");
			}
			
			// 1.从redis服务器里面找到这个商品
			String obj = (String) redisTemplate.boundHashOps("111").get(id + "");
			TbSeckillGoods seckillGoods = JSON.parseObject(obj, TbSeckillGoods.class);
			// 2.判断商品状态 库存>0
			if (seckillGoods == null || seckillGoods.getStockCount() < 1) {
				throw new RuntimeException("商品不可购买");
			}
			
			
			
			
			
			//判断用户是否处于排队状态
			Boolean member = redisTemplate.boundSetOps(SECKILL_USER).isMember(username);
			
			if(member){
				//出入排队状态里
				//查询是否存储订单
				 Object object = redisTemplate.boundHashOps("seckillorder").get(username);
				 if(object != null){
					 //订单不为空
					 throw new RuntimeException("你还有未支付订单");
				 }
				 
				 throw new RuntimeException("正在排队中...");
			}else{
				//队列里没有此用户,需要设置绑定排队信息
				
				//获取这个秒杀商品人数
				Long increment = redisTemplate.boundValueOps(SECKILL_COUNT_NUM+id).increment(0);
				//判断是够超限
				if(increment >=seckillGoods.getStockCount()+200){
					throw new RuntimeException("购买人数超限");
				}
				
				
				//1.把id和username封装到一个类里,ordercode
				OrderRecode orderRecode=new OrderRecode(id, username);
				//放入到redis队列里
				redisTemplate.boundListOps(OrderRecode.class.getClass().getSimpleName()).leftPush(orderRecode);
				//set集合存错username
				redisTemplate.boundSetOps(SECKILL_USER).add(username);
				//设置抢购人数+1
				redisTemplate.boundValueOps(SECKILL_COUNT_NUM+id).increment(1);
				
				//开启多线程,支持两个下单操作
				
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			// 3.生成这个买家的订单--补全订单信息,存入redis服务器里
			TbSeckillOrder seckillOrder = new TbSeckillOrder();
			IdWorker idWorker = new IdWorker();
			seckillOrder.setId(idWorker.nextId());// 订单id
			seckillOrder.setSeckillId(id);// 秒杀商品id
			seckillOrder.setMoney(new BigDecimal(seckillGoods.getStockCount()));// 支付金额
			seckillOrder.setUserId(username);// 买家名
			seckillOrder.setSellerId(seckillGoods.getSellerId());// 卖家名字
			seckillOrder.setCreateTime(new Date());// 创建时间
			seckillOrder.setStatus("0");// 未支付状态
			// 将生成的订单存到redis服务器里
			redisTemplate.boundHashOps("seckillorder").put(username + id, JSON.toJSONString(seckillOrder));
			// 将商品库存数-1
			seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
			// 5.再次判断商品库存数,如果数量为0,则同步到MySQL数据库,并把redis服务器里这个商品删除
			if (seckillGoods.getStockCount() <= 0) {
				seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
				// 删除redis服务器里这个商品
				redisTemplate.boundHashOps("111").delete(id + "");
			} else {
				// 将商品数量-1在同步到redis服务器里
				redisTemplate.boundHashOps("111").put(id + "", JSON.toJSONString(seckillGoods));
			}

			// 生成订单成功
			return new PygResult(true, "订单生成成功");
		} catch (Exception e) {
			e.printStackTrace();
			// 生成订单失败
			return new PygResult(false, "订单生成失败");
		}
	}

}
