package com.wangfj.product.PIS.controller.support;

import javax.validation.constraints.NotNull;

import com.wangfj.product.core.controller.support.base.para.BasePara;

/**
 * erp编码/品牌/款/色/尺寸\规格 Para
 * 
 * @Class Name ValidProductPara
 * @Author wangsy
 * @Create In 2015年7月14日
 */
public class ProductListPara extends BasePara {

	/**
	 * @Field long serialVersionUID
	 */
	private static final long serialVersionUID = 3142531605333861168L;

	/*
	 * 商品大码=erp编码
	 */
	private String erpProductCode;
	/*
	 * 品牌编码
	 */
	@NotNull(message = "{ProductListPara.brandCode.isNotNull}")
	private String brandCode;
	/*
	 * 款号
	 */
	@NotNull(message = "{ProductListPara.productNum.isNotNull}")
	private String productNum;
	/*
	 * 色码
	 */
	@NotNull(message = "{ProductListPara.colorCode.isNotNull}")
	private String colorCode;
	/*
	 * 尺码/规格
	 */
	@NotNull(message = "{ProductListPara.sizeCode.isNotNull}")
	private String sizeCode;

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

	public String getProductNum() {
		return productNum;
	}

	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getSizeCode() {
		return sizeCode;
	}

	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}

	@Override
	public String toString() {
		return "ProductListPara [erpProductCode=" + erpProductCode + ", brandCode=" + brandCode
				+ ", productNum=" + productNum + ", colorCode=" + colorCode + ", sizeCode="
				+ sizeCode + "]";
	}

}
