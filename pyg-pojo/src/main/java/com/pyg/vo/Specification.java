package com.pyg.vo;

import java.io.Serializable;
import java.util.List;

import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationOption;

public class Specification implements Serializable {

	// 规格
	private TbSpecification tbSpecification;

	// 规格选项
	private List<TbSpecificationOption> optionList;

	public Specification(TbSpecification tbSpecification, List<TbSpecificationOption> optionList) {
		super();
		this.tbSpecification = tbSpecification;
		this.optionList = optionList;
	}

	public Specification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TbSpecification getTbSpecification() {
		return tbSpecification;
	}

	public void setTbSpecification(TbSpecification tbSpecification) {
		this.tbSpecification = tbSpecification;
	}

	public List<TbSpecificationOption> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<TbSpecificationOption> optionList) {
		this.optionList = optionList;
	}

	@Override
	public String toString() {
		return "Specification [tbSpecification=" + tbSpecification + ", optionList=" + optionList + "]";
	}

}
