package com.pyg.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pyg.pay.service.PayService;
import com.pyg.utils.HttpClient;

@Service
public class PayServiceImpl implements PayService {

	// 获得配置文件里的参数
	@Value("${appid}")
	private String appid;
	@Value("${partner}")
	// 商户号
	private String partner;
	@Value("${partnerkey}")
	private String partnerkey;
	@Value("${notifyurl}")
	private String notifyurl;

	/**
	 * 调用微信支付接口,返回微信接口的结果--map 参数:一个是支付金额total_fee ,一个是out_trade_no 订单号
	 * 
	 * @throws Exception
	 */
	public Map creatWXPay(String total_fee, String out_trade_no) {
		// TODO Auto-generated method stub
		Map map = new HashMap<>();
		// 公众号id
		map.put("appid", appid);
		// 商户号
		map.put("mch_id", partner);
		// 随机字符串
		map.put("nonce_str", WXPayUtil.generateNonceStr());

		// 商品描述
		map.put("body", "快快付款");
		// 订单号
		map.put("out_trade_no", out_trade_no);
		// 支付金额
		map.put("total_fee", total_fee);
		// 终端ip
		map.put("spbill_create_ip", "127.0.0.1");
		// 通知地址--随便写
		map.put("notify_url", "www.itcast.com");
		// 交易类型
		map.put("trade_type", "NATIVE");

		try {
			// 刚才把要调用微信支付接口的参数都封装好了,现在要模拟发送 了
			// 把map对象转换为微信接口需要的xml对象
			String xmlParam = WXPayUtil.generateSignedXml(map, partnerkey);
			// 使用httpclient工具发送请求
			HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			client.setHttps(true);
			client.setXmlParam(xmlParam);
			client.post();
			// 获得请求微信支付后的结果
			String result = client.getContent();
			System.out.println(result);
			// 把结果字符串转换为map集合,resultMap里面有很多数据,我们只有code_url
			Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
			// 创建一个新map,封装这个方法的返回值
			Map map2 = new HashMap<>();
			// 封装二维码连接地址
			map2.put("code_url", resultMap.get("code_url"));
			// 封装支付金额
			map2.put("total_fee", total_fee);
			// 封装订单号
			map2.put("out_trade_no", out_trade_no);
			// 封装一个提示信息吧
			map2.put("message", "调用微信支付接口成功");
			return map2;

		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<>();
		}

	}

	/**
	 * 查询订到号的支付状态 参数:订单号 返回值:map对象,里面有success或者closed等信息,根据不同的信息,在做出不同的处理
	 */
	public Map checkPayStatus(String out_trade_no) {
		// 1.封装微信支付查询的参数
		Map wxmap = new HashMap<>();
		// 公众号id
		wxmap.put("appid", appid);
		// 商户号
		wxmap.put("mch_id", partner);
		// 订单号
		wxmap.put("out_trade_no", out_trade_no);
		// 随机字符串
		wxmap.put("nonce_str", WXPayUtil.generateNonceStr());
		// 签名
		// 转换为xml格式并添加签名
		try {
			String xmlParam = WXPayUtil.generateSignedXml(wxmap, partnerkey);
			// 调用httpclient发送请求,得到结果
			HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
			client.setHttps(true);
			client.setXmlParam(xmlParam);
			client.post();
			// 获得结果
			String statusResultString = client.getContent();
			// 把结果转换为map对象
			Map<String, String> statusMap = WXPayUtil.xmlToMap(statusResultString);
			// 判断是否支付成功
			String trade_state = statusMap.get("trade_state");
			Map map = new HashMap<>();
			if (trade_state.equals("SUCCESS")) {
				// 支付成功
				map.put("trade_state", "SUCCESS");
			} else if (trade_state.equals("PAYERROR")) {
				// 支付失败
				map.put("trade_state", "PAYERROR");
			} else {
				//未支付,判断超时等
				map.put("trade_state", "NOTPAY");
			}
			// 状态查询成功
			return map;

		} catch (Exception e) {
			e.printStackTrace();
			// 状态查询失败
			return null;
		}

	}

}
