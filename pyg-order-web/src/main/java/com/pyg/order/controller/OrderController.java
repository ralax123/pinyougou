package com.pyg.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.cart.service.CartServcie;
import com.pyg.order.service.OrderService;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbAddress;
import com.pyg.pojo.TbOrder;
import com.pyg.utils.PygResult;
import com.pyg.vo.Cart;
import com.pyg.vo.OrderInfo;

//import entity.Result;
/**
 * controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/order")
public class OrderController {

	@Reference(timeout = 1000000)
	private OrderService orderService;

	/**
	 * 查询买家地址列表
	 */
	@RequestMapping("/findAddressList")
	public List<TbAddress> findAddressList() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return orderService.findAddressList(username);
	}

	// 查询redis购物车清单--指定用户
	// 注入cartservice接口
	@Reference(timeout = 1000000)
	private CartServcie cartService;

	@RequestMapping("findCartList")
	public List<Cart> findCartList() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Cart> cartList = cartService.findRedisCartList(username);
		return cartList;
	}

	/**
	 * 将购物车里的商品提交到表里--三个表--还要把这个用户名字传过去
	 */
	@RequestMapping("submitOrder")
	public PygResult submitOrder(@RequestBody OrderInfo order) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return orderService.submitOrder(order, username);
	}

}
