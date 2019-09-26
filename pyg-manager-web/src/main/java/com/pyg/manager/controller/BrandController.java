package com.pyg.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.BrandService;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PygResult;

@RestController
@RequestMapping("/brand")
public class BrandController {

	// 注入远程service服务对象
	@Reference(timeout = 10000000)
	private BrandService brandService;

	/**
	 * 需求：查询所有品牌数据
	 */
	@RequestMapping("findAll")
	public List<TbBrand> findAll() {
		// 调用远程service服务对象方法
		List<TbBrand> list = brandService.findAll();
		return list;
	}

	// 分页查询
	@RequestMapping("findPage")
	public PageResult findPage(int page, int rows) {
		PageResult pageResult = brandService.findPage(page, rows);
		return pageResult;
	}

	// 添加商品品牌
	@RequestMapping("add")
	// 前端angular传的是json的!!!!
	public PygResult add(@RequestBody TbBrand brand) {
		PygResult result = brandService.add(brand);
		return result;
	}

	// 修改商品品牌
	@RequestMapping("update")
	public PygResult update(@RequestBody TbBrand brand) {
		PygResult result = brandService.updateByprimaryKey(brand);
		return result;
	}

	// 查询单个商品
	@RequestMapping("findOne")
	public TbBrand findOne(Long id) {
		TbBrand tbBrand = brandService.findOne(id);
		return tbBrand;
	}

	// 删除商品品牌
	@RequestMapping("delete")
	public PygResult deleteById(Long[] ids) {
		System.out.println(ids);
		PygResult result = brandService.delete(ids);
		return result;
	}

	// 查询select2,下拉框
	@RequestMapping("findBrandList")
	public List<Map> findBrandList() {
		return brandService.findBrandList();
	}

}
