package com.pyg.portal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.content.service.ContentService;
import com.pyg.pojo.TbContent;

@RestController
@RequestMapping("/content")
public class ContentController {
	
	
	@RequestMapping("hello")
	public String showHello(){
		return "hello test";
	}

	@Reference
	private ContentService contentService;

	// 查询指定广告分类下的广告
	@RequestMapping("findAdByCategoryId")
	public List<TbContent> findAdByCategoryId(Long categoryId) {
		return contentService.findAdByCategoryId(categoryId);

	}

}
