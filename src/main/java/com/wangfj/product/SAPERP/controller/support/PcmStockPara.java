package com.wangfj.product.SAPERP.controller.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PcmStockPara {
	/**
	 * @Field long serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private Long sid;
	/*
	 * sku
	 */
	@JsonProperty(value = "SKU")
	private String SKU;
	/*
	 * 供应商商品编码
	 */
	@JsonProperty(value = "SUPPLYPRODUCTID")
	private String SUPPLYPRODUCTID;
	/*
	 * 库存地点编码
	 */
	@JsonProperty(value = "LOCATION")
	private String LOCATION;
	/*
	 * 出货主体编号
	 */
	@JsonProperty(value = "LOCATIONOWNERID")
	private String LOCATIONOWNERID;
	/*
	 * 库存数
	 */
	@JsonProperty(value = "INVENTORY")
	private Integer INVENTORY;
	/*
	 * 借用库存数
	 */
	@JsonProperty(value = "BORROWINVENTORY")
	private Integer BORROWINVENTORY;
	/*
	 * 残次品库存数
	 */
	@JsonProperty(value = "DEFECTIVEINVENTORY")
	private Integer DEFECTIVEINVENTORY;
	/*
	 * 停售库存数
	 */
	@JsonProperty(value = "STOPSALESINVENTORY")
	private Integer STOPSALESINVENTORY;
	/*
	 * 安全库存数
	 */
	@JsonProperty(value = "WARNINGINVENTORY")
	private Integer WARNINGINVENTORY;
	/*
	 * 改动类型
	 */
	@JsonProperty(value = "TYPE")
	private String TYPE;
	/*
	 * 调用方
	 */
	@JsonProperty(value = "SOURCE")
	private String SOURCE;
	/*
	 * 操作人
	 */
	@JsonProperty(value = "OPERATOR")
	private String OPERATOR;
	/**
	 * guid
	 */
	@JsonProperty(value = "GUID")
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
