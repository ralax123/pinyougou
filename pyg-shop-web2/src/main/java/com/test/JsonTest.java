package com.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class JsonTest {
	public static void main(String[] args) {
		Map map = new HashMap<>();
		map.put("id", 12);
		map.put("name", "zhangsan ");
		map.put("address", "shanghai");
		System.out.println(map);

		String s = "{'name':'lisi','age':22}";
		Map parse = (Map) JSON.parse(s);
		System.out.println(parse);
		Object name = parse.get("name");
		System.out.println(name);
		Long age =new Long((Integer) parse.get("age")) ;
		System.out.println(age);
	}

}
