package com.wangfj.product.EfutureERP.controller.support;

import javax.validation.constraints.NotNull;

/**
 * ERP商品改基本信息para
 * 
 * @Class Name ErpChangePara
 * @Author zhangxy
 * @Create In 2015年7月16日
 */
public class ErpChangePara {
	/**
	 * 门店
	 */
	@NotNull(message = "{ErpChangePara.STORE.isNotNull}")
	private String STORE;
	/**
	 * 单据号
	 */
	@NotNull(message = "{ErpChangePara.SEQNO.isNotNull}")
	private String SEQNO;
	/**
	 * 行号
	 */
	@NotNull(message = "{ErpChangePara.ROWNO.isNotNull}")
	private String ROWNO;
	/**
	 * 单据类别
	 */
	@NotNull(message = "{ErpChangePara.XGLB.isNotNull}")
	private String XGLB;
	/**
	 * 商品编码
	 */
	@NotNull(message = "{ErpChangePara.PRODUCT.isNotNull}")
	private String PRODUCT;
	/**
	 * 新值
	 */
	@NotNull(message = "{ErpChangePara.VALUE.isNotNull}")
	private String VALUE;
	/**
	 * 生效日期
	 */
	@NotNull(message = "{ErpChangePara.DATE.isNotNull}")
	private String DATE;
	@NotNull(message = "{ErpChangePara.actionCode.isNotNull}")
	private String actionCode;
	private String actionDate;
	private String actionPerson;
	private String ACTION_CODE;
	private String ACTION_DATE;
	private String ACTION_PERSON;

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

	public String getXGLB() {
		return XGLB;
	}

	public void setXGLB(String xGLB) {
		XGLB = xGLB;
	}

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

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
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
