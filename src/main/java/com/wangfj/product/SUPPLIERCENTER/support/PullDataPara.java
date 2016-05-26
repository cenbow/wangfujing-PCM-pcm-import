package com.wangfj.product.SUPPLIERCENTER.support;

import java.util.List;
import java.util.Map;

/**
 * 供应商商品上传使用DataPara
 * 
 * @Class Name PullDataPara
 * @Author wangsy
 * @Create In 2015年7月15日
 */
public class PullDataPara {
	private String supplierCode;/* 供应商代码(门店ERP的供应商编码) */
	private String counterCode; /* 专柜编码（中台的专柜编码） */
	private String erpProductCode;/* 商品大码（商品对应的门店ERP编码） */
	private String brandCode;/* 品牌编码(门店品牌编码)。 */
	private String standardBarCode;/* 自编商品条码/国标条码 */
	private String productNum;/* 款号（吊牌或者外包装上的款号） */
	private String modelNum;/* 货号(吊牌或者外包装的货号，一般情况下货号是到款色规的编码，或者货号=款号) */
	private String productName;/* 商品名称 */
	private String unitCode;/* 库存单位编码（单位字典中的编码）(销售单位) */
	private String colorCode;/* 色码（吊牌或者外包装上的色码对应的字典编码,如果没有，必须传空） */
	private String colorName;/* 颜色描述（一般情况下颜色描述=色码） */
	private String proColor;/* 色系 */
	private String inputTax;/* 进项税 */
	private String outputTax;/* 销项税 */
	private String consumptionTax;/* 消费税 */
	private String rate_price;/* 扣率/进价 */
	private String purchasePrice_taxRebate;/* 扣率/含税进价 */
	private String discountLimit;/* 折扣底限 */
	private String placeOfOrigin;/* 产地 */
	private String countryOfOrigin;/* 原产地 */
	private String processingType;/* 加工类型 */
	private String prodCategoryCode;/* 末级分类代码（末级工业分类在中台工业分类字典的编码） */
	private String finalClassiFicationCode;/* 末级统计分类代码 */
	private String sizeCode;/* 尺码/规格 */
	private String yearToMarket;/* 上市年份(yyyy) */
	private String seasonCode;/* 季节（01 春；02夏；03秋；04冬；05春夏；06春秋；07秋冬；08四季）传编码（例如01） */
	private String marketPrice;/* 吊牌价（原价） */
	private String operateMode;/* 经营方式（Z001经销，Z002代销，Z003联营，Z004平台服务，Z005租赁） */
	private String productAbbr;/* 商品简称(最多18个中文字符)。与最后一次准入的专柜商品简称保持一致。 */
	private String crowdUser;/* 适用人群 */
	private String remarks;/* 备注 */
	private String offerNumber;/* 要约号 */
	private String entryNumber;/* 录入人员编号 */
	private String procurementPersonnelNumber;/* 采购人员编号 */
	private String mainAttribute;/* 主属性 */
	private String registeredTradeName;/* 商品注册商标名 */
	private String features;/* 特性 */
	private String manageCateGory;/* 管理分类 */
	private String lineNumber;/* 行号（唯一编码，供应商生成的GUID编码） */
	private String type;/* 类型（0百货，1超市) */
	private String productCode;// 中台商品编码??
	private String isAdjustPrice;/* 是否允许ERP调价 */
	private String isPromotion;/* 是否允许ERP促销 */
	private String inventory;/* 期初库存 */
	//供应商商品上传新加字段
	private String shortDesc;//商品描述（短描述）
	private String purchasePrice;//供货价（采购价）
	private String originCountry;//原产国编码
	private String originLand;//产地编码
	private String baseUnitCode;//基本计量单位
	private String field;//统比销
	private String salesTax;//消费税
	private String productNameAlias;//专柜商品简称
	private String barCodeType;//条码类型
	private String discount;//扣率
	private String stockType;//库存类型
	private String shopCode;//门店编码
	private String businessType;//业态
	private String supProCode;//供应商商品编码
	private List<Map<String,Object>> standardBarCodes;//条码列表
	private String discountCode;//扣率码
	private String productType;//商品类型1 普通商品（实物类），2 赠品 ，3 礼品，4 虚拟商品（充值卡，购物卡），5 服务类商品 （礼品包装，购物接送服务，停车服务）（注：礼品不可卖，赠品可卖
	private String tmsType;//包装组
	private String importNo;//供应商导入商品批次号
	private String shelfLife;//总货架寿命
	private String remainShelLife;//剩余货架寿命
	
	
	/**
	 * @Return the String shelfLife
	 */
	public String getShelfLife() {
		return shelfLife;
	}

