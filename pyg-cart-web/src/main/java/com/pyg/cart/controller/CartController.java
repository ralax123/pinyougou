package com.pyg.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pyg.cart.service.CartServcie;
import com.pyg.utils.CookieUtil;
import com.pyg.utils.PygResult;
import com.pyg.vo.Cart;

@RestController
@RequestMapping("/cart")
public class CartController {

	// 注入cartservic
	@Reference(timeout = 1000000)
	private CartServcie cartService;

	/**
	 * 商品添加,返回值是添加后的购物车集合
	 */
	@RequestMapping("addGoodsToCartList")
	@CrossOrigin(origins = "http://pinyougou.com")
	/**
	 * 添加购物车,首先判断查看原来购物车列表 1.未登录的时候,查看cookie购物车 2.登录,查询redis购物车
	 *
	 * 得到原来的购物车信息后,在原来的基础上,继续追加购物车信息 1.获取用户登录信息,判断是否登录 2.登录,添加的是redis购物车
	 * 3.未登录,添加的是coolie购物车
	 */
	public PygResult addGoodsToCartList(HttpServletRequest request, HttpServletResponse response, Long itemId,
			Integer num) {
		try {
			// 查看用户登录状态
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			// 先查看购物车信息-原始数据
			List<Cart> cartList = this.findCartList(request, response);
			// 根据查询的购物车信息,再次追加购物车信息
			cartList = cartService.addCartList(cartList, itemId, num);
			// 根据用户登录状态,往不同的域里存储购物车
			if ("anonymousUser".equals(username)) {
				// 未登录,保存cookie里面
				CookieUtil.setCookie(request, response, "cookie_cart", JSON.toJSONString(cartList), 10000000, true);
			} else {
				// 登录,保存在redis购物车里
				cartService.addRedisCartList(cartList, username);
			}
			// 添加成功
			return new PygResult(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "添加失败");
		}

	}

	/**
	 * 需求：查询购物车列表 未登录--查询cookie购物车 登录---查询redis购物车
	 * 
	 * @return
	 */
	@RequestMapping("findCartList")
	private List<Cart> findCartList(HttpServletRequest request, HttpServletResponse response) {
		// 获取用户登录信息--未登录时,返回的值是anonymousUser
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// 未登录,查看cookie购物车
		String cookiecartJson = CookieUtil.getCookieValue(request, "cookie_cart", true);
		// 判断购物车是否存值
		if (StringUtils.isBlank(cookiecartJson)) {
			cookiecartJson = "[]";
		}
		// 把json字符串转变为list集合
		List<Cart> cookieCartList = JSON.parseArray(cookiecartJson, Cart.class);

		if ("anonymousUser".equals(username)) {
			// 返回cookie购物车
			return cookieCartList;
		} else {
			// 登录了,调用service里面的方法,返回redis查询结果---并且要合并cookie里面的数据

			List<Cart> redisCartList = cartService.findRedisCartList(username);
			// 判断cookie购物车里有没有商品
			if (cookieCartList.size() > 0) {
				// 合并购物车,把合并的结果赋值给redis购物车里
				redisCartList = cartService.mergeCart(cookieCartList, redisCartList);
				// 把购物车列表添加到redis购物车里面
				cartService.addRedisCartList(redisCartList, username);
				// 清空cookie购物车
				CookieUtil.deleteCookie(request, response, "cookie_cart");

			}
			// 返回登录状态购物车列表
			return redisCartList;
		}
	}

	// 显示当前登录用户名字
	@RequestMapping("showUsername")
	public String showUsername() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return username;
	}

}
