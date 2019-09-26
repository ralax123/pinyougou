package com.pyg.seckill.service;

import java.util.List;

import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.pojo.TbSeckillOrder;
import com.pyg.utils.PygResult;

/**
 * 服务层接口
 * 
 * @author Administrator
 *
 */
public interface SeckillOrderService {

	/**
	 * 返回全部列表
	 * 
	 * @return
	 */
	public List<TbSeckillOrder> findAll();


	/**
	 * 从redis服务器里面查询改商品信息
	 */
	public TbSeckillGoods showSeckillgood(Long id);


	/**
	 * 抢购成功,创建订单
	 * 参数:商品id,买家名字
	 * 返回值:pygresult
	 */
	public PygResult createOrder(Long id, String username);


}
