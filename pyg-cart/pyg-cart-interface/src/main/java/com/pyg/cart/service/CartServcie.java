package com.pyg.cart.service;

import java.util.List;

import com.pyg.vo.Cart;

public interface CartServcie {

	/**
	 * 查询reids数据库购物车列表
	 */
	public List<Cart> findRedisCartList(String username);

	/**
	 * 商品添加,返回值是添加后的购物车集合
	 */
	List<Cart> addCartList(List<Cart> cartList, Long itemId, Integer num);

	/**
	 * 把封装好的商品数据添加到redi购物车服务器里
	 */
	void addRedisCartList(List<Cart> cartList, String username);

	// 合并购物车
	public List<Cart> mergeCart(List<Cart> cookieCartList, List<Cart> redisCartList);

}
