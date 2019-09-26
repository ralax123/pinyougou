package com.pyg.manager.service;

import java.util.List;
import java.util.Map;

import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbTypeTemplate;
import com.pyg.utils.PygResult;

public interface TypeTemplateService {

	// 返回分页列表
	public PageResult findPage(int pageNum, int pageSize);

	// 保存规格--规格选项
	// 参数specification
	public PygResult add(TbTypeTemplate tbTypeTemplate);

	/**
	 * 根据id查询规格和规格选项 参数:id 返回值Specification
	 */
	public TbTypeTemplate findOne(Long id);

	// 保存规格和规格选项
	public PygResult update(TbTypeTemplate tbTypeTemplate);

	/**
	 * 需求:批量删除规格 参数long数组ids 返回值pygresult
	 */
	public PygResult delete(Long[] ids);

	/**
	 * 查询specifaction表中的id和name,封装成id和text返回
	 */
	public List<TbSpecification> findSpc();

	// 通过id查询返回规格选项
	public List<Map> findSpecList(Long id);

}
