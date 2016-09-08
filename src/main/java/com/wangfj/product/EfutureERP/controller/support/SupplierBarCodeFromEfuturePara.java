package com.wangfj.product.EfutureERP.controller.support;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupplierBarCodeFromEfuturePara extends BasePara {
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
	@JsonProperty(value = "SALEPRICE")
	private BigDecimal SALEPRICE; /* 售价 */
	@NotNull(message = "{PushSupplierBarCodeToMDERPDto.actionCode.isNotNull}")
	private String actionCode; /* 本条记录对应的操作 (A添加；U更新；D删除) */
	private String actionDate; /* 操作时间 */
	private String actionPerson; /* 操作人 */

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

	public BigDecimal getSALEPRICE() {
		return SALEPRICE;
	}

	public void setSALEPRICE(BigDecimal sALEPRICE) {
		SALEPRICE = sALEPRICE;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public String getActionPerson() {
		return actionPerson;
	}

	public void setActionPerson(String actionPerson) {
		this.actionPerson = actionPerson;
	}

	@Override
	public String toString() {
		return "SupplierBarCodeFromEfuturePara [STORECODE=" + STORECODE + ", MATNR=" + MATNR
				+ ", LIFNR=" + LIFNR + ", COUNTERCODE=" + COUNTERCODE + ", SBARCODE=" + SBARCODE
				+ ", SBARCODETYPE=" + SBARCODETYPE + ", SBARCODENAME=" + SBARCODENAME
				+ ", SALEUNIT=" + SALEUNIT + ", SALEAMOUNT=" + SALEAMOUNT + ", SALEPRICE="
				+ SALEPRICE + ", actionCode=" + actionCode + ", actionDate=" + actionDate
				+ ", actionPerson=" + actionPerson + "]";
	}
}
