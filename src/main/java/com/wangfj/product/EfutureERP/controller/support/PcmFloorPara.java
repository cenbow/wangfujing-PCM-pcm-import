package com.wangfj.product.EfutureERP.controller.support;

import java.util.Date;

public class PcmFloorPara extends BasePara {
	private Long sid;

	private String storeCode;/* 门店编码 */

	private String code;/* 楼层编码 */

	private String oldCode;/* 旧的 楼层编码 */

	private String name;/* 楼层名称 */

	private String oldName;/* 旧的 楼层名称 */

	private Long optUserSid;

	private Date updateTime;

	private String createName;

	private Date createTime;
	private String actionCode;
	
	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOptUserSid() {
		return optUserSid;
	}

	public void setOptUserSid(Long optUserSid) {
		this.optUserSid = optUserSid;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOldCode() {
		return oldCode;
	}

	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}


	@Override
	public String toString() {
		return "PcmFloorPara [sid=" + sid + ", storeCode=" + storeCode
				+ ", code=" + code + ", oldCode=" + oldCode + ", name=" + name
				+ ", oldName=" + oldName + ", optUserSid=" + optUserSid
				+ ", updateTime=" + updateTime + ", createName=" + createName
				+ ", createTime=" + createTime + "]";
	}

}
