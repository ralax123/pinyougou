package com.pyg.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemExample;
import com.pyg.pojo.TbItemExample.Criteria;
import com.alibaba.fastjson.JSON;

@Component
public class SolrUtil {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private SolrTemplate solrTemplate;

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring/*.xml");
		SolrUtil solrUtil = (SolrUtil) ac.getBean(SolrUtil.class);
		solrUtil.importItemData();
	}

	public void importItemData() {
		// TODO Auto-generated method stub
		// 查询商品
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");
		List<TbItem> list = itemMapper.selectByExample(example);
		for (TbItem tbItem : list) {
			Map<String, String> specMap = (Map<String, String>) JSON.parse(tbItem.getSpec());
			tbItem.setSpecMap(specMap);

		}
		// 将查询的商品添加到solr里面
		solrTemplate.saveBeans(list);
		// 提交
		solrTemplate.commit();

	}
}
