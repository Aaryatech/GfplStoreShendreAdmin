package com.ats.tril.model;

public class MrnInfo {
	private String id;
	private int mrnId;
	private String mrnDate;
	private String mrnNo;
	private String vendorName;
	private String billNo;
	private String itemDesc;
	private float approveQty;
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
		return mrnNo;
	}
	public void setMrnNo(String mrnNo) {
		this.mrnNo = mrnNo;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public float getApproveQty() {
		return approveQty;
	}
	public void setApproveQty(float approveQty) {
		this.approveQty = approveQty;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "MrnInfo [id=" + id + ", mrnId=" + mrnId + ", mrnDate=" + mrnDate + ", mrnNo=" + mrnNo + ", vendorName="
				+ vendorName + ", billNo=" + billNo + ", itemDesc=" + itemDesc + ", approveQty=" + approveQty + "]";
	}
	
	
}
