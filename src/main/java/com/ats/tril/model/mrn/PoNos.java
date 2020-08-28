package com.ats.tril.model.mrn;


public class PoNos {

	private int mrnDetailId;
	
	private int poId;
	
	private String poNo;

	public int getMrnDetailId() {
		return mrnDetailId;
	}

	public void setMrnDetailId(int mrnDetailId) {
		this.mrnDetailId = mrnDetailId;
	}

	public int getPoId() {
		return poId;
	}

	public void setPoId(int poId) {
		this.poId = poId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	@Override
	public String toString() {
		return "PoNos [mrnDetailId=" + mrnDetailId + ", poId=" + poId + ", poNo=" + poNo + "]";
	}
	
	
}
