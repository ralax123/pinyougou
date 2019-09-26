package com.pyg.order.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbAddressMapper;
import com.pyg.mapper.TbOrderItemMapper;
import com.pyg.mapper.TbOrderMapper;
import com.pyg.order.service.OrderService;
import com.pyg.pojo.TbAddress;
import com.pyg.pojo.TbAddressExample;
import com.pyg.pojo.TbAddressExample.Criteria;
import com.pyg.pojo.TbOrder;
import com.pyg.pojo.TbOrderItem;
import com.pyg.utils.IdWorker;
import com.pyg.utils.PygResult;
import com.pyg.vo.Cart;
import com.pyg.vo.OrderInfo;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;

	// 注入地址接口代理对象
	@Autowired
	private TbAddressMapper addressMapper;
	// 注入redis服务
	@Autowired
	private RedisTemplate redisTemplate;
	// 注入orderitem接口代理对象
	@Autowired
	private TbOrderItemMapper orderItemMapper;

	// 注入idwork
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询买家地址列表
	 */
	public List<TbAddress> findAddressList(String username) {
		// TODO Auto-generated method stub
		TbAddressExample example = new TbAddressExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(username);
		List<TbAddress> addressList = addressMapper.selectByExample(example);
		return addressList;
	}

	/**
	 * 将购物车里的商品提交到表里--三个表
	 */
	public PygResult submitOrder(OrderInfo orderInfo, String username) {
		try {

			TbAddressExample example = new TbAddressExample();
			Criteria criteria = example.createCriteria();
			criteria.andIsDefaultEqualTo("1");
			// 把默认地址为1的查询出来,修改为0,
			List<TbAddress> addressList = addressMapper.selectByExample(example);
			if (addressList != null && addressList.size() > 0) {
				TbAddress tbAddress = addressList.get(0);
				tbAddress.setIsDefault("0");
				addressMapper.updateByPrimaryKeySelective(tbAddress);
			}
			// 1.修改用户的默认地址
			TbAddress address = orderInfo.getAddress();
			// 把前台传递过来的这个地址修改数据库里字段
			address.setIsDefault("1");
			addressMapper.updateByPrimaryKeySelective(address);

			// 2保存order表
			TbOrder order = orderInfo.getOrder();
			// 从redis购物车里查询出所有的商品---Stirng类型不能直接转换为list类型[报错]
			String cartList1 = (String) redisTemplate.boundHashOps("redis_cart").get(username);
			List<Cart> cartList = JSON.parseArray(cartList1, Cart.class);
			// 循环购物车商品,添加到表里
			for (Cart cart : cartList) {
				TbOrder tbOrder = new TbOrder();
				// 生成订单号
				long orderId = idWorker.nextId();
				// 1设置订单号
				tbOrder.setOrderId(orderId);
				// 设置商家的总价格
				tbOrder.setPayment(new BigDecimal(10000));
				// 支付方法
				tbOrder.setPaymentType(orderInfo.getOrder().getPaymentType());
				// 设置付款状态
				tbOrder.setStatus("1");
				Date date =new Date();
				// 设置创建时间
				tbOrder.setCreateTime(date);
				// 设置订单更新时间
				tbOrder.setUpdateTime(date);
				// 设置买家地址
				tbOrder.setReceiverAreaName("传智播客");
				// 设置买家手机号
				tbOrder.setReceiverMobile(orderInfo.getOrder().getReceiverMobile());
				// 设置买家姓名
				tbOrder.setReceiver(orderInfo.getOrder().getReceiver());
				// 设置订单来源 2-pc段
				tbOrder.setSourceType("2");
				// 设置商家id
				tbOrder.setSellerId(cart.getSellerId());

				// 保存
				orderMapper.insertSelective(tbOrder);

				// 循环购物车里每个商家的商品
				List<TbOrderItem> orderItemList = cart.getOrderItemList();
				for (TbOrderItem tbOrderItem : orderItemList) {
					// 生成订单明细
					long orderItemId = idWorker.nextId();
					// 设置订单id
					tbOrderItem.setId(orderItemId);
					// 设置订单id
					tbOrderItem.setOrderId(orderId);
					// 设置sellerid
					tbOrderItem.setSellerId(cart.getSellerId());
					// 保存
					orderItemMapper.insertSelective(tbOrderItem);
				}

			}
			// 保存成功过
			return new PygResult(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			// 保存失败
			return new PygResult(false, "保存失败");
		}

	}

}
