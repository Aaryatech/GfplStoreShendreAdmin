package com.ats.tril.model;

import java.io.Serializable;

public class MrnExcelPuch implements Serializable{

	 private int  mrnDetailId;
	 private int mrnId;
	 private int mrnTypeId;
	 private String mrnType;
	 private String mrnNo;
	 private String mrnDate;
	 private String billNo;
	 private String billDate;
	 private String poNo;
	 private String poDate;
	 private int vendorId;
	 private String type;
	 private int supplierId;
	 private String supplierCode;
	 private String suppErpCode;
	 private String vendorErpCode;
	 private String supplierName;
	 private String gstNo;
	 private String state;
	 private int catId;
	 private int itemId;
	 private String itemName;
	 private String hsnCode;
	 private float qty;
	 private String uom;
	 private float rate;
	 private float amount;
	 private float sgstPer;
	 private float sgstRs;
	 private float cgstPer;
	 private float cgstRs;
	 private float igstPer;
	 private float igstRs;
	 private float cessPer;
	 private float cessRs;
	 private float itemDiscount;
	 private float totalDiscount;
	 private float roundOff;
	 private float totalAmt;
	 private float totalTaxableAmt;
	 private float cgstSum;
	 private float sgstSum;
	 private float igstSum;
	 private float taxAmt;
	 private String remark;
	 
	 
	 private float packValue;
	 
	 private float freightValue;
	 
	 private float insu;
	 
	 private float otherChargesBefor;
	 
	 private float otherChargesAfter;
	 
	 
	 
