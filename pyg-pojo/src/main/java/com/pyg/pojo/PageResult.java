package com.pyg.pojo;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable {
	// 总记录数
	private long total;
	// 当前页结果集
	private List rows;

	public PageResult(long total, List rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public PageResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "PageResult [total=" + total + ", rows=" + rows + "]";
	}

}
