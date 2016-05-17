package com.wangfj.product.core.controller.brand.support;

/**
 * 门店与门店品牌关系上传参数
 * 
 * @Class Name PcmShopBrandUploadPara
 * @Author wangxuan
 * @Create In 2015-9-18
 */
public class PcmShopBrandUploadPara {

	private String storeCode;// 门店编码

	private String brandCode;// 门店品牌编码

	private String brandName;// 门店品牌名称

	private String ACT_CODE;// 本条记录对应的操作 (A添加；U更新；D删除)

	private String ACT_DATE;// 操作时间

	private String ACT_PERSON;// 操作人

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode == null ? null : storeCode.trim();
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode == null ? null : brandCode.trim();
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName == null ? null : brandName.trim();
	}

	public String getACT_CODE() {
		return ACT_CODE;
	}

	public void setACT_CODE(String aCT_CODE) {
		ACT_CODE = aCT_CODE;
	}

	public String getACT_DATE() {
		return ACT_DATE;
	}

	public void setACT_DATE(String aCT_DATE) {
		ACT_DATE = aCT_DATE;
	}

	public String getACT_PERSON() {
		return ACT_PERSON;
	}

	public void setACT_PERSON(String aCT_PERSON) {
		ACT_PERSON = aCT_PERSON;
	}

	@Override
	public String toString() {
		return "PcmShopBrandUploadPara [storeCode=" + storeCode + ", brandCode=" + brandCode
				+ ", brandName=" + brandName + ", ACT_CODE=" + ACT_CODE + ", ACT_DATE=" + ACT_DATE
				+ ", ACT_PERSON=" + ACT_PERSON + "]";
	}

}
