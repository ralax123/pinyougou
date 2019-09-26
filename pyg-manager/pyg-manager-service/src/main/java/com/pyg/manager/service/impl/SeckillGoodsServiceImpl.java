package com.pyg.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.SeckillGoodsService;
import com.pyg.mapper.TbSeckillGoodsMapper;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbAddressExample.Criteria;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.pojo.TbSeckillGoodsExample;
import com.pyg.utils.PygResult;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillGoods> findAll() {
		// 查询商品未审核的
		TbSeckillGoodsExample example = new TbSeckillGoodsExample();
		com.pyg.pojo.TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("0");

		return seckillGoodsMapper.selectByExample(example);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbSeckillGoods> page = (Page<TbSeckillGoods>) seckillGoodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeckillGoods seckillGoods) {
		seckillGoodsMapper.insert(seckillGoods);
	}

	/**
	 * 修改
	 */
	@Override
	public void update(TbSeckillGoods seckillGoods) {
		seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillGoods findOne(Long id) {
		return seckillGoodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			seckillGoodsMapper.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 修改商家提交的秒杀商品的状态
	 * 
	 */
	public PygResult updateStatus(Long[] ids, String status) {
		try {
			for (Long id : ids) {
				TbSeckillGoods seckillGoods = seckillGoodsMapper.selectByPrimaryKey(id);
				// 修改状态
				seckillGoods.setStatus(status);
				// 在修改
				seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
			}
			// 修改状态成功
			return new PygResult(true, "修改状态成功");
		} catch (Exception e) {
			e.printStackTrace();
			// 修改状态失败
			return new PygResult(false, "修改状态失败");
		}
	}

}
