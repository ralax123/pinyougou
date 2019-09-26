package com.pyg.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.pyg.mapper.TbGoodsDescMapper;
import com.pyg.mapper.TbGoodsMapper;
import com.pyg.mapper.TbItemCatMapper;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbGoodsExample;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemCat;
import com.pyg.pojo.TbItemCatExample;
import com.pyg.pojo.TbItemExample;
import com.pyg.pojo.TbItemExample.Criteria;

@Component
public class HtmlUtils {

	// 注入货品mapper接口代理对象
	@Autowired
	private TbGoodsMapper goodsMapper;

	// 注入描述对象
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	// 注入商品分类对象
	@Autowired
	private TbItemCatMapper itemCatMapper;

	// 注入mapper接口代理对象
	@Autowired
	private TbItemMapper itemMapper;

	/**
	 * 需求： 1，查询数据库 数据 1） 货品数据 2） 货品描述数据 3） sku列表数据 4）面包屑导航-分类数据 2，生成静态页面
	 */
	public void genHTML() {

		try {
			// 创建一个map对象，封装数据
			Map<String, Object> dataModel = new HashMap<String, Object>();

			// 创建货品对象example对象
			TbGoodsExample example = new TbGoodsExample();
			com.pyg.pojo.TbGoodsExample.Criteria createCriteria = example.createCriteria();
			// 设置参数
			// 分类不能null
			createCriteria.andCategory1IdIsNotNull();
			createCriteria.andCategory2IdIsNotNull();
			createCriteria.andCategory3IdIsNotNull();
			// 审核通过
			createCriteria.andAuditStatusEqualTo("1");

			// 执行查询
			List<TbGoods> goodsList = goodsMapper.selectByExample(example);

			// 循环货品列表，每一个货品列表都对应生成html
			for (TbGoods tbGoods : goodsList) {
				// 查询货品描述数据
				TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(tbGoods.getId());

				// 查询sku列表数据
				// 创建example对象
				TbItemExample example2 = new TbItemExample();
				// 创建criteria对象
				Criteria createCriteria2 = example2.createCriteria();
				// 设置参数
				createCriteria2.andGoodsIdEqualTo(tbGoods.getId());

				// 执行查询
				List<TbItem> itemList = itemMapper.selectByExample(example2);

				// 封装数据
				dataModel.put("goods", tbGoods);

				dataModel.put("goodsDesc", goodsDesc);

				// sku列表
				dataModel.put("itemList", itemList);

				// 查询一级分类

				TbItemCat itemCat1 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id());

				// 分类
				dataModel.put("itemCat1", itemCat1.getName());

				// 查询二级分类

				TbItemCat itemCat2 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id());

				// 分类
				dataModel.put("itemCat2", itemCat2.getName());

				// 查询三级分类

				TbItemCat itemCat3 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());

				// 分类
				dataModel.put("itemCat3", itemCat3.getName());
				
				//调用工具类生成HTML
				FMUtils fm = new FMUtils();
				fm.ouputFile("item.ftl",tbGoods.getId()+".html" , dataModel);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// 加载配置文件
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath*:spring/*.xml");
		// 获取工具类对象
		HtmlUtils htmlUtils = app.getBean(HtmlUtils.class);
		// 调用数据导入方法
		htmlUtils.genHTML();
	}

}
