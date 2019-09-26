package com.pyg.seckill.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.pojo.TbSeckillOrder;
import com.pyg.seckill.service.SeckillOrderService;
import com.pyg.utils.PygResult;

//import entity.Result;
/**
 * controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {

	@Reference
	private SeckillOrderService seckillOrderService;

	/**
	 * 返回全部列表
	 * 
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeckillOrder> findAll() {
		return seckillOrderService.findAll();
	}

	// ==========================================
	/**
	 * 从redis服务器里面查询改商品信息
	 */
	@RequestMapping("showSeckillgood")
	public TbSeckillGoods showSeckillgood(Long id) {
		return seckillOrderService.showSeckillgood(id);
	}

	/**
	 * 抢购成功,创建订单 参数:商品id 返回值:pygresult
	 */
	@RequestMapping("createOrder")
	public PygResult createOrder(Long id) {
		// 获得当前用户名
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if ("anonymousUser".equals(username)) {
			// 禁止匿名下单
			return new PygResult(false, "请你先登录");
		}
		PygResult PygResult = seckillOrderService.createOrder(id, username);
		return PygResult;
	}

}
