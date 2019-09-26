package com.testdemo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pyg.pojo.TbItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-solr.xml")
public class TestTemplate {

	@Autowired
	private SolrTemplate solrTemplate;

	@Test
	public void testdemo01() {
		TbItem item = new TbItem();
		item.setId(1l);
		item.setBrand("华为");
		item.setCategory("手机");
		item.setGoodsId(1l);
		item.setSeller("悟空卖家");
		item.setTitle("华为Mate");
		item.setPrice(new BigDecimal(2000));
		solrTemplate.saveBean(item);
		solrTemplate.commit();
	}

//	@Test
//	public void queryById() {
//		TbItem tem = solrTemplate.getById(1, TbItem.class);
//		System.out.println(tem.getSeller());
//	}

	// 分页查询
//	@Test
//	public void findapage() {
//		// 插入100条数据
//		List<TbItem> list = new ArrayList<TbItem>();
//		for (int i = 0; i < 100; i++) {
//			TbItem item = new TbItem();
//			item.setId(i + 1L);
//			item.setBrand("华为");
//			item.setCategory("手机");
//			item.setGoodsId(1L);
//			item.setSeller("华为2号专卖店");
//			item.setTitle("华为Mate" + i);
//			item.setPrice(new BigDecimal(2000 + i));
//			list.add(item);
//		}
//		solrTemplate.saveBeans(list);
//		solrTemplate.commit();
//	}

	@Test
	public void testPage() {
		Query query = new SimpleQuery("*:*");
		query.setOffset(20);
		query.setRows(20);
		ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
		long totalElements = scoredPage.getTotalElements();
		System.out.println(totalElements);
		List<TbItem> list = scoredPage.getContent();
		for (TbItem tbItem : list) {
			System.out.println(tbItem.getTitle() + "==" + tbItem.getPrice());

		}
	}

	// 条件插叙
	@Test
	public void testPageQuery() {
		Query query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_title").contains("2");
		query.addCriteria(criteria);
		query.setRows(80);
		ScoredPage<TbItem> queryForPage = solrTemplate.queryForPage(query, TbItem.class);
		List<TbItem> list = queryForPage.getContent();
		for (TbItem tbItem : list) {
			System.out.println(tbItem.getTitle());

		}

	}

	@Test
	// 删除数据
	public void delete() {
		Query query = new SimpleQuery("*:*");
		// Criteria criteria = new Criteria("item_title").contains("2");
		// query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
}
