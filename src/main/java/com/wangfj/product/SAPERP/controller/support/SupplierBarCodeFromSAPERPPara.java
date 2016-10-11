package com.wangfj.product.SAPERP.controller.support;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierBarCodeFromSAPERPPara {
	@NotNull(message = "{PushSupplierBarCodeToMDERPDto.STORECODE.isNotNull}")
	@JsonProperty(value = "STORECODE")
	private String STORECODE; /* 门店(中台的门店编码) */
	@NotNull(message = "{PushSupplierBarCodeToMDERPDto.MATNR.isNotNull}")
	@JsonProperty(value = "MATNR")
	private String MATNR; /* 商品的ERP编码 */
	@NotNull(message = "{PushSupplierBarCodeToMDERPDto.LIFNR.isNotNull}")
	@JsonProperty(value = "LIFNR")
	private String LIFNR; /* 供应商编码 */
	@JsonProperty(value = "COUNTERCODE")
	private String COUNTERCODE; /* 专柜编码 */
	@NotNull(message = "{PushSupplierBarCodeToMDERPDto.SBARCODE.isNotNull}")
	@JsonProperty(value = "SBARCODE")
	private String SBARCODE; /* 供应商商品条码 */
	@JsonProperty(value = "SBARCODETYPE")
	private String SBARCODETYPE; /* 条码类型 */
	@JsonProperty(value = "SBARCODENAME")
	private String SBARCODENAME; /* 条码名称 */
	@JsonProperty(value = "SALEUNIT")
	private String SALEUNIT; /* 销售单位 */
	@JsonProperty(value = "SALEAMOUNT")
	private String SALEAMOUNT; /* 多包装的含量 */
	// @JsonProperty(value = "SALEPRICE")
	// private BigDecimal SALEPRICE; /* 售价 */
	@NotNull(message = "{PushSupplierBarCodeToMDERPDto.ACTIONCODE.isNotNull}")
	@JsonProperty(value = "ACTION_CODE")
	private String ACTION_CODE; /* 本条记录对应的操作 (A添加；U更新；D删除) */
	@JsonProperty(value = "ACTION_DATE")
	private String ACTION_DATE; /* 操作时间 */
	@JsonProperty(value = "ACTION_PERSON")
	private String ACTION_PERSON; /* 操作人 */

	public String getSTORECODE() {
		return STORECODE;
	}

	public void setSTORECODE(String sTORECODE) {
		STORECODE = sTORECODE;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getLIFNR() {
		return LIFNR;
	}

	public void setLIFNR(String lIFNR) {
		LIFNR = lIFNR;
	}

	public String getCOUNTERCODE() {
		return COUNTERCODE;
	}

	public void setCOUNTERCODE(String cOUNTERCODE) {
		COUNTERCODE = cOUNTERCODE;
	}

	public String getSBARCODE() {
		return SBARCODE;
	}

	public void setSBARCODE(String sBARCODE) {
		SBARCODE = sBARCODE;
	}

	public String getSBARCODETYPE() {
		return SBARCODETYPE;
	}

	public void setSBARCODETYPE(String sBARCODETYPE) {
		SBARCODETYPE = sBARCODETYPE;
	}

	public String getSBARCODENAME() {
		return SBARCODENAME;
	}

	public void setSBARCODENAME(String sBARCODENAME) {
		SBARCODENAME = sBARCODENAME;
	}

	public String getSALEUNIT() {
		return SALEUNIT;
	}

	public void setSALEUNIT(String sALEUNIT) {
		SALEUNIT = sALEUNIT;
	}

	public String getSALEAMOUNT() {
		return SALEAMOUNT;
	}

	public void setSALEAMOUNT(String sALEAMOUNT) {
		SALEAMOUNT = sALEAMOUNT;
	}

	/*
	 * public BigDecimal getSALEPRICE() { return SALEPRICE; }
	 * 
	 * public void setSALEPRICE(BigDecimal sALEPRICE) { SALEPRICE = sALEPRICE; }
	 */

	public String getACTION_CODE() {
		return ACTION_CODE;
	}

	public void setACTION_CODE(String aCTION_CODE) {
		ACTION_CODE = aCTION_CODE;
	}

	public String getACTION_DATE() {
		return ACTION_DATE;
	}

	public void setACTION_DATE(String aCTION_DATE) {
		ACTION_DATE = aCTION_DATE;
	}

	public String getACTION_PERSON() {
		return ACTION_PERSON;
	}

	public void setACTION_PERSON(String aCTION_PERSON) {
		ACTION_PERSON = aCTION_PERSON;
	}

	@Override
	public String toString() {
		return "SupplierBarCodeFromSAPERPPara [STORECODE=" + STORECODE + ", MATNR=" + MATNR
				+ ", LIFNR=" + LIFNR + ", COUNTERCODE=" + COUNTERCODE + ", SBARCODE=" + SBARCODE
				+ ", SBARCODETYPE=" + SBARCODETYPE + ", SBARCODENAME=" + SBARCODENAME
				+ ", SALEUNIT=" + SALEUNIT + ", SALEAMOUNT=" + SALEAMOUNT + ", ACTION_CODE="
				+ ACTION_CODE + ", ACTION_DATE=" + ACTION_DATE + ", ACTION_PERSON=" + ACTION_PERSON
				+ "]";
	}

}
