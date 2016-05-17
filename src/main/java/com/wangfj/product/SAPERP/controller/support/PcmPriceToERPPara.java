package com.wangfj.product.SAPERP.controller.support;

public class PcmPriceToERPPara {

	/**
	 * 门店(中台的门店编码)
	 */
	private String storeCode;

	/**
	 * 商品编码 电商ERP上传电商商品编码 门店ERP上传的是门店ERP自己的商品编码 导入终端为空
	 */
	private String productCode;

	/**
	 * 中台专柜商品编码 （电商ERP，门店ERP和导入终端上传的是中台供应商商品编码） 电商和导入终端不为空，门店ERP可能传空
	 */
	private String supplierProdCode;

	/**
	 * 售价
	 */
	private String salesPrice;

	/**
	 * 中台专柜编码
	 */
	private String storeCounterCode;

	/**
	 * 货币单位 对于门店ERP是固定值RMB 对于电商ERP则根据系统当前设置提供
	 */
	private String unit;

	/**
	 * 开始日期（包含当天。只到日期，不到时分秒）yyyymmdd
	 */
	private String beginDate;

	/**
	 * 结束日期（包含当天。只到日期，不到时分秒。对于门店ERP则为固定值99991231）
	 */
	private String endDate;

	/**
	 * 变价号（调价单号，主要用于BI统计分析用）电商传值，导入终端传空
	 */
	private String changeCode;

	/**
	 * 零售价、短期价（1标识零售价 2 短期价） 结束日期是9999年12月31日的就是零售价，其它就是短期价
	 */
	private String priceType;

	/**
	 * 本条记录对应的操作 (A添加；U更新；D删除)
	 */
	private String actionCode;

	/**
	 * 操作时间：yyyyMMdd.HHmmssZ
	 */
	private String actionDate;

	/**
	 * 操作人
	 */
	private String actionPerson;

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSupplierProdCode() {
		return supplierProdCode;
	}

	public void setSupplierProdCode(String supplierProdCode) {
		this.supplierProdCode = supplierProdCode;
	}

	public String getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
	}

	public String getStoreCounterCode() {
		return storeCounterCode;
	}

	public void setStoreCounterCode(String storeCounterCode) {
		this.storeCounterCode = storeCounterCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getChangeCode() {
		return changeCode;
	}

	public void setChangeCode(String changeCode) {
		this.changeCode = changeCode;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
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
