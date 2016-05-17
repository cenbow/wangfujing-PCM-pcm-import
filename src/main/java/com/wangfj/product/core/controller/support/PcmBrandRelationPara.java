package com.wangfj.product.core.controller.support;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 集团品牌门店品牌的关系Para
 * 
 * @Class Name PcmBrandRelationPara
 * @Author wangx
 * @Create In 2015-8-11
 */
public class PcmBrandRelationPara {

	private Long sid;

	@NotNull(message = "{PcmBrandRelationPara.brandSid.isNotNull}")
	private String brandSid;// 门店品牌sid

	@NotNull(message = "{PcmBrandRelationPara.brandRootSid.isNotNull}")
	private String brandRootSid;// 集团品牌sid

	private String optUser;// 操作人

	private Date optDate;// 操作时间

	private Integer status = 0;// 有效标记：0有效，1无效（默认为0）

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

	public String getBrandRootSid() {
		return brandRootSid;
	}

	public void setBrandRootSid(String brandRootSid) {
		this.brandRootSid = brandRootSid == null ? null : brandRootSid.trim();
	}

	public String getOptUser() {
		return optUser;
	}

	public void setOptUser(String optUser) {
		this.optUser = optUser == null ? null : optUser.trim();
	}

	public Date getOptDate() {
		return optDate;
	}

	public void setOptDates(Date optDate) {
		this.optDate = optDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
