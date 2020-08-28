package com.ats.tril.model;

import java.util.List;

import com.ats.tril.model.mrn.MrnDetail;

//Sachin 28-07-2020
// Class to transfer various beans to web api

public class TransModel {

	List<PoDetail> poDetailList;
	List<MrnDetail> mrnDetailList;
	
	GetPoHeaderList poHeader;
	
	public List<PoDetail> getPoDetailList() {
		return poDetailList;
	}
	public void setPoDetailList(List<PoDetail> poDetailList) {
		this.poDetailList = poDetailList;
	}
	
	public List<MrnDetail> getMrnDetailList() {
		return mrnDetailList;
	}
	public void setMrnDetailList(List<MrnDetail> mrnDetailList) {
		this.mrnDetailList = mrnDetailList;
	}
	
	public GetPoHeaderList getPoHeader() {
		return poHeader;
	}
	public void setPoHeader(GetPoHeaderList poHeader) {
		this.poHeader = poHeader;
	}
	
	
}
