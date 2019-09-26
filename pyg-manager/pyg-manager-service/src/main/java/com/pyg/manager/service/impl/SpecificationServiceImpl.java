package com.pyg.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.SpecificationService;
import com.pyg.mapper.TbSpecificationMapper;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationOption;
import com.pyg.pojo.TbSpecificationOptionExample;
import com.pyg.utils.PygResult;
import com.pyg.vo.Specification;

@Service
public class SpecificationServiceImpl implements SpecificationService {

	// 注入品牌mapper接口代理对象
	@Autowired
	private TbSpecificationMapper specificationMapper;
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;

	@Override
	// 分页查询
	public PageResult findPage(int pageNum, int pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		Page<TbSpecification> pageInfo = (Page<TbSpecification>) specificationMapper.selectByExample(null);

		return new PageResult(pageInfo.getTotal(), pageInfo.getResult());
	}

	@Override
	// 保存规格和规格选项
	// 保存规格的时候要返回主键,用来保存 规格选项
	public PygResult add(Specification specification) {
		// TODO Auto-generated method stub
		try {
			specificationMapper.insertSelective(specification.getTbSpecification());

			List<TbSpecificationOption> optionList = specification.getOptionList();
			// 循环添加规格选项
			for (TbSpecificationOption specificationOption : optionList) {
				// 会的规格的主键
				Long id = specification.getTbSpecification().getId();
				specificationOption.setSpecId(id);
				specificationOptionMapper.insertSelective(specificationOption);
			}
			// 保存成功
			return new PygResult(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			// 保存失败
			return new PygResult(false, "aaa添加失败");
		}
	}

	/**
	 * 根据id查询规格和规格选项 参数:id 返回值Specification
	 */
	public Specification findOne(Long id) {
		// TODO Auto-generated method stub
		// 先查询规格
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		// 根据外键查询规格选项
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.pyg.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		// 查询
		List<TbSpecificationOption> list = specificationOptionMapper.selectByExample(example);
		Specification specification = new Specification(tbSpecification, list);

		return specification;
	}

	/**
	 * 保存规格和规格选项
	 */
	public PygResult update(Specification specification) {
		// TODO Auto-generated method stub
		// 1保存规格商品
		try {
			TbSpecification tbSpecification = specification.getTbSpecification();
			Long id = tbSpecification.getId();
			specificationMapper.updateByPrimaryKey(tbSpecification);
			// 在循环保存规格选项--可能数量有增减,直接删除然后添加
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			com.pyg.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);
			//删除带有指点外键的规格选项
			specificationOptionMapper.deleteByExample(example);

			// 在添加
			List<TbSpecificationOption> list = specification.getOptionList();
			for (TbSpecificationOption tbSpecificationOption : list) {
				tbSpecificationOption.setSpecId(id);
				specificationOptionMapper.insert(tbSpecificationOption);

			}
			// 保存成功
			return new PygResult(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			// 保存失败
			return new PygResult(false, "保存失败");
		}

	}

	/**
	 * 需求:批量删除规格 参数long数组ids 返回值pygresult
	 */
	public PygResult delete(Long[] ids) {
		// TODO Auto-generated method stub
		try {
			//先循环删除规格表的数据
			for (Long id : ids) {
				specificationMapper.deleteByPrimaryKey(id);
				//在删除规格选项表里的 数据
				TbSpecificationOptionExample example=new TbSpecificationOptionExample();
				com.pyg.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
				criteria.andSpecIdEqualTo(id);
				specificationOptionMapper.deleteByExample(example);
			}
			//删除成功
			return new PygResult(true, "删除规格表数据成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "删除规格表数据失败");
		}
		
	}

	/**
	 * 查询id和name,用于显示在select2 的下拉列表里
	 */
	public List<Map> findSpecList() {
		// TODO Auto-generated method stub
		return specificationMapper.findSpecList();
		 
	}

}