	public float getPackValue() {
		return packValue;
	}
	public void setPackValue(float packValue) {
		this.packValue = packValue;
	}
	public float getFreightValue() {
		return freightValue;
	}
	public void setFreightValue(float freightValue) {
		this.freightValue = freightValue;
	}
	public float getInsu() {
		return insu;
	}
	public void setInsu(float insu) {
		this.insu = insu;
	}
	public float getOtherChargesBefor() {
		return otherChargesBefor;
	}
	public void setOtherChargesBefor(float otherChargesBefor) {
		this.otherChargesBefor = otherChargesBefor;
	}
	public float getOtherChargesAfter() {
		return otherChargesAfter;
	}
	public void setOtherChargesAfter(float otherChargesAfter) {
		this.otherChargesAfter = otherChargesAfter;
	}
	public int getMrnDetailId() {
		return mrnDetailId;
	}
	public void setMrnDetailId(int mrnDetailId) {
		this.mrnDetailId = mrnDetailId;
	}
	public int getMrnId() {
		return mrnId;
	}
	public void setMrnId(int mrnId) {
		this.mrnId = mrnId;
	}
	public int getMrnTypeId() {
		return mrnTypeId;
	}
	public void setMrnTypeId(int mrnTypeId) {
		this.mrnTypeId = mrnTypeId;
	}
	public String getMrnType() {
		return mrnType;
	}
	public void setMrnType(String mrnType) {
		this.mrnType = mrnType;
	}
	public String getMrnNo() {
		return mrnNo;
	}
	public void setMrnNo(String mrnNo) {
		this.mrnNo = mrnNo;
	}
	public String getMrnDate() {
		return mrnDate;
	}
	public void setMrnDate(String mrnDate) {
		this.mrnDate = mrnDate;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getPoDate() {
		return poDate;
	}
	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSuppErpCode() {
		return suppErpCode;
	}
	public void setSuppErpCode(String suppErpCode) {
		this.suppErpCode = suppErpCode;
	}
	public String getVendorErpCode() {
		return vendorErpCode;
	}
	public void setVendorErpCode(String vendorErpCode) {
		this.vendorErpCode = vendorErpCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getGstNo() {
		return gstNo;
	}
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public float getQty() {
		return qty;
	}
	public void setQty(float qty) {
		this.qty = qty;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getSgstPer() {
		return sgstPer;
	}
	public void setSgstPer(float sgstPer) {
		this.sgstPer = sgstPer;
	}
	public float getSgstRs() {
		return sgstRs;
	}
	public void setSgstRs(float sgstRs) {
		this.sgstRs = sgstRs;
	}
	public float getCgstPer() {
		return cgstPer;
	}
	public void setCgstPer(float cgstPer) {
		this.cgstPer = cgstPer;
	}
	public float getCgstRs() {
		return cgstRs;
	}
	public void setCgstRs(float cgstRs) {
		this.cgstRs = cgstRs;
	}
	public float getIgstPer() {
		return igstPer;
	}
	public void setIgstPer(float igstPer) {
		this.igstPer = igstPer;
	}
	public float getIgstRs() {
		return igstRs;
	}
	public void setIgstRs(float igstRs) {
		this.igstRs = igstRs;
	}
	public float getCessPer() {
		return cessPer;
	}
	public void setCessPer(float cessPer) {
		this.cessPer = cessPer;
	}
	public float getCessRs() {
		return cessRs;
	}
	public void setCessRs(float cessRs) {
		this.cessRs = cessRs;
	}
	public float getItemDiscount() {
		return itemDiscount;
	}
	public void setItemDiscount(float itemDiscount) {
		this.itemDiscount = itemDiscount;
	}
	public float getTotalDiscount() {
		return totalDiscount;
	}
	public void setTotalDiscount(float totalDiscount) {
		this.totalDiscount = totalDiscount;
	}
	public float getRoundOff() {
		return roundOff;
	}
	public void setRoundOff(float roundOff) {
		this.roundOff = roundOff;
	}
	public float getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(float totalAmt) {
		this.totalAmt = totalAmt;
	}
	public float getTotalTaxableAmt() {
		return totalTaxableAmt;
	}
	public void setTotalTaxableAmt(float totalTaxableAmt) {
		this.totalTaxableAmt = totalTaxableAmt;
	}
	public float getCgstSum() {
		return cgstSum;
	}
	public void setCgstSum(float cgstSum) {
		this.cgstSum = cgstSum;
	}
	public float getSgstSum() {
		return sgstSum;
	}
	public void setSgstSum(float sgstSum) {
		this.sgstSum = sgstSum;
	}
	public float getIgstSum() {
		return igstSum;
	}
	public void setIgstSum(float igstSum) {
		this.igstSum = igstSum;
	}
	public float getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(float taxAmt) {
		this.taxAmt = taxAmt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "MrnExcelPuch [mrnDetailId=" + mrnDetailId + ", mrnId=" + mrnId + ", mrnTypeId=" + mrnTypeId
				+ ", mrnType=" + mrnType + ", mrnNo=" + mrnNo + ", mrnDate=" + mrnDate + ", billNo=" + billNo
				+ ", billDate=" + billDate + ", poNo=" + poNo + ", poDate=" + poDate + ", vendorId=" + vendorId
				+ ", type=" + type + ", supplierId=" + supplierId + ", supplierCode=" + supplierCode + ", suppErpCode="
				+ suppErpCode + ", vendorErpCode=" + vendorErpCode + ", supplierName=" + supplierName + ", gstNo="
				+ gstNo + ", state=" + state + ", catId=" + catId + ", itemId=" + itemId + ", itemName=" + itemName
				+ ", hsnCode=" + hsnCode + ", qty=" + qty + ", uom=" + uom + ", rate=" + rate + ", amount=" + amount
				+ ", sgstPer=" + sgstPer + ", sgstRs=" + sgstRs + ", cgstPer=" + cgstPer + ", cgstRs=" + cgstRs
				+ ", igstPer=" + igstPer + ", igstRs=" + igstRs + ", cessPer=" + cessPer + ", cessRs=" + cessRs
				+ ", itemDiscount=" + itemDiscount + ", totalDiscount=" + totalDiscount + ", roundOff=" + roundOff
				+ ", totalAmt=" + totalAmt + ", totalTaxableAmt=" + totalTaxableAmt + ", cgstSum=" + cgstSum
				+ ", sgstSum=" + sgstSum + ", igstSum=" + igstSum + ", taxAmt=" + taxAmt + ", remark=" + remark
				+ ", packValue=" + packValue + ", freightValue=" + freightValue + ", insu=" + insu
				+ ", otherChargesBefor=" + otherChargesBefor + ", otherChargesAfter=" + otherChargesAfter + "]";
	}
	 
	 
	 
}
