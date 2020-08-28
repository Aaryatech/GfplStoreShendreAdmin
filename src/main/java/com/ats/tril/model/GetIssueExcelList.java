package com.ats.tril.model;

public class GetIssueExcelList {

	private int issueId;
	private String issueDate;
	private String issueSlipNo;
	private int issueDetailId;
	private int itemId;
	private String itemDesc;
	private int itemIssueQty;
	private String itemUom;
	private String batchNo;
	private float itemRate;
	public int getIssueId() {
		return issueId;
	}
	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getIssueSlipNo() {
		return issueSlipNo;
	}
	public void setIssueSlipNo(String issueSlipNo) {
		this.issueSlipNo = issueSlipNo;
	}
	public int getIssueDetailId() {
		return issueDetailId;
	}
	public void setIssueDetailId(int issueDetailId) {
		this.issueDetailId = issueDetailId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public int getItemIssueQty() {
		return itemIssueQty;
	}
	public void setItemIssueQty(int itemIssueQty) {
		this.itemIssueQty = itemIssueQty;
	}
	public String getItemUom() {
		return itemUom;
	}
	public void setItemUom(String itemUom) {
		this.itemUom = itemUom;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public float getItemRate() {
		return itemRate;
	}
	public void setItemRate(float itemRate) {
		this.itemRate = itemRate;
	}
	@Override
	public String toString() {
		return "GetIssueExcelList [issueId=" + issueId + ", issueDate=" + issueDate + ", issueSlipNo=" + issueSlipNo
				+ ", issueDetailId=" + issueDetailId + ", itemId=" + itemId + ", itemDesc=" + itemDesc
				+ ", itemIssueQty=" + itemIssueQty + ", itemUom=" + itemUom + ", batchNo=" + batchNo + ", itemRate="
				+ itemRate + "]";
	}
	
	
}
