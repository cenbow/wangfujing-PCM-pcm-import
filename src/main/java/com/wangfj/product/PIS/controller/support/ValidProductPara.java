package com.wangfj.product.PIS.controller.support;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.wangfj.product.core.controller.support.base.para.BasePara;

/**
 * 验证Para
 * 
 * @Class Name ValidProductPara
 * @Author wangsy
 * @Create In 2015年7月14日
 */
public class ValidProductPara extends BasePara {

	/**
	 * @Field long serialVersionUID
	 */
	private static final long serialVersionUID = 3142531605333861168L;

	/*
	 * 供应商编码
	 */
	@NotNull(message = "{ValidProductPara.supplierCode.isNotNull}")
	private String supplierCode;

	/*
	 * 专柜编码
	 */
	@NotNull(message = "{ValidProductPara.counterCode.isNotNull}")
	private String counterCode;

	/*
	 * productList对象中包含的商品行数
	 */
	@NotNull(message = "{ValidProductPara.productCount.isNotNull}")
	private String productCount;

	/*
	 * 待校验的商品列表
	 */
	private List<ProductListPara> listProductPara;

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

	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}

	public List<ProductListPara> getListProductPara() {
		return listProductPara;
	}

	public void setListProductPara(List<ProductListPara> listProductPara) {
		this.listProductPara = listProductPara;
	}

	@Override
	public String toString() {
		return "ValidProductPara [supplierCode=" + supplierCode + ", counterCode=" + counterCode
				+ ", productCount=" + productCount + ", listProductPara=" + listProductPara + "]";
	}

}
