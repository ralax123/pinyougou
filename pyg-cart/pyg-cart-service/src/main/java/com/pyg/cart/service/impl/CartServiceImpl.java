package com.pyg.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pyg.cart.service.CartServcie;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemExample;
import com.pyg.pojo.TbItemExample.Criteria;
import com.pyg.pojo.TbOrderItem;
import com.pyg.vo.Cart;

@Service
public class CartServiceImpl implements CartServcie {

	// 注入redis模板对象,用于用户登录时,购物车保存在redis服务器里
	@Autowired
	private RedisTemplate redisTemplate;

	// 注入商品sku接口代理对象,
	@Autowired
	private TbItemMapper itemMapper;

	/**
	 * 查询reids数据库购物车列表
	 */
	public List<Cart> findRedisCartList(String username) {
		String cartListStirng = (String) redisTemplate.boundHashOps("redis_cart").get(username);
		List<Cart> cartList = JSON.parseArray(cartListStirng, Cart.class);
		// 判断redis购物车是否为空
		if (cartList == null) {
			cartList = new ArrayList<Cart>();
		}
		return cartList;
	}

	/**
	 * 添加购物车步骤 1.判断选中的商品是否存在 2.判断选中的商品是否可用 3.判断购物车里有没有这个商家信息 3-a,有这个商家
	 * 判断以前购物车有么有现在选中的商品? 有--直接改变商品数量 没有--创建这个商品,并添加到这个商家里 3-b,没有这个商家
	 * 直接创建商家,并创建这个商品
	 * 
	 * ②商品删除同样是调用这个方法,只不过是改变商品的数量
	 * 
	 */
	public List<Cart> addCartList(List<Cart> cartList, Long itemId, Integer num) {
		// 1判断选中的商品是存在
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		if (item == null) {
			throw new RuntimeException("商品不存在");
		}
		// 2.判断选中的商品是否可用
		if (!item.getStatus().equals("1")) {
			throw new RuntimeException("商品下架或者删除");
		}
		// 3通过要添加的商品,获得这个商品的商家的id
		String sellerId = item.getSellerId();
		// 4.判断购物车里有没有这个商家信息
		Cart cart = isSameSeller(cartList, sellerId);
		if (cart != null) {// 说明购物车原来就有这个商家
			// 再来判断选中的商品,以前购物车里是否已经有了
			List<TbOrderItem> orderItemList = cart.getOrderItemList();
			TbOrderItem orderItem = isSameOrderItem(orderItemList, itemId);
			if (orderItem != null) {
				// 原来购物车里面就有这个商家的这个商品,只需要改变这个商品的数量即可
				orderItem.setNum(orderItem.getNum() + num);
				// 计算总价格
				orderItem.setTotalFee(new BigDecimal(orderItem.getNum() * orderItem.getPrice().doubleValue()));

				// 删除商品,其实就是改变商品数量
				if (orderItem.getNum() < 1) {
					// 就把这个商品删除了
					cart.getOrderItemList().remove(orderItem);
				}
				// 如果这个商家的所有商品在购物车里都没有了,那么就删除这个商家
				if (cart.getOrderItemList().size() <= 0) {
					cartList.remove(cart);
				}
			} else {// 原来购物车里有这个商家,但是没有这个商品
					// 需要创建这个商品,并添加到这个商家里面
				TbOrderItem newOrderItem = this.creatOrderItem(item, itemId, num);
				orderItemList.add(newOrderItem);

			}

		} else {// 购物车里原来没有这个商家
			// 新建这个cart
			cart = new Cart();
			cart.setSellerId(item.getSellerId());
			cart.setSellName(item.getSeller());
			// 给cart的list赋值
			List<TbOrderItem> orderList = new ArrayList<>();
			// 调用方法,创建一个新的商品
			TbOrderItem newOrderItem = this.creatOrderItem(item, itemId, num);
			orderList.add(newOrderItem);
			cart.setOrderItemList(orderList);

			// 在把创建好的商家对象放到购物车列表里
			cartList.add(cart);
		}
		return cartList;
	}

	/**
	 * 所添加的商品,购物车原来没有它,所以要创建新的,参数传递是1.这个商品,2这个商品的id(和1其实是有关联,这里传递只是方便一点) 3.商品的数量
	 */
	private TbOrderItem creatOrderItem(TbItem item, Long itemId, Integer num) {
		TbOrderItem orderItem = new TbOrderItem();
		orderItem.setItemId(itemId);
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setTitle(item.getTitle());
		// 设置单价
		orderItem.setPrice(item.getPrice());
		// 设置数量
		orderItem.setNum(num);
		// 设置总价
		orderItem.setTotalFee(new BigDecimal(num * item.getPrice().doubleValue()));
		// 设置卖家
		orderItem.setSellerId(item.getSellerId());
		// 设置图片
		orderItem.setPicPath(item.getImage());
		return orderItem;
	}

	// 再来判断选中的商品,以前购物车里是否已经有了
	private TbOrderItem isSameOrderItem(List<TbOrderItem> orderItemList, Long itemId) {
		// TODO Auto-generated method stub
		for (TbOrderItem tbOrderItem : orderItemList) {
			if (tbOrderItem.getItemId() == itemId.longValue()) {
				return tbOrderItem;
			}
		}
		return null;
	}

	// 判断购物车里有没有这个商家信息
	private Cart isSameSeller(List<Cart> cartList, String sellerId) {
		for (Cart cart : cartList) {
			if (cart.getSellerId().equals(sellerId)) {
				return cart;
			}
		}
		return null;
	}

	// ===================================
	/**
	 * 把刚才封装好的购物车参数,添加到redis服务器里面
	 */
	@Override
	public void addRedisCartList(List<Cart> cartList, String username) {
		String cart_list = JSON.toJSONString(cartList);
		redisTemplate.boundHashOps("redis_cart").put(username, cart_list);
		// redisTemplate.boundHashOps("redis_cart").put("aaa", "sdfasdf");
	}

	// 合并购物车
	public List<Cart> mergeCart(List<Cart> cookieCartList, List<Cart> redisCartList) {
		List<Cart> addCartList = new ArrayList<>();
		// 循环cookie里面数据
		for (Cart cart : cookieCartList) {
			// 取出cookie中商品集合
			List<TbOrderItem> orderItemList = cart.getOrderItemList();
			// 循环商品,进行合并
			for (TbOrderItem tbOrderItem : orderItemList) {
				// 调用添加商品的方法
				addCartList = addCartList(redisCartList, tbOrderItem.getItemId(), tbOrderItem.getNum());
			}
		}
		return addCartList;
	}

}
