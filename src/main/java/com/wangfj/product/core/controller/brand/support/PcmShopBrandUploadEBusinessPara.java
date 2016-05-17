package com.wangfj.product.core.controller.brand.support;

public class PcmShopBrandUploadEBusinessPara {

	private String STORECODE;// 门店编码

	private String BRANDCODE;// 门店品牌编码

	private String BRANDNAME;// 门店品牌名称

	private String STORETYPE;// 门店类型（0-全局，1-电商、2-北京，3-其他门店）

	private String BRANDNAMESECOND;// 品牌第二名称

	private String BRANDNAMESPELL;// 品牌中文拼音

	private String BRANDNAMEEN;// 品牌英文名称

	private String ISDISPLAY;// 是否展示（0：是，1：否）

	private String BRANDPIC1;// logo图片

	private String BRANDPIC2;// banner图片

	private String BRANDDESC;// 品牌描述

	private String ACTIONCODE;// 本条记录对应的操作 (A添加；U更新；D删除)

	private String ACTIONDATE;// 操作时间

	private String ACTIONPERSON;// 操作人

	public String getSTORECODE() {
		return STORECODE;
	}

	public void setSTORECODE(String sTORECODE) {
		STORECODE = sTORECODE;
	}

	public String getBRANDCODE() {
		return BRANDCODE;
	}

	public void setBRANDCODE(String bRANDCODE) {
		BRANDCODE = bRANDCODE;
	}

	public String getBRANDNAME() {
		return BRANDNAME;
	}

	public void setBRANDNAME(String bRANDNAME) {
		BRANDNAME = bRANDNAME;
	}

	public String getSTORETYPE() {
		return STORETYPE;
	}

	public void setSTORETYPE(String sTORETYPE) {
		STORETYPE = sTORETYPE;
	}

	public String getBRANDNAMESECOND() {
		return BRANDNAMESECOND;
	}

	public void setBRANDNAMESECOND(String bRANDNAMESECOND) {
		BRANDNAMESECOND = bRANDNAMESECOND;
	}

	public String getBRANDNAMESPELL() {
		return BRANDNAMESPELL;
	}

	public void setBRANDNAMESPELL(String bRANDNAMESPELL) {
		BRANDNAMESPELL = bRANDNAMESPELL;
	}

	public String getBRANDNAMEEN() {
		return BRANDNAMEEN;
	}

	public void setBRANDNAMEEN(String bRANDNAMEEN) {
		BRANDNAMEEN = bRANDNAMEEN;
	}

	public String getISDISPLAY() {
		return ISDISPLAY;
	}

	public void setISDISPLAY(String iSDISPLAY) {
		ISDISPLAY = iSDISPLAY;
	}

	public String getBRANDPIC1() {
		return BRANDPIC1;
	}

	public void setBRANDPIC1(String bRANDPIC1) {
		BRANDPIC1 = bRANDPIC1;
	}

	public String getBRANDPIC2() {
		return BRANDPIC2;
	}

	public void setBRANDPIC2(String bRANDPIC2) {
		BRANDPIC2 = bRANDPIC2;
	}

	public String getBRANDDESC() {
		return BRANDDESC;
	}

	public void setBRANDDESC(String bRANDDESC) {
		BRANDDESC = bRANDDESC;
	}

	public String getACTIONCODE() {
		return ACTIONCODE;
	}

	public void setACTIONCODE(String aCTIONCODE) {
		ACTIONCODE = aCTIONCODE;
	}

	public String getACTIONDATE() {
		return ACTIONDATE;
	}

	public void setACTIONDATE(String aCTIONDATE) {
		ACTIONDATE = aCTIONDATE;
	}

	public String getACTIONPERSON() {
		return ACTIONPERSON;
	}

	public void setACTIONPERSON(String aCTIONPERSON) {
		ACTIONPERSON = aCTIONPERSON;
	}

	@Override
	public String toString() {
		return "PcmShopBrandUploadEBusinessPara [STORECODE=" + STORECODE + ", BRANDCODE="
				+ BRANDCODE + ", BRANDNAME=" + BRANDNAME + ", STORETYPE=" + STORETYPE
				+ ", BRANDNAMESECOND=" + BRANDNAMESECOND + ", BRANDNAMESPELL=" + BRANDNAMESPELL
				+ ", BRANDNAMEEN=" + BRANDNAMEEN + ", ISDISPLAY=" + ISDISPLAY + ", BRANDPIC1="
				+ BRANDPIC1 + ", BRANDPIC2=" + BRANDPIC2 + ", BRANDDESC=" + BRANDDESC
				+ ", ACTIONCODE=" + ACTIONCODE + ", ACTIONDATE=" + ACTIONDATE + ", ACTIONPERSON="
				+ ACTIONPERSON + "]";
	}

}
