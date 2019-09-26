package com.pyg.manager.service;

import java.util.List;
import java.util.Map;

import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PygResult;
import com.pyg.vo.Specification;

public interface SpecificationService {

	// 返回分页列表
	public PageResult findPage(int pageNum, int pageSize);

	// 保存规格--规格选项
	// 参数specification
	public PygResult add(Specification specification);
	
	/**
	 * 根据id查询规格和规格选项 
	 * 参数:id 
	 * 返回值Specification
	 */
	public Specification findOne(Long id);

	//保存规格和规格选项 
	public PygResult update(Specification specification);

	/**
	 * 需求:批量删除规格 参数long数组ids 返回值pygresult
	 */
	public PygResult delete(Long[] ids);

	/**
	 * 查询id和name,用于显示在select2 的下拉列表里
	 */
	public List<Map> findSpecList();

}
