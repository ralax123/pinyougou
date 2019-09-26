package com.pyg.order.service;

import java.util.List;

import org.springframework.core.annotation.Order;

import com.pyg.pojo.TbAddress;
import com.pyg.pojo.TbOrder;
import com.pyg.utils.PygResult;
import com.pyg.vo.Cart;
import com.pyg.vo.OrderInfo;

/**
 * 服务层接口
 * 
 * @author Administrator
 *
 */
public interface OrderService {

	/**
	 * 查询买家地址列表
	 */
	public List<TbAddress> findAddressList(String username);

	/**
	 * 将购物车里的商品提交到表里--三个表
	 */
	public PygResult submitOrder(OrderInfo order, String username);

}
