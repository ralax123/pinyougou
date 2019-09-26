package com.pyg.sms;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.aliyuncs.exceptions.ClientException;
import com.pyg.utils.SmsUtils;

@Component
public class SendSms {

	// 注入工具类
	@Autowired
	private SmsUtils smsUtils;

	// 监听mq收到的消息
	@JmsListener(destination = "smsCodeQueue")
	public void sendSms(Map<String, String> smsMap) {

		try {// 获取手机号
			String phone = smsMap.get("phone");
			// 获取自己生成的验证码
			String code = smsMap.get("code");
			// 获取模板
			String templateCode = smsMap.get("template_code");
			// 获取签名
			String signName = smsMap.get("sign_name");
			// signName = new String(signName.getBytes("ISO8859-1"), "UTF-8");

			// 调用发短信
			System.out.println("=====================================");
			System.out.println(phone);
			System.out.println(code);
			System.out.println("templateCode" + templateCode);
			System.out.println("signName" + signName);

			smsUtils.sendSms(signName, templateCode, phone, code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
