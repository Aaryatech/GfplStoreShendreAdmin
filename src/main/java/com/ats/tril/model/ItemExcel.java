package com.ats.tril.model;

public class ItemExcel {

	private int itemId;

	private int catId;

	private int grpId;

	private String catDesc;

	private String grpDesc;

	private String itemUom;

	private String itemUom2;

	private String itemDesc;

	private float taxPer;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public int getGrpId() {
		return grpId;
	}

	public void setGrpId(int grpId) {
		this.grpId = grpId;
	}

	public String getCatDesc() {
		return catDesc;
	}

	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}

	public String getGrpDesc() {
		return grpDesc;
	}

	public void setGrpDesc(String grpDesc) {
		this.grpDesc = grpDesc;
	}

	public String getItemUom() {
		return itemUom;
	}

	public void setItemUom(String itemUom) {
		this.itemUom = itemUom;
	}

	public String getItemUom2() {
		return itemUom2;
	}

	public void setItemUom2(String itemUom2) {
		this.itemUom2 = itemUom2;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public float getTaxPer() {
		return taxPer;
	}

	public void setTaxPer(float taxPer) {
		this.taxPer = taxPer;
	}

	@Override
	public String toString() {
		return "ItemExcel [itemId=" + itemId + ", catId=" + catId + ", grpId=" + grpId + ", catDesc=" + catDesc
				+ ", grpDesc=" + grpDesc + ", itemUom=" + itemUom + ", itemUom2=" + itemUom2 + ", itemDesc=" + itemDesc
				+ ", taxPer=" + taxPer + "]";
	}

}
