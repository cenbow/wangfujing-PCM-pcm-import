package com.wangfj.product.core.controller.support;

import javax.validation.constraints.NotNull;

/**
 * 一品多供应商关系上传(输入参数)
 * 
 * @Class Name PcmShoppeProSupplyPara
 * @Author wangxuan
 * @Create In 2015-8-28
 */
public class PcmShoppeProSupplyUploadPara {

	@NotNull(message = "{PcmShoppeProSupplyPara.storeCode.isNotNull}")
	private String storeCode;// 门店编号

	@NotNull(message = "{PcmShoppeProSupplyPara.productCode.isNotNull}")
	private String productCode;// 商品编码

	@NotNull(message = "{PcmShoppeProSupplyPara.supplierProductCode.isNotNull}")
	private String supplierProductCode;// 专柜商品编码

	@NotNull(message = "{PcmShoppeProSupplyPara.supplierCode.isNotNull}")
	private String supplierCode;// 商品对应的供应商编码

	@NotNull(message = "{PcmShoppeProSupplyPara.ACTION_CODE.isNotNull}")
	private String ACTION_CODE;// 本条记录对应的操作 (A添加；U更新；D删除)

	private String ACTION_DATE;// 操作时间（格式为yyyyMMdd.HHmmssZ)

	private String ACTION_PERSON;// 操作人

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode == null ? null : storeCode.trim();
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode == null ? null : productCode.trim();
	}

	public String getSupplierProductCode() {
		return supplierProductCode;
	}

	public void setSupplierProductCode(String supplierProductCode) {
		this.supplierProductCode = supplierProductCode == null ? null : supplierProductCode.trim();
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode == null ? null : supplierCode.trim();
	}

	public String getACTION_CODE() {
		return ACTION_CODE;
	}

	public void setACTION_CODE(String aCTION_CODE) {
		ACTION_CODE = aCTION_CODE == null ? null : aCTION_CODE.trim();
	}

	public String getACTION_DATE() {
		return ACTION_DATE;
	}

	public void setACTION_DATE(String aCTION_DATE) {
		ACTION_DATE = aCTION_DATE == null ? null : aCTION_DATE.trim();
	}

	public String getACTION_PERSON() {
		return ACTION_PERSON;
	}

	public void setACTION_PERSON(String aCTION_PERSON) {
		ACTION_PERSON = aCTION_PERSON == null ? null : aCTION_PERSON.trim();
	}

}
