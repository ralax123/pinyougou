package com.pyg.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.BrandService;
import com.pyg.manager.service.SpecificationService;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PygResult;
import com.pyg.vo.Specification;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

	// 注入远程service服务对象
	@Reference(timeout = 10000000)
	private SpecificationService specificationService;

	// 分页查询
	@RequestMapping("findPage")
	public PageResult findPage(int page, int rows) {
		PageResult pageResult = specificationService.findPage(page, rows);
		return pageResult;
	}

	/**
	 * 需求:添加规格和规格选项两个表
	 * 
	 */
	@RequestMapping("add")
	public PygResult add(@RequestBody Specification specification) {
		PygResult result = specificationService.add(specification);
		return result;
	}

	/**
	 * 根据id查询规格和规格选项 参数:id 返回值Specification
	 */
	@RequestMapping("findOne")
	public Specification findOnd(Long id) {
		return specificationService.findOne(id);
	}

	/**
	 * 保存规格和规格选项
	 */
	@RequestMapping("update")
	public PygResult update(@RequestBody Specification specification) {
		return specificationService.update(specification);
	};

	/**
	 * 需求:批量删除规格 参数long数组ids 返回值pygresult
	 */
	@RequestMapping("delete")
	public PygResult delete(Long[] ids) {
		return specificationService.delete(ids);

	};
	/**
	 * 查询id和name,用于显示在select2 的下拉列表里
	 */
	@RequestMapping("findSpecList")
	public List<Map> findSpecList(){
		return specificationService.findSpecList();
	}
}
