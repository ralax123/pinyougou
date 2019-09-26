package com.pyg.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.TypeTemplateService;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbTypeTemplate;
import com.pyg.utils.PygResult;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

	// 注入远程service服务对象
	@Reference(timeout = 10000000)
	private TypeTemplateService typeTemplateService;

	// 分页查询
	@RequestMapping("findPage")
	public PageResult findPage(int page, int rows) {
		PageResult pageResult = typeTemplateService.findPage(page, rows);
		return pageResult;
	}

	/**
	 * 需求:添加规格和规格选项两个表
	 * 
	 */
	@RequestMapping("add")
	public PygResult add(@RequestBody TbTypeTemplate tbTypeTemplate) {
		PygResult result = typeTemplateService.add(tbTypeTemplate);
		return result;
	}

	/**
	 * 根据id查询规格和规格选项 参数:id 返回值Specification
	 */
	@RequestMapping("findOne")
	public TbTypeTemplate findOnd(Long id) {
		return typeTemplateService.findOne(id);
	}

	/**
	 * 保存规格和规格选项
	 */
	@RequestMapping("update")
	public PygResult update(@RequestBody TbTypeTemplate tbTypeTemplate) {
		return typeTemplateService.update(tbTypeTemplate);
	};

	/**
	 * 需求:批量删除规格 参数long数组ids 返回值pygresult
	 */
	@RequestMapping("delete")
	public PygResult delete(Long[] ids) {
		return typeTemplateService.delete(ids);

	};

	/**
	 * 查询specifaction表中的id和name,封装成id和text返回
	 */
	@RequestMapping("findSpc")
	public List<TbSpecification> findSpc() {
		return typeTemplateService.findSpc();
	}
}
