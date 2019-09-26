package com.pyg.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.TypeTemplateService;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.mapper.TbTypeTemplateMapper;
import com.pyg.pojo.PageResult;
import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationOption;
import com.pyg.pojo.TbSpecificationOptionExample;
import com.pyg.pojo.TbTypeTemplate;
import com.pyg.utils.PygResult;

@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

	// 注入
	@Autowired
	private TbTypeTemplateMapper tbTypeTemplateMapper;
	// 注入规格选项
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;

	@Override
	// 分页查询
	public PageResult findPage(int pageNum, int pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		Page<TbTypeTemplate> pageInfo = (Page<TbTypeTemplate>) tbTypeTemplateMapper.selectByExample(null);

		return new PageResult(pageInfo.getTotal(), pageInfo.getResult());
	}

	@Override
	// 添加
	public PygResult add(TbTypeTemplate tbTypeTemplate) {
		try {
			tbTypeTemplateMapper.insert(tbTypeTemplate);
			// 添加成功
			return new PygResult(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "添加失败");
		}
	}

	@Override
	public TbTypeTemplate findOne(Long id) {
		// TODO Auto-generated method stub
		TbTypeTemplate primaryKey = tbTypeTemplateMapper.selectByPrimaryKey(id);

		return primaryKey;
	}

	@Override
	public PygResult update(TbTypeTemplate tbTypeTemplate) {
		// TODO Auto-generated method stub
		try {
			tbTypeTemplateMapper.updateByPrimaryKey(tbTypeTemplate);
			// 修改成功过
			return new PygResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "修改失败");
		}
	}

	@Override
	public PygResult delete(Long[] ids) {
		try {
			// 循环删除
			for (Long id : ids) {
				tbTypeTemplateMapper.deleteByPrimaryKey(id);
			}
			// 删除成功
			return new PygResult(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "删除失败");
		}
	}

	/**
	 * 查询specifaction表中的id和name,封装成id和text返回
	 */
	public List<TbSpecification> findSpc() {
		List<TbSpecification> list = tbTypeTemplateMapper.findIdAndName();
		return list;
	}

	// 通过id查询返回规格选项
	public List<Map> findSpecList(Long id) {
		TbTypeTemplate typeTemplate = tbTypeTemplateMapper.selectByPrimaryKey(id);
		// typeTemplate得到到时是一个json的字符串[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}],要转换为map
		List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
		for (Map map : list) {
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			com.pyg.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(new Long((Integer) map.get("id")));
			List<TbSpecificationOption> options = specificationOptionMapper.selectByExample(example);
			map.put("option", options);
		}
		return list;

	}

}