	/**
	 * @Param String shelfLife to set
	 */
	public void setShelfLife(String shelfLife) {
		this.shelfLife = shelfLife;
	}

	/**
	 * @Return the String remainShelLife
	 */
	public String getRemainShelLife() {
		return remainShelLife;
	}

	/**
	 * @Param String remainShelLife to set
	 */
	public void setRemainShelLife(String remainShelLife) {
		this.remainShelLife = remainShelLife;
	}

	public String getImportNo() {
		return importNo;
	}

	public void setImportNo(String importNo) {
		this.importNo = importNo;
	}

	public String getTmsType() {
		return tmsType;
	}

	public void setTmsType(String tmsType) {
		this.tmsType = tmsType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public List<Map<String, Object>> getStandardBarCodes() {
		return standardBarCodes;
	}

	public void setStandardBarCodes(List<Map<String, Object>> standardBarCodes) {
		this.standardBarCodes = standardBarCodes;
	}

	public String getSupProCode() {
		return supProCode;
	}

	public void setSupProCode(String supProCode) {
		this.supProCode = supProCode;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getProductNameAlias() {
		return productNameAlias;
	}

	public void setProductNameAlias(String productNameAlias) {
		this.productNameAlias = productNameAlias;
	}

	public String getBarCodeType() {
		return barCodeType;
	}

	public void setBarCodeType(String barCodeType) {
		this.barCodeType = barCodeType;
	}

	public String getSalesTax() {
		return salesTax;
	}

	public void setSalesTax(String salesTax) {
		this.salesTax = salesTax;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
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

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getErpProductCode() {
		return erpProductCode;
	}

	public void setErpProductCode(String erpProductCode) {
		this.erpProductCode = erpProductCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getStandardBarCode() {
		return standardBarCode;
	}

	public void setStandardBarCode(String standardBarCode) {
		this.standardBarCode = standardBarCode;
	}

	public String getProductNum() {
		return productNum;
	}

	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}

	public String getModelNum() {
		return modelNum;
	}

	public void setModelNum(String modelNum) {
		this.modelNum = modelNum;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getProColor() {
		return proColor;
	}

	public void setProColor(String proColor) {
		this.proColor = proColor;
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

	public String getConsumptionTax() {
		return consumptionTax;
	}

	public void setConsumptionTax(String consumptionTax) {
		this.consumptionTax = consumptionTax;
	}

	public String getRate_price() {
		return rate_price;
	}

	public void setRate_price(String rate_price) {
		this.rate_price = rate_price;
	}

	public String getPurchasePrice_taxRebate() {
		return purchasePrice_taxRebate;
	}

	public void setPurchasePrice_taxRebate(String purchasePrice_taxRebate) {
		this.purchasePrice_taxRebate = purchasePrice_taxRebate;
	}

	public String getDiscountLimit() {
		return discountLimit;
	}

	public void setDiscountLimit(String discountLimit) {
		this.discountLimit = discountLimit;
	}

	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	public String getProcessingType() {
		return processingType;
	}

	public void setProcessingType(String processingType) {
		this.processingType = processingType;
	}

	public String getProdCategoryCode() {
		return prodCategoryCode;
	}

	public void setProdCategoryCode(String prodCategoryCode) {
		this.prodCategoryCode = prodCategoryCode;
	}

	public String getFinalClassiFicationCode() {
		return finalClassiFicationCode;
	}

	public void setFinalClassiFicationCode(String finalClassiFicationCode) {
		this.finalClassiFicationCode = finalClassiFicationCode;
	}

	public String getSizeCode() {
		return sizeCode;
	}

	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}

	public String getYearToMarket() {
		return yearToMarket;
	}

	public void setYearToMarket(String yearToMarket) {
		this.yearToMarket = yearToMarket;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getOperateMode() {
		return operateMode;
	}

	public void setOperateMode(String operateMode) {
		this.operateMode = operateMode;
	}

	public String getProductAbbr() {
		return productAbbr;
	}

	public void setProductAbbr(String productAbbr) {
		this.productAbbr = productAbbr;
	}

	public String getCrowdUser() {
		return crowdUser;
	}

	public void setCrowdUser(String crowdUser) {
		this.crowdUser = crowdUser;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOfferNumber() {
		return offerNumber;
	}

	public void setOfferNumber(String offerNumber) {
		this.offerNumber = offerNumber;
	}

	public String getEntryNumber() {
		return entryNumber;
	}

	public void setEntryNumber(String entryNumber) {
		this.entryNumber = entryNumber;
	}

	public String getProcurementPersonnelNumber() {
		return procurementPersonnelNumber;
	}

	public void setProcurementPersonnelNumber(String procurementPersonnelNumber) {
		this.procurementPersonnelNumber = procurementPersonnelNumber;
	}

	public String getMainAttribute() {
		return mainAttribute;
	}

	public void setMainAttribute(String mainAttribute) {
		this.mainAttribute = mainAttribute;
	}

	public String getRegisteredTradeName() {
		return registeredTradeName;
	}

	public void setRegisteredTradeName(String registeredTradeName) {
		this.registeredTradeName = registeredTradeName;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getManageCateGory() {
		return manageCateGory;
	}

	public void setManageCateGory(String manageCateGory) {
		this.manageCateGory = manageCateGory;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getIsAdjustPrice() {
		return isAdjustPrice;
	}

	public void setIsAdjustPrice(String isAdjustPrice) {
		this.isAdjustPrice = isAdjustPrice;
	}

	public String getIsPromotion() {
		return isPromotion;
	}

	public void setIsPromotion(String isPromotion) {
		this.isPromotion = isPromotion;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	@Override
	public String toString() {
		return "PullDataPara [supplierCode=" + supplierCode + ", counterCode=" + counterCode
				+ ", erpProductCode=" + erpProductCode + ", brandCode=" + brandCode
				+ ", standardBarCode=" + standardBarCode + ", productNum=" + productNum
				+ ", modelNum=" + modelNum + ", productName=" + productName + ", unitCode="
				+ unitCode + ", colorCode=" + colorCode + ", colorName=" + colorName
				+ ", proColor=" + proColor + ", inputTax=" + inputTax + ", outputTax=" + outputTax
				+ ", consumptionTax=" + consumptionTax + ", rate_price=" + rate_price
				+ ", purchasePrice_taxRebate=" + purchasePrice_taxRebate + ", discountLimit="
				+ discountLimit + ", placeOfOrigin=" + placeOfOrigin + ", countryOfOrigin="
				+ countryOfOrigin + ", processingType=" + processingType + ", prodCategoryCode="
				+ prodCategoryCode + ", finalClassiFicationCode=" + finalClassiFicationCode
				+ ", sizeCode=" + sizeCode + ", yearToMarket=" + yearToMarket + ", seasonCode="
				+ seasonCode + ", marketPrice=" + marketPrice + ", operateMode=" + operateMode
				+ ", productAbbr=" + productAbbr + ", crowdUser=" + crowdUser + ", remarks="
				+ remarks + ", offerNumber=" + offerNumber + ", entryNumber=" + entryNumber
				+ ", procurementPersonnelNumber=" + procurementPersonnelNumber + ", mainAttribute="
				+ mainAttribute + ", registeredTradeName=" + registeredTradeName + ", features="
				+ features + ", manageCateGory=" + manageCateGory + ", lineNumber=" + lineNumber
				+ ", type=" + type + ", productCode=" + productCode + ", isAdjustPrice="
				+ isAdjustPrice + ", isPromotion=" + isPromotion + ", inventory=" + inventory + "]";
	}

}
