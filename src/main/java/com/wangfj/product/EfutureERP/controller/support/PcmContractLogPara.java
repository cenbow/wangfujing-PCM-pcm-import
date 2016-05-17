package com.wangfj.product.EfutureERP.controller.support;

import javax.validation.constraints.NotNull;

public class PcmContractLogPara {

	@NotNull(message = "{PcmContractLogPara.contractCode.isNotNull}")
	private String contractCode; // 要约号

	@NotNull(message = "{PcmContractLogPara.storeCode.isNotNull}")
	private String storeCode;// 门店编码

	@NotNull(message = "{PcmContractLogPara.supplyCode.isNotNull}")
	private String supplierCode;// 供应商编码

	@NotNull(message = "{PcmContractLogPara.manageType.isNotNull}")
	private String manageType;// 经营方式(经销9,代销2，联营0，租赁3)

	@NotNull(message = "{PcmContractLogPara.buyType.isNotNull}")
	private String buyType;// 采购类别（总部集采“1”、城市集采“2”、门店经营“3”）

	@NotNull(message = "{PcmContractLogPara.operMode.isNotNull}")
	private String operMode;// 管理方式（单品“1” 金饰“5”服务费“6”租赁“7”）

	@NotNull(message = "{PcmContractLogPara.businessType.isNotNull}")
	private String businessType;// 业态类型（百货:E 超市:C）

	@NotNull(message = "{PcmContractLogPara.inputTax.isNotNull}")
	private String inputTax;// 进项税率（0.17,0.13,0.05.......）

	@NotNull(message = "{PcmContractLogPara.outputTax.isNotNull}")
	private String outputTax; // 销项税率(0.17,0.13,0.05....)

	@NotNull(message = "{PcmContractLogPara.commissionRate.isNotNull}")
	private String commissionRate;// 要约扣率

	private String manageCategory;// 管理分类

	@NotNull(message = "{PcmContractLogPara.actionCode.isNotNull}")
	private String actionCode;// 本条记录对应的操作 (A添加；U更新；D删除)

	private String actionDate;// 操作时间（格式为yyyyMMdd.HHmmssZ，结果形如”
								// 20141008.101603+0800”）

	private String actionPerson;// 操作人

	public String getManageCategory() {
		return manageCategory;
	}

	public void setManageCategory(String manageCategory) {
		this.manageCategory = manageCategory;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public String getOperMode() {
		return operMode;
	}

	public void setOperMode(String operMode) {
		this.operMode = operMode;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getInputTax() {
		return inputTax;
	}

	public void setInputTax(String inputTax) {
		this.inputTax = inputTax;
	}

	public String getOutputTax() {
		return outputTax;
	}

	public void setOutputTax(String outputTax) {
		this.outputTax = outputTax;
	}

	public String getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(String commissionRate) {
		this.commissionRate = commissionRate;
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

}