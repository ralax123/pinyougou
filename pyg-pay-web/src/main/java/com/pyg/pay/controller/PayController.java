package com.pyg.pay.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pay.service.PayService;
import com.pyg.utils.IdWorker;

@RestController
@RequestMapping("/pay")
public class PayController {

	@Reference(timeout = 100000)
	private PayService payService;

	/**
	 * 调用微信支付接口, 参数:1.支付金额,2out_trade_no 订单号
	 * 返回值:1.二维码链接地址,2支付金额,3订单号out_trade_no
	 */
	@RequestMapping("creatQrCode")
	public Map creatQrCode() {
		// 生成订单号
		IdWorker idWorker = new IdWorker();
		long out_trade_no = idWorker.nextId();
		Map payMap = payService.creatWXPay("1", out_trade_no + "");
		return payMap;
	}

	/**
	 * 查询订到号的支付状态 参数:订单号 返回值:map对象,里面有success或者closed等信息,根据不同的信息,在做出不同的处理
	 * 
	 * @throws InterruptedException
	 */
	@RequestMapping("checkPayStatus")
	public Map checkPayStatus(String out_trade_no) throws InterruptedException {

		int i = 0;
		while (true) {
			// 每三秒查询一次支付状态
			Thread.sleep(1000);
			System.out.println("啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊" + i);
			Map statusMap = payService.checkPayStatus(out_trade_no);
			// 如果支付成功,则退出循环
			if (statusMap.get("trade_state").equals("SUCCESS")) {
				// 支付成功,退出循环
				return statusMap;
			}

			i += 1;
			if (i > 30) {
				// 超过5分钟了覆盖statusMap里面的trade_stat字段的值
				statusMap.put("trade_state", "timeout");
				return statusMap;

			}

		}

	}

}