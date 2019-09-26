package com.pyg.search.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.search.service.SearchService;

@RestController
@RequestMapping("/search")
public class SearchController {

	@Reference
	private SearchService searchService;

	@RequestMapping("search")
	// 查询本页面传来的参数,分页查询出总条数和商品集合返回
	public Map search(@RequestBody Map searchMap) {
		Map map = searchService.search(searchMap);
		return map;
	}

}
