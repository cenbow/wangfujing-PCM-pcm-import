package com.wangfj.product.SAPERP.controller.support;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonProperty;

public class SaveProductParaSAPERP {
	@NotNull(message = "要约号不能为空")
	@JsonProperty(value = "CONTRACTCODE")
	private String CONTRACTCODE; // 要约号

	@NotNull(message = "门店编码不能为空")
	@JsonProperty(value = "STORECODE")
	private String STORECODE;// 门店编码

	@NotNull(message = "供应商编码不能为空")
	@JsonProperty(value = "SUPPLIERCODE")
	private String SUPPLIERCODE;// 供应商编码

	@NotNull(message = "经营方式不能为空")
	@JsonProperty(value = "MANAGETYPE")
	private String MANAGETYPE;// 经营方式(经销9,代销2，联营0，租赁3)

	@NotNull(message = "采购类别不能为空")
	@JsonProperty(value = "BUYTYPE")
	private String BUYTYPE;// 采购类别（总部集采“1”、城市集采“2”、门店经营“3”）

	@NotNull(message = "管理方式不能为空")
	@JsonProperty(value = "OPERMODE")
	private String OPERMODE;// 管理方式（单品“1” 金饰“5”服务费“6”租赁“7”）

	@NotNull(message = "业态类型不能为空")
	@JsonProperty(value = "BUSINESSTYPE")
	private String BUSINESSTYPE;// 业态类型（百货:E 超市:C）

	@NotNull(message = "进项税率不能为空")
	@JsonProperty(value = "INPUTTAX")
	private String INPUTTAX;// 进项税率（0.17,0.13,0.05.......）

	@JsonProperty(value = "OUTPUTTAX")
	private String OUTPUTTAX; // 销项税率(0.17,0.13,0.05....)

	@JsonProperty(value = "COMMISSIONRATE")
	private String COMMISSIONRATE;// 要约扣率

	@NotNull(message = "actionCode不能为空")
	@JsonProperty(value = "ACTIONCODE")
	private String ACTIONCODE;// 本条记录对应的操作 (A添加；U更新；D删除)

	@JsonProperty(value = "ACTIONDATE")
	private String ACTIONDATE;// 操作时间（格式为yyyyMMdd.HHmmssZ，结果形如”
								// 20141008.101603+0800”）

	@JsonProperty(value = "ACTIONPERSON")
	private String ACTIONPERSON;// 操作人

	@JsonProperty(value = "PRODUCTS")
	private List<ProductsSAPERP> PRODUCTS;

	public String getCONTRACTCODE() {
		return CONTRACTCODE;
	}

	public void setCONTRACTCODE(String cONTRACTCODE) {
		CONTRACTCODE = cONTRACTCODE;
	}

	public String getSTORECODE() {
		return STORECODE;
	}

	public void setSTORECODE(String sTORECODE) {
		STORECODE = sTORECODE;
	}

	public String getSUPPLIERCODE() {
		return SUPPLIERCODE;
	}

	public void setSUPPLIERCODE(String sUPPLIERCODE) {
		SUPPLIERCODE = sUPPLIERCODE;
	}

	public String getMANAGETYPE() {
		return MANAGETYPE;
	}

	public void setMANAGETYPE(String mANAGETYPE) {
		MANAGETYPE = mANAGETYPE;
	}

	public String getBUYTYPE() {
		return BUYTYPE;
	}

	public void setBUYTYPE(String bUYTYPE) {
		BUYTYPE = bUYTYPE;
	}

	public String getOPERMODE() {
		return OPERMODE;
	}

	public void setOPERMODE(String oPERMODE) {
		OPERMODE = oPERMODE;
	}

	public String getBUSINESSTYPE() {
		return BUSINESSTYPE;
	}

	public void setBUSINESSTYPE(String bUSINESSTYPE) {
		BUSINESSTYPE = bUSINESSTYPE;
	}

	public String getINPUTTAX() {
		return INPUTTAX;
	}

	public void setINPUTTAX(String iNPUTTAX) {
		INPUTTAX = iNPUTTAX;
	}

	public String getOUTPUTTAX() {
		return OUTPUTTAX;
	}

	public void setOUTPUTTAX(String oUTPUTTAX) {
		OUTPUTTAX = oUTPUTTAX;
	}

	public String getCOMMISSIONRATE() {
		return COMMISSIONRATE;
	}

	public void setCOMMISSIONRATE(String cOMMISSIONRATE) {
		COMMISSIONRATE = cOMMISSIONRATE;
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

	public List<ProductsSAPERP> getPRODUCTS() {
		return PRODUCTS;
	}

	public void setPRODUCTS(List<ProductsSAPERP> pRODUCTS) {
		PRODUCTS = pRODUCTS;
	}

	@Override
	public String toString() {
		return "SaveProductParaSAPERP [CONTRACTCODE=" + CONTRACTCODE + ", STORECODE=" + STORECODE
				+ ", SUPPLIERCODE=" + SUPPLIERCODE + ", MANAGETYPE=" + MANAGETYPE + ", BUYTYPE="
				+ BUYTYPE + ", OPERMODE=" + OPERMODE + ", BUSINESSTYPE=" + BUSINESSTYPE
				+ ", INPUTTAX=" + INPUTTAX + ", OUTPUTTAX=" + OUTPUTTAX + ", COMMISSIONRATE="
				+ COMMISSIONRATE + ", ACTIONCODE=" + ACTIONCODE + ", ACTIONDATE=" + ACTIONDATE
				+ ", ACTIONPERSON=" + ACTIONPERSON + ", PRODUCTS=" + PRODUCTS + "]";
	}

}
