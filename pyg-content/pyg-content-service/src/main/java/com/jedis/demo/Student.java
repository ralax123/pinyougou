package com.jedis.demo;

import java.io.Serializable;

public class Student implements Serializable{
	private String name;
	private String address;

	public Student(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}

	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


}
