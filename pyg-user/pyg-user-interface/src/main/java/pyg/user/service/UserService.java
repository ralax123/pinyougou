package pyg.user.service;

import com.pyg.pojo.TbUser;
import com.pyg.utils.PygResult;

/**
 * 服务层接口
 * 
 * @author Administrator
 *
 */
public interface UserService {

	// 注册
	public PygResult register(String smsCode, TbUser user);

	// 发送验证码
	/**
	 * 1.生成验证码
	 *  2.给手机发验证码,同时把验证码保存在redis服务器里,
	 *  便于后期注册时校验验证码 3
	 *  3.把手机号和验证码作为消息发给activemq消息服务器里
	 */
	public PygResult getSmsCode(String phone);

}
