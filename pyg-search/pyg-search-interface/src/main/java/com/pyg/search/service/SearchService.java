package com.pyg.search.service;

import java.util.Map;

public interface SearchService {


	// 查询本页面传来的参数,分页查询出总条数和商品集合返回
	Map search(Map searchMap);

}
