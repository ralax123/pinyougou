package com.pyg.manager.service;

import java.util.List;
import java.util.Map;

import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PygResult;

public interface BrandService {

	/**
	 * 需求：查询所有品牌数据
	 */
	public List<TbBrand> findAll();

	// 返回分页列表
	public PageResult findPage(int pageNum, int pageSize);

	// 添加商品品牌
	public PygResult add(TbBrand brand);

	// 修改商品updateByprimaryKey
	public PygResult updateByprimaryKey(TbBrand brand);

	// 查询单个商品
	public TbBrand findOne(Long id);

	// 删除商品品牌
	public PygResult delete(Long[] id);

	// 品牌条件查询
	public PageResult queryBrand(TbBrand brand, int page, int rows);

	//查询,下拉框select2
	public List<Map> findBrandList();

}
