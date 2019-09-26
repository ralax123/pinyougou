package com.pyg.vo;

import java.io.Serializable;

import com.pyg.pojo.TbAddress;
import com.pyg.pojo.TbOrder;

/**
 * 这是提交订单的封装类, 订单提交需要往三个表里添加数据, 分别是address表,tborder表,tborder-item表
 * 
 * @author ASUS
 *
 */
public class OrderInfo implements Serializable {

	// 地址表
	private TbAddress address;
	// 商家订单表
	private TbOrder order;

	public OrderInfo(TbAddress address, TbOrder order) {
		super();
		this.address = address;
		this.order = order;
	}

	public OrderInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TbAddress getAddress() {
		return address;
	}

	public void setAddress(TbAddress address) {
		this.address = address;
	}

	public TbOrder getOrder() {
		return order;
	}

	public void setOrder(TbOrder order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "OrderInfo [address=" + address + ", order=" + order + "]";
	}

}
