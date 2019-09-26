package com.pyg.vo;

import java.io.Serializable;

public class OrderRecode implements Serializable {

	private Long id;
	private String username;

	public OrderRecode(Long id, String username) {
		super();
		this.id = id;
		this.username = username;
	}

	public OrderRecode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
