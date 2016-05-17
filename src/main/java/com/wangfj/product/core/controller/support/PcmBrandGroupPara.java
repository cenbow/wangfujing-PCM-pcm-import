package com.wangfj.product.core.controller.support;

import java.util.Date;

public class PcmBrandGroupPara {

	private Long sid;

	private String brandSid; // 集团品牌编码

	private String brandName;// 中文名称

	private String brandNameSpell;// 中文拼音

	private String brandNameAlias;// 别名

	private String brandNameEn;// 英文名称

	private String brandDesc;// 品牌描述

	private String brandPict;// 品牌图片

	private Date brandCreateTime;// 创建时间

	private String brandCreateCountry;// 创建国家

	private String brandSpecialty;// 品牌特点

	private String brandSuitability;// 适合人群

	private Long isFactoryStore;// 是否是工厂店

	private String factoryBigPic;// 工厂店大图片

	private String factorySmallPic;// 工厂店缩略图

	private String activityBigPic;// 活动大图

	private String activitySmallPic;// 活动缩略图

	private Long factoryStoreOrder;//

	private Long parentFactoryStoreId;// 主工厂店Id

	private String logoPic;// 工厂店logo图片，在工厂店查看页面显示的图片

	private Long awesome;//

	private Long isDisplay;// 是否展示

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getBrandSid() {
		return brandSid;
	}

	public void setBrandSid(String brandSid) {
		this.brandSid = brandSid == null ? null : brandSid.trim();
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName == null ? null : brandName.trim();
	}

	public String getBrandNameSpell() {
		return brandNameSpell;
	}

	public void setBrandNameSpell(String brandNameSpell) {
		this.brandNameSpell = brandNameSpell == null ? null : brandNameSpell.trim();
	}

	public String getBrandNameAlias() {
		return brandNameAlias;
	}

	public void setBrandNameAlias(String brandNameAlias) {
		this.brandNameAlias = brandNameAlias == null ? null : brandNameAlias.trim();
	}

	public String getBrandNameEn() {
		return brandNameEn;
	}

	public void setBrandNameEn(String brandNameEn) {
		this.brandNameEn = brandNameEn == null ? null : brandNameEn.trim();
	}

	public String getBrandDesc() {
		return brandDesc;
	}

	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc == null ? null : brandDesc.trim();
	}

	public String getBrandPict() {
		return brandPict;
	}

	public void setBrandPict(String brandPict) {
		this.brandPict = brandPict == null ? null : brandPict.trim();
	}

	public Date getBrandCreateTime() {
		return brandCreateTime;
	}

	public void setBrandCreateTime(Date brandCreateTime) {
		this.brandCreateTime = brandCreateTime;
	}

	public String getBrandCreateCountry() {
		return brandCreateCountry;
	}

	public void setBrandCreateCountry(String brandCreateCountry) {
		this.brandCreateCountry = brandCreateCountry == null ? null : brandCreateCountry.trim();
	}

	public String getBrandSpecialty() {
		return brandSpecialty;
	}

	public void setBrandSpecialty(String brandSpecialty) {
		this.brandSpecialty = brandSpecialty == null ? null : brandSpecialty.trim();
	}

	public String getBrandSuitability() {
		return brandSuitability;
	}

	public void setBrandSuitability(String brandSuitability) {
		this.brandSuitability = brandSuitability == null ? null : brandSuitability.trim();
	}

	public Long getIsFactoryStore() {
		return isFactoryStore;
	}

	public void setIsFactoryStore(Long isFactoryStore) {
		this.isFactoryStore = isFactoryStore;
	}

	public String getFactoryBigPic() {
		return factoryBigPic;
	}

	public void setFactoryBigPic(String factoryBigPic) {
		this.factoryBigPic = factoryBigPic == null ? null : factoryBigPic.trim();
	}

	public String getFactorySmallPic() {
		return factorySmallPic;
	}

	public void setFactorySmallPic(String factorySmallPic) {
		this.factorySmallPic = factorySmallPic == null ? null : factorySmallPic.trim();
	}

	public String getActivityBigPic() {
		return activityBigPic;
	}

	public void setActivityBigPic(String activityBigPic) {
		this.activityBigPic = activityBigPic == null ? null : activityBigPic.trim();
	}

	public String getActivitySmallPic() {
		return activitySmallPic;
	}

	public void setActivitySmallPic(String activitySmallPic) {
		this.activitySmallPic = activitySmallPic == null ? null : activitySmallPic.trim();
	}

	public Long getFactoryStoreOrder() {
		return factoryStoreOrder;
	}

	public void setFactoryStoreOrder(Long factoryStoreOrder) {
		this.factoryStoreOrder = factoryStoreOrder;
	}

	public Long getParentFactoryStoreId() {
		return parentFactoryStoreId;
	}

	public void setParentFactoryStoreId(Long parentFactoryStoreId) {
		this.parentFactoryStoreId = parentFactoryStoreId;
	}

	public String getLogoPic() {
		return logoPic;
	}

	public void setLogoPic(String logoPic) {
		this.logoPic = logoPic == null ? null : logoPic.trim();
	}

	public Long getAwesome() {
		return awesome;
	}

	public void setAwesome(Long awesome) {
		this.awesome = awesome;
	}

	public Long getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Long isDisplay) {
		this.isDisplay = isDisplay;
	}
}