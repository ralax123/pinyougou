package com.pyg.manager.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.GoodsService;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.mapper.TbGoodsDescMapper;
import com.pyg.mapper.TbGoodsMapper;
import com.pyg.mapper.TbItemCatMapper;
import com.pyg.mapper.TbItemMapper;
import com.pyg.mapper.TbSellerMapper;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbGoodsExample;
import com.pyg.pojo.TbGoodsExample.Criteria;
import com.pyg.pojo.TbItem;
import com.pyg.utils.PygResult;
import com.pyg.vo.Goods;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemMapper itemMapper;
	// 注入品牌
	@Autowired
	private TbBrandMapper brandMapper;
	// 注入商家
	@Autowired
	private TbSellerMapper sellerMapper;
	// 注入商品分类
	@Autowired
	private TbItemCatMapper itemCatMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		TbGoodsExample example = new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeleteEqualTo("1");
		return goodsMapper.selectByExample(example);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbGoods goods) {
		goodsMapper.insert(goods);
	}

	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods) {
		goodsMapper.updateByPrimaryKey(goods);
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id) {
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			goodsMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbGoodsExample example = new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		// 值查询未审核的和没有被删除的的商品.未审核的状态前台已经初始化放到goods里面了
		criteria.andIsDeleteEqualTo("0");
		criteria.andAuditStatusEqualTo("0");

		if (goods != null) {
			if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
				criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
			}
			if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
				criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
			}
			if (goods.getCaption() != null && goods.getCaption().length() > 0) {
				criteria.andCaptionLike("%" + goods.getCaption() + "%");
			}
			if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
				criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
			}
			if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
				criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
			}
			if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
				criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
			}

		}

		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	// 插入商品信息(三个表)
	public PygResult insert(Goods goods) {
		try {
			// 1.保存tbgoods表
			TbGoods tbGoods = goods.getGoods();
			// 设置成为未申请状态
			tbGoods.setAuditStatus("0");
			//设置直接启用
			tbGoods.setIsEnableSpec("1");
			if (tbGoods != null) {
				goodsMapper.insertSelective(tbGoods);
			}
			// 2.保存tbgoodsDesc
			TbGoodsDesc tbGoodsDesc = goods.getGoodsDesc();
			if (tbGoodsDesc != null) {
				tbGoodsDesc.setGoodsId(tbGoods.getId());
				goodsDescMapper.insertSelective(tbGoodsDesc);
			}
			if ("1".equals(goods.getGoods().getIsEnableSpec())) {

				// 3对sku的保存
				List<TbItem> itemList = goods.getItemList();
				for (TbItem item : itemList) {

					// 1设置标题title--是spu标题加上规格啥的
					String title = tbGoods.getGoodsName();
					// item的spec--{"机身内存":"16G","网络":"联通3G"}
					String spec = item.getSpec();
					// 转换为对象
					Map<String, Object> specMap = (Map<String, Object>) JSON.parse(spec);
					Set<String> keySet = specMap.keySet();
					for (String key : keySet) {
						title += specMap.get(key) + "--";

					}
					item.setTitle(title);
					// 设置买点dellPoint
					item.setSellPoint("快来买啊");
					// 设置图片image--[{"color":"红色","url":"http://192.168.25.133/group1/M00/00/01/wKgZhVmHINKADo__AAjlKdWCzvg874.jpg"},{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/01/wKgZhVmHINyAQAXHAAgawLS1G5Y136.jpg"}]
					// 里面是多个图片,保存一张就行了
					String itemImages = tbGoodsDesc.getItemImages();
					List<Map> list = JSON.parseArray(itemImages, Map.class);
					String imgUrl = "";
					if (list != null && list.size() > 0) {
						imgUrl = (String) list.get(0).get("url");
					}
					item.setImage(imgUrl);

					// 设置所属分类categoryId
					item.setCategoryid(tbGoods.getCategory3Id());
					// 设置商品状态status

					// 设置创建时间create_time
					item.setCreateTime(new Date());

					// 设置更新时间update_time
					item.setUpdateTime(new Date());

					// 设置市场价
					item.setMarketPrice(item.getPrice());
					// 花费价格
					item.setCostPirce(item.getPrice());
					// 设置goods_id
					item.setGoodsId(tbGoods.getId());
					// 设置seller_id
					item.setSellerId(tbGoods.getSellerId());
					// 设置品牌名称
					TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
					item.setBrand(brand.getName());
					// 插入商品
					itemMapper.insert(item);
				}
			} else {
				TbItem item = new TbItem();
				// 1.设置title
				item.setTitle(goods.getGoods().getGoodsName());
				// 2.设置sellpoint
				item.setSellPoint("快来买卖");
				// 3设置price
				item.setPrice(goods.getGoods().getPrice());
				// 4.设置价格
				item.setPrice(goods.getGoods().getPrice());
				// 5.设置数量
				item.setNum(9999);
				// 6.设置图片-[{"color":"红色","url":"http://192.168.25.133/group1/M00/00/01/wKgZhVmHINKADo__AAjlKdWCzvg874.jpg"},{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/01/wKgZhVmHINyAQAXHAAgawLS1G5Y136.jp
				String images = goods.getGoodsDesc().getItemImages();
				List<Map> list = JSON.parseArray(images, Map.class);
				String imageUrl = "";
				if (list != null && list.size() > 0) {
					imageUrl = (String) list.get(0).get("url");
				}
				item.setImage(imageUrl);
				// 7.设置categoryId
				item.setCategoryid(goods.getGoods().getCategory3Id());
				// 8.设置状态
				item.setStatus("1");
				// 9.设置创建时间
				item.setCreateTime(new Date());
				// 10.设置修改时间
				item.setUpdateTime(new Date());
				// 11.设置花费价格
				item.setCostPirce(goods.getGoods().getPrice());
				// 12.设置商场价格
				item.setMarketPrice(goods.getGoods().getPrice());
				// 13.设置goodsid
				item.setGoodsId(goods.getGoods().getId());
				// 13设置sellerid
				item.setSellerId(goods.getGoods().getSellerId());
				// 14设置brand
				TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
				item.setBrand(brand.getName());
				// 15.设置spec
				item.setSpec("{}");
				// 16.设置分裂名称catagory
				item.setCategory(itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id()).getName());
				// 17.设置商家名称
				item.setSeller(sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId()).getNickName());

			}

			// 保存成功
			return new PygResult(true, "保存aaaaaaaaa成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "保存aaaaaaaaa失败");
		}
	}

	// 逻辑删除商品
	public PygResult deleNotTrue(Long[] ids) {
		try {
			// TODO Auto-generated method stub
			for (Long id : ids) {
				TbGoods goods = goodsMapper.selectByPrimaryKey(id);
				goods.setIsDelete("1");
				goodsMapper.updateByPrimaryKey(goods);
			}
			// 删除成功
			return new PygResult(true, "逻辑删除成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new PygResult(false, "逻辑删除失败");
		}
	}

	// 修改商品是够审核通过
	public PygResult updateStatus(Long[] ids, String status) {
		// TODO Auto-generated method stub
		try {
			for (Long id : ids) {
				TbGoods goods = goodsMapper.selectByPrimaryKey(id);
				goods.setAuditStatus(status);
				goodsMapper.updateByPrimaryKey(goods);
			}
			return new PygResult(true, "审核做出处理了");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new PygResult(true, "审核梳理失败");
		}
	}

}
