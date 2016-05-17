package com.wangfj.product.SAPERP.controller.support;

public class PcmStockPara {
	/**
	 * @Field long serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private Long sid;
	/*
	 * sku
	 */
	private String SKU;
	/*
	 * 供应商商品编码
	 */
	private String SUPPLYPRODUCTID;
	/*
	 * 库存地点编码
	 */
	private String LOCATION;
	/*
	 * 出货主体编号
	 */
	private String LOCATIONOWNERID;
	/*
	 * 库存数
	 */
	private Integer INVENTORY;
	/*
	 * 借用库存数
	 */
	private Integer BORROWINVENTORY;
	/*
	 * 残次品库存数
	 */
	private Integer DEFECTIVEINVENTORY;
	/*
	 * 停售库存数
	 */
	private Integer STOPSALESINVENTORY;
	/*
	 * 安全库存数
	 */
	private Integer WARNINGINVENTORY;
	/*
	 * 改动类型
	 */
	private String TYPE;
	/*
	 * 调用方
	 */
	private String SOURCE;
	/*
	 * 操作人
	 */
	private String OPERATOR;
	/**
	 * guid
	 */
	private String GUID;

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getSKU() {
		return SKU;
	}

	public void setSKU(String sKU) {
		SKU = sKU;
	}

	public String getSUPPLYPRODUCTID() {
		return SUPPLYPRODUCTID;
	}

	public void setSUPPLYPRODUCTID(String sUPPLYPRODUCTID) {
		SUPPLYPRODUCTID = sUPPLYPRODUCTID;
	}

	public String getLOCATION() {
		return LOCATION;
	}

	public void setLOCATION(String lOCATION) {
		LOCATION = lOCATION;
	}

	public String getLOCATIONOWNERID() {
		return LOCATIONOWNERID;
	}

	public void setLOCATIONOWNERID(String lOCATIONOWNERID) {
		LOCATIONOWNERID = lOCATIONOWNERID;
	}

	public Integer getINVENTORY() {
		return INVENTORY;
	}

	public void setINVENTORY(Integer iNVENTORY) {
		INVENTORY = iNVENTORY;
	}

	public Integer getBORROWINVENTORY() {
		return BORROWINVENTORY;
	}

	public void setBORROWINVENTORY(Integer bORROWINVENTORY) {
		BORROWINVENTORY = bORROWINVENTORY;
	}

	public Integer getDEFECTIVEINVENTORY() {
		return DEFECTIVEINVENTORY;
	}

	public void setDEFECTIVEINVENTORY(Integer dEFECTIVEINVENTORY) {
		DEFECTIVEINVENTORY = dEFECTIVEINVENTORY;
	}

	public Integer getSTOPSALESINVENTORY() {
		return STOPSALESINVENTORY;
	}

	public void setSTOPSALESINVENTORY(Integer sTOPSALESINVENTORY) {
		STOPSALESINVENTORY = sTOPSALESINVENTORY;
	}

	public Integer getWARNINGINVENTORY() {
		return WARNINGINVENTORY;
	}

	public void setWARNINGINVENTORY(Integer wARNINGINVENTORY) {
		WARNINGINVENTORY = wARNINGINVENTORY;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getSOURCE() {
		return SOURCE;
	}

	public void setSOURCE(String sOURCE) {
		SOURCE = sOURCE;
	}

	public String getOPERATOR() {
		return OPERATOR;
	}

	public void setOPERATOR(String oPERATOR) {
		OPERATOR = oPERATOR;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "PcmStockPara [sid=" + sid + ", SKU=" + SKU + ", SUPPLYPRODUCTID=" + SUPPLYPRODUCTID
				+ ", LOCATION=" + LOCATION + ", LOCATIONOWNERID=" + LOCATIONOWNERID + ", INVENTORY="
				+ INVENTORY + ", BORROWINVENTORY=" + BORROWINVENTORY + ", DEFECTIVEINVENTORY="
				+ DEFECTIVEINVENTORY + ", STOPSALESINVENTORY=" + STOPSALESINVENTORY
				+ ", WARNINGINVENTORY=" + WARNINGINVENTORY + ", TYPE=" + TYPE + ", SOURCE=" + SOURCE
				+ ", OPERATORS=" + OPERATOR + ", GUID=" + GUID + "]";
	}

}
