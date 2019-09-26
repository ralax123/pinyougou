package com.pyg.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.BrandService;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbBrandExample;
import com.pyg.pojo.TbBrandExample.Criteria;
import com.pyg.utils.PygResult;

@Service
public class BrandServiceImpl implements BrandService {

	// 注入品牌mapper接口代理对象
	@Autowired
	private TbBrandMapper brandMapper;

	public List<TbBrand> findAll() {
		// 调用接口方法
		List<TbBrand> list = brandMapper.selectByExample(null);
		return list;
	}

	@Override
	// 分页查询
	public PageResult findPage(int pageNum, int pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);

		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	// 添加商品品牌
	public PygResult add(TbBrand brand) {
		// TODO Auto-generated method stub
		try {
			brandMapper.insert(brand);
			return new PygResult(true, "添加成功");

		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "添加失败");

		}
	}

	@Override
	// 修改商品品牌信息
	public PygResult updateByprimaryKey(TbBrand brand) {
		// TODO Auto-generated method stub
		try {
			brandMapper.updateByPrimaryKey(brand);
			return new PygResult(true, "修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new PygResult(false, "修改失败");
		}
	}

	@Override
	// 查询单个商品--商品修改前的工作
	public TbBrand findOne(Long id) {
		// TODO Auto-generated method stub
		TbBrand tbBrand = brandMapper.selectByPrimaryKey(id);
		return tbBrand;
	}

	@Override
	// 批量删除商品品牌
	public PygResult delete(Long[] ids) {
		try {
			for (Long id : ids) {
				brandMapper.deleteByPrimaryKey(id);
			}
			return new PygResult(true, "删除成功");
		} catch (Exception e) {
			return new PygResult(false, "删除失败");
		}
	}

	// 条件查询商品品牌并分页
	@Override
	public PageResult queryBrand(TbBrand brand, int page, int rows) {
		// TODO Auto-generated method stub

		// 判断前面传来的查询调教
		TbBrandExample example = new TbBrandExample();
		Criteria criteria = example.createCriteria();
		if (brand.getName() != null && brand.getName().length() > 0) {
			criteria.andNameLike("%" + brand.getName() + "%");
		}
		if (brand.getFirstChar() != null && brand.getFirstChar().length() > 0) {
			criteria.andFirstCharEqualTo(brand.getFirstChar());
		}

		PageHelper.startPage(page, rows);
		Page<TbBrand> pageInfo = (Page<TbBrand>) brandMapper.selectByExample(example);

		return new PageResult(pageInfo.getTotal(), pageInfo.getResult());
	}

	@Override
	// 查询所有,显示下来框,select2
	public List<Map> findBrandList() {
		// TODO Auto-generated method stub
		List<Map> brandList = brandMapper.findBrandList();
		return brandList;
	}

}
