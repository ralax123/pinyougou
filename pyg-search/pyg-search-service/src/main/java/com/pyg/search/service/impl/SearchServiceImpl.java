package com.pyg.search.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pyg.pojo.TbItem;
import com.pyg.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SolrTemplate solrTemplate;

	// 查询本页面传来的参数,分页查询出总条数和商品集合返回 从solr里面查询
	public Map search(Map searchMap) {

		String keywords = (String) searchMap.get("keywords");
		// 创建封装条件参数的query对象
		HighlightQuery query = new SimpleHighlightQuery();
		Criteria criteria = null;
		// 判断参数是否为null
		if (keywords != null && !"".equals(keywords)) {
			criteria = new Criteria("item_title").is(keywords);
		} else {
			criteria = new Criteria().expression("*:*");
		}

		// 2添加分类过滤查询
		String category = (String) searchMap.get("category");
		if (category != null && !"".equals(category)) {
			Criteria categoryCriteria = new Criteria("item_category").is(category);
			FilterQuery filterQuery = new SimpleFilterQuery(categoryCriteria);
			query.addFilterQuery(filterQuery);
		}
		// 3.添加品牌过滤查询
		String brand = (String) searchMap.get("brand");
		if (brand != null && !"".equals(brand)) {
			Criteria criteria2 = new Criteria("item_brand").is(brand);
			// 创建过滤查询对象
			FilterQuery filterQuery = new SimpleFilterQuery(criteria2);
			// 将过滤查询对象添加到主查询里
			query.addFilterQuery(filterQuery);
		}
		// 4.添加价格过滤查询
		String price = (String) searchMap.get("price");// "0-500"
		if (price != null && !"".equals(price)) {
			String[] priceArr = price.split("-");
			String priceFirst = priceArr[0];
			String priceLast = priceArr[1];
			// 判断价格过滤区间
			if (!priceFirst.equals("0")) {
				Criteria criteria2 = new Criteria("item_price").greaterThanEqual(priceFirst);
				// 创建过滤查询对象
				FilterQuery filterQuery = new SimpleFilterQuery(criteria2);
				// 将过滤查询对象添加到主查询里
				query.addFilterQuery(filterQuery);
			}
			if (!priceLast.equals("*")) {
				Criteria criteria2 = new Criteria("item_price").lessThanEqual(priceLast);
				// 创建过滤查询对象
				FilterQuery filterQuery = new SimpleFilterQuery(criteria2);
				// 将过滤查询对象添加到主查询里
				query.addFilterQuery(filterQuery);
			}

		}
		// 5添加规格属性过滤查询
		Map<String, String> map = (Map<String, String>) searchMap.get("spec");
		if (map != null) {
			for (String key : map.keySet()) {
				Criteria criteria2 = new Criteria("item_spec_" + key).is(map.get(key));
				// 创建过滤查询对象
				FilterQuery filterQuery = new SimpleFilterQuery(criteria2);
				// 将过滤查询对象添加到主查询里
				query.addFilterQuery(filterQuery);
			}
		}

		// 6添加排序过滤插叙
		String sortField = (String) searchMap.get("sortField");
		String sort = (String) searchMap.get("sort");
		if (sortField != null && !"".equals(sortField) && sort != null && !"".equals(sort)) {
			if (sort.equals("ASC")) {
				Sort sort2 = new Sort(Direction.ASC, "item_" + sortField);
				query.addSort(sort2);
			}
			if (sort.equals("DESC")) {
				Sort sort2 = new Sort(Direction.DESC, "item_" + sortField);
				query.addSort(sort2);
			}

		}

		// 7.高亮查询

		// 创建高亮对象
		HighlightOptions highlightOptions = new HighlightOptions();
		// 添加高亮字段
		highlightOptions.addField("item_title");
		// 设置高亮前后缀
		highlightOptions.setSimplePrefix("<font color='red'>");
		highlightOptions.setSimplePostfix("</font>");
		// 把高亮字段添加到查询对象里
		query.setHighlightOptions(highlightOptions);

		// 添加查询条件
		query.addCriteria(criteria);

		// ====================================
		// 8.分页查询
		Integer page = new Integer(searchMap.get("page") + "");
		Integer pageSize = new Integer(searchMap.get("pageSize") + "");
		// 判读
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Integer pageBegin = (page - 1) * pageSize;

		// 分页查询
		query.setOffset(pageBegin);
		query.setRows(pageSize);

		// 查询
		HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

		// 获取总记录数据
		long totalElements = highlightPage.getTotalElements();

		// 获取总记录
		List<TbItem> list = highlightPage.getContent();

		// 循环获得高亮字段
		for (TbItem tbItem : list) {
			List<Highlight> highlights = highlightPage.getHighlights(tbItem);
			// 判断高亮时候存在
			if (highlights != null && highlights.size() > 0) {
				// 获取高亮字段
				String highLightTitle = highlights.get(0).getSnipplets().get(0);
				// 把高亮字段添加到对象里
				tbItem.setTitle(highLightTitle);
			}

		}

		// 创建map对象，封装查询结果
		Map<String, Object> maps = new HashMap<>();
		maps.put("total", totalElements);
		maps.put("rows", list);
		// 封装总页码数
		int totalPages = highlightPage.getTotalPages();
		maps.put("totalPages", totalPages);

		return maps;
	}

}
