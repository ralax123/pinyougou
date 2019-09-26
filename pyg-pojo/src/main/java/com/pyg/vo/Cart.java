package com.pyg.vo;

import java.io.Serializable;
import java.util.List;

import com.pyg.pojo.TbOrderItem;

public class Cart implements Serializable {
	/**
	 * 购物车封装的实体类 参数1.商家id 2商家姓名 3商家的商品
	 */
	private String sellerId;

	private String sellName;

	private List<TbOrderItem> orderItemList;

	public Cart(String sellerId, String sellName, List<TbOrderItem> orderItemList) {
		super();
		this.sellerId = sellerId;
		this.sellName = sellName;
		this.orderItemList = orderItemList;
	}

	public Cart() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellName() {
		return sellName;
	}

	public void setSellName(String sellName) {
		this.sellName = sellName;
	}

	public List<TbOrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<TbOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	@Override
	public String toString() {
		return "Cart [sellerId=" + sellerId + ", sellName=" + sellName + ", orderItemList=" + orderItemList + "]";
	}

}
