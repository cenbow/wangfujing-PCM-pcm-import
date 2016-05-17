package com.wangfj.product.EfutureERP.controller.support;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

/**
 * ERP商品信息DTO
 * 
 * @Class Name PcmErpProductDto
 * @Author zhangxy
 * @Create In 2015年7月9日
 */
public class GetErpProductFromEfuturePara extends BasePara {
	private String storeCode;
	/**
	 * 专柜编码
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.counterCode.isNotNull}")
	private String counterCode;
	/**
	 * 商品编码（大码）
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.productCode.isNotNull}")
	private String productCode;
	/**
	 * 大码类型:0单品码,1长期统码,2特卖统码,3促销统码,4促销扣率码,5价格码,6扣率码
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.skuType.isNotNull}")
	private String skuType;
	/**
	 * 门店供应商编码
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.supplierCode.isNotNull}")
	private String supplierCode;
	/**
	 * 门店品牌编码
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.brandCode.isNotNull}")
	private String brandCode;
	/**
	 * 商品名称
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.productName.isNotNull}")
	private String productName;
	/**
	 * 商品经营方式
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.productType.isNotNull}")
	private String productType;
	/**
	 * 商品类别:01 自营单品,05 金银首饰,06 服务费商品,07 租赁商品,,08 联营单品,09 组包码
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.productCategory.isNotNull}")
	private String productCategory;
	/**
	 * 规格名称（例如250ml）
	 */
	private String specName;
	/**
	 * 货号
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.articleNum.isNotNull}")
	private String articleNum;
	/**
	 * 销售计量单位（上传的是单位的文本信息，例如瓶）
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.salesUnit.isNotNull}")
	private String salesUnit;
	/**
	 * 供应商条码
	 */
	private String supplierBarcode;
	/**
	 * 售价
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.salesPrice.isNotNull}")
	private BigDecimal salesPrice;
	/**
	 * 进项税
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.inputTax.isNotNull}")
	private BigDecimal inputTax;
	/**
	 * 销项税
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.outputTax.isNotNull}")
	private BigDecimal outputTax;
	/**
	 * 消费税
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.salesTax.isNotNull}")
	private BigDecimal salesTax;
	/**
	 * 商品状态(0正常；1停售；2停货；3暂停使用；4已删除；5淘汰)
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.status.isNotNull}")
	private String status;
	/**
	 * 是否允许促销(0 允许 , 1 不允许)
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.isPromotion.isNotNull}")
	private String isPromotion;
	/**
	 * 是否允许调价(0 允许 ,1 不允许)
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.isAdjustPrice.isNotNull}")
	private String isAdjustPrice;
	/**
	 * 所属末级管理分类的编码
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.manageCategory.isNotNull}")
	private String manageCategory;
	/**
	 * 折扣底限(0到1.00之间的小数)
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.discountLimit.isNotNull}")
	private BigDecimal discountLimit;
	/**
	 * 产地（文本信息）
	 */
	private String originLand;
	/**
	 * 服务费类型
	 */
	private String serviceFeeType;
	/**
	 * 业态类型（1百货、2超市、3电商）
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.formatType.isNotNull}")
	private String formatType;
	/**
	 * 所属末级统计分类的编码
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.statCategory.isNotNull}")
	private String statCategory;
	/**
	 * 标准扣率（0到100之间的百分数，最多六位小数，只传数值不传%号，比如20.123%只传20.123。）
	 */
	private BigDecimal commissionRate;
	/**
	 * 操作代码
	 */
	@NotNull(message = "{GetErpProductFromEfuturePara.actionCode.isNotNull}")
	private String actionCode;
	/**
	 * 操作日期
	 */
	private String actionDate;
	/**
	 * 操作员标示
	 */
	private String actionPerson;

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSkuType() {
		return skuType;
	}

	public void setSkuType(String skuType) {
		this.skuType = skuType;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(String articleNum) {
		this.articleNum = articleNum;
	}

	public String getSalesUnit() {
		return salesUnit;
	}

	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}

	public String getSupplierBarcode() {
		return supplierBarcode;
	}

	public void setSupplierBarcode(String supplierBarcode) {
		this.supplierBarcode = supplierBarcode;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	public BigDecimal getInputTax() {
		return inputTax;
	}

	public void setInputTax(BigDecimal inputTax) {
		this.inputTax = inputTax;
	}

	public BigDecimal getOutputTax() {
		return outputTax;
	}

	public void setOutputTax(BigDecimal outputTax) {
		this.outputTax = outputTax;
	}

	public BigDecimal getSalesTax() {
		return salesTax;
	}

	public void setSalesTax(BigDecimal salesTax) {
		this.salesTax = salesTax;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsPromotion() {
		return isPromotion;
	}

	public void setIsPromotion(String isPromotion) {
		this.isPromotion = isPromotion;
	}

	public String getIsAdjustPrice() {
		return isAdjustPrice;
	}

	public void setIsAdjustPrice(String isAdjustPrice) {
		this.isAdjustPrice = isAdjustPrice;
	}

	public String getManageCategory() {
		return manageCategory;
	}

	public void setManageCategory(String manageCategory) {
		this.manageCategory = manageCategory;
	}

	public BigDecimal getDiscountLimit() {
		return discountLimit;
	}

	public void setDiscountLimit(BigDecimal discountLimit) {
		this.discountLimit = discountLimit;
	}

	public String getOriginLand() {
		return originLand;
	}

	public void setOriginLand(String originLand) {
		this.originLand = originLand;
	}

	public String getServiceFeeType() {
		return serviceFeeType;
	}

	public void setServiceFeeType(String serviceFeeType) {
		this.serviceFeeType = serviceFeeType;
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	public String getStatCategory() {
		return statCategory;
	}

	public void setStatCategory(String statCategory) {
		this.statCategory = statCategory;
	}

	public BigDecimal getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(BigDecimal commissionRate) {
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

	@Override
	public String toString() {
		return "GetErpProductFromEfuturePara [storeCode=" + storeCode + ", counterCode="
				+ counterCode + ", productCode=" + productCode + ", skuType=" + skuType
				+ ", supplierCode=" + supplierCode + ", brandCode=" + brandCode + ", productName="
				+ productName + ", productType=" + productType + ", productCategory="
				+ productCategory + ", specName=" + specName + ", articleNum=" + articleNum
				+ ", salesUnit=" + salesUnit + ", supplierBarcode=" + supplierBarcode
				+ ", salesPrice=" + salesPrice + ", inputTax=" + inputTax + ", outputTax="
				+ outputTax + ", salesTax=" + salesTax + ", status=" + status + ", isPromotion="
				+ isPromotion + ", isAdjustPrice=" + isAdjustPrice + ", manageCategory="
				+ manageCategory + ", discountLimit=" + discountLimit + ", originLand="
				+ originLand + ", serviceFeeType=" + serviceFeeType + ", formatType=" + formatType
				+ ", statCategory=" + statCategory + ", commissionRate=" + commissionRate
				+ ", actionCode=" + actionCode + ", actionDate=" + actionDate + ", actionPerson="
				+ actionPerson + "]";
	}

}
