package com.ats.tril.model;

import java.util.List;

import com.ats.tril.model.mrn.PoNos;


public class ErpHeader {
	
	private int mrnId; 
	private String mrnDate; 
	private String MrnNo; 
	private String BillNo; 
	private String Billdate; 
	private int MrnStatus;
	private int vendorId; 
	private String vendorName; 
	List<PoNos> poNosList;

	
	
	public List<PoNos> getPoNosList() {
		return poNosList;
	}
	public void setPoNosList(List<PoNos> poNosList) {
		this.poNosList = poNosList;
	}
	public int getMrnId() {
		return mrnId;
	}
	public void setMrnId(int mrnId) {
		this.mrnId = mrnId;
	}
	public String getMrnDate() {
		return mrnDate;
	}
	public void setMrnDate(String mrnDate) {
		this.mrnDate = mrnDate;
	}
	public String getMrnNo() {
		return MrnNo;
	}
	public void setMrnNo(String mrnNo) {
		MrnNo = mrnNo;
	}
	
	public String getBillNo() {
		return BillNo;
	}
	public void setBillNo(String billNo) {
		BillNo = billNo;
	}
	public String getBilldate() {
		return Billdate;
	}
	public void setBilldate(String billdate) {
		Billdate = billdate;
	}
	public int getMrnStatus() {
		return MrnStatus;
	}
	public void setMrnStatus(int mrnStatus) {
		MrnStatus = mrnStatus;
	}
    
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	@Override
	public String toString() {
		return "ErpHeader [mrnId=" + mrnId + ", mrnDate=" + mrnDate + ", MrnNo=" + MrnNo + ", BillNo=" + BillNo
				+ ", Billdate=" + Billdate + ", MrnStatus=" + MrnStatus + ", vendorId=" + vendorId + ", vendorName="
				+ vendorName + ", poNosList=" + poNosList + "]";
	}
	
	
}
