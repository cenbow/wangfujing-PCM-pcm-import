/**
 * @Probject Name: pcm-import
 * @Path: com.wangfj.product.SUPPLIERCENTER.controller.supportPcmUploadProductPara.java
 * @Create By wangc
 * @Create In 2016-2-25 下午7:23:14
 */
package com.wangfj.product.SUPPLIERCENTER.controller.support;

import com.wangfj.product.core.controller.support.base.para.BasePara;

/**
 * 供应商商品上传para
 * @Class Name PcmUploadProductPara
 * @Author wangc
 * @Create In 2016-2-25
 */
public class PcmUploadProductPara extends BasePara {
	/**
	 * @Field long serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	private String shortDesc;//商品描述（短描述）
	
	private String IndusCategoryCode;//工业分类编码
	
	private String shopCode;//门店编码
	
	private String supplierCode;//供应商编码
	
	private String brandCode;//门店品牌编码
	
	private String modelCode;//款号
	
	private String articleNum;//供应商货号
	
	private String barCode;//供应商商品条码（标准条码）
	
	private String marketPrice;//市场价
	
	private String salePrice;//零售价
	
	private String purchasePrice;//供货价(采购价)
	
	private String inputTax;//进项税（采购税码）
	
	private String ouputTax;//销项税
	
	private String salesTax;//消费税
	
	private String staCategoryCode;//统计分类
	
	private String originCountry;//原产国编码
	
	private String originLand;//产地编码
	
	private String colorName;//色码
	
	private String colorAlias;//色码描述
	
	private String stanCode;//尺寸码;
	
	private String stanName;//尺码/规格描述
	
	private String baseUnitCode;//基本计量单位
	
	private String field;//统销比
	
	private String isCod;//可Cod
	
	private String tmsType;//商品组：包装商品（物流属性）
	
	private String isOriginPackage;//是否有原厂包装
	
	private String onSaleDate;//上市日期
	
	private Integer sexCode;//试用性别
	
	private String purchaseGroup;//采购组
	
	private String supProCode;//供应商商品编码
	
	private String season;//商品季节属性
	
	private String colorSid;//色系
	
	private String contract;//要约号
	
	private String shoppeCode;//专柜编码
	
	private String shoProName;//专柜商品名称
	
	private String shoProAlias;//专柜商品简称
	
	private String barCodeType;//条码类型

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getIndusCategoryCode() {
		return IndusCategoryCode;
	}

	public void setIndusCategoryCode(String indusCategoryCode) {
		IndusCategoryCode = indusCategoryCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
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

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(String articleNum) {
		this.articleNum = articleNum;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public String getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getInputTax() {
		return inputTax;
	}

	public void setInputTax(String inputTax) {
		this.inputTax = inputTax;
	}

	public String getOuputTax() {
		return ouputTax;
	}

	public void setOuputTax(String ouputTax) {
		this.ouputTax = ouputTax;
	}

	public String getSalesTax() {
		return salesTax;
	}

	public void setSalesTax(String salesTax) {
		this.salesTax = salesTax;
	}

	public String getStaCategoryCode() {
		return staCategoryCode;
	}

	public void setStaCategoryCode(String staCategoryCode) {
		this.staCategoryCode = staCategoryCode;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getOriginLand() {
		return originLand;
	}

	public void setOriginLand(String originLand) {
		this.originLand = originLand;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getColorAlias() {
		return colorAlias;
	}

	public void setColorAlias(String colorAlias) {
		this.colorAlias = colorAlias;
	}

	public String getStanCode() {
		return stanCode;
	}

	public void setStanCode(String stanCode) {
		this.stanCode = stanCode;
	}

	public String getStanName() {
		return stanName;
	}

	public void setStanName(String stanName) {
		this.stanName = stanName;
	}

	public String getBaseUnitCode() {
		return baseUnitCode;
	}

	public void setBaseUnitCode(String baseUnitCode) {
		this.baseUnitCode = baseUnitCode;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getIsCod() {
		return isCod;
	}

	public void setIsCod(String isCod) {
		this.isCod = isCod;
	}

	public String getTmsType() {
		return tmsType;
	}

	public void setTmsType(String tmsType) {
		this.tmsType = tmsType;
	}

	public String getIsOriginPackage() {
		return isOriginPackage;
	}

	public void setIsOriginPackage(String isOriginPackage) {
		this.isOriginPackage = isOriginPackage;
	}

	public String getOnSaleDate() {
		return onSaleDate;
	}

	public void setOnSaleDate(String onSaleDate) {
		this.onSaleDate = onSaleDate;
	}

	public Integer getSexCode() {
		return sexCode;
	}

	public void setSexCode(Integer sexCode) {
		this.sexCode = sexCode;
	}

	public String getPurchaseGroup() {
		return purchaseGroup;
	}

	public void setPurchaseGroup(String purchaseGroup) {
		this.purchaseGroup = purchaseGroup;
	}

	public String getSupProCode() {
		return supProCode;
	}

	public void setSupProCode(String supProCode) {
		this.supProCode = supProCode;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getColorSid() {
		return colorSid;
	}

	public void setColorSid(String colorSid) {
		this.colorSid = colorSid;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public String getShoppeCode() {
		return shoppeCode;
	}

	public void setShoppeCode(String shoppeCode) {
		this.shoppeCode = shoppeCode;
	}

	public String getShoProName() {
		return shoProName;
	}

	public void setShoProName(String shoProName) {
		this.shoProName = shoProName;
	}

	public String getShoProAlias() {
		return shoProAlias;
	}

	public void setShoProAlias(String shoProAlias) {
		this.shoProAlias = shoProAlias;
	}

	public String getBarCodeType() {
		return barCodeType;
	}

	public void setBarCodeType(String barCodeType) {
		this.barCodeType = barCodeType;
	}
	
	
}
