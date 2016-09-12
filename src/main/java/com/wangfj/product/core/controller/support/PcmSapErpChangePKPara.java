package com.wangfj.product.core.controller.support;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PcmSapErpChangePKPara {

	@JsonProperty(value = "PRODUCT")
	private String PRODUCT;

	@JsonProperty(value = "VALUE")
	private String VALUE;

	@JsonProperty(value = "XGLB")
	private String XGLB;

	@JsonProperty(value = "STORE")
	private String STORE;

	@JsonProperty(value = "SEQNO")
	private String SEQNO;

	@JsonProperty(value = "ROWNO")
	private String ROWNO;

	@JsonProperty(value = "DATE")
	private String DATE;

	@JsonProperty(value = "ACTION_CODE")
	private String ACTION_CODE;

	@JsonProperty(value = "ACTION_DATE")
	private String ACTION_DATE;

	@JsonProperty(value = "ACTION_PERSON")
	private String ACTION_PERSON;

	public String getPRODUCT() {
		return PRODUCT;
	}

	public void setPRODUCT(String pRODUCT) {
		PRODUCT = pRODUCT;
	}

	public String getVALUE() {
		return VALUE;
	}

	public void setVALUE(String vALUE) {
		VALUE = vALUE;
	}

	public String getXGLB() {
		return XGLB;
	}

	public void setXGLB(String xGLB) {
		XGLB = xGLB;
	}

	public String getSTORE() {
		return STORE;
	}

	public void setSTORE(String sTORE) {
		STORE = sTORE;
	}

	public String getSEQNO() {
		return SEQNO;
	}

	public void setSEQNO(String sEQNO) {
		SEQNO = sEQNO;
	}

	public String getROWNO() {
		return ROWNO;
	}

	public void setROWNO(String rOWNO) {
		ROWNO = rOWNO;
	}

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
	}

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

}
