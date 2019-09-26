package com.pyg.pay.service;

import java.util.Map;

public interface PayService {

	/**
	 * 调用微信支付接口,返回微信接口的结果--map 
	 * 参数:一个是支付金额total_fee ,一个是out_trade_no 订单号
	 */
	public Map creatWXPay(String total_fee, String out_trade_no);

	/**
	 * 查询订到号的支付状态
	 * 参数:订单号
	 * 返回值:map对象,里面有success或者closed等信息,根据不同的信息,在做出不同的处理
	 */
	public Map checkPayStatus(String out_trade_no);

}
