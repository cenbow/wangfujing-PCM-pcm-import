/**
 * @Probject Name: pcm-core
 * @Path: com.wangfj.product.EfutureERP.controller.supportPcmOrganizaPara.java
 * @Create By wuxiong
 * @Create In 2015年7月28日 下午6:56:29
 * TODO
 */
package com.wangfj.product.EfutureERP.controller.support;

import java.util.Date;

/**
 * @Class Name PcmOrganizaPara
 * @Author wuxiong
 * @Create In 2015年7月28日
 */
public class PcmOrganizaPara extends BasePara {
	private Long sid;
	private String code;/* 机构编码 */
	private String name;/* 机构名字 */
	private String type;/* 级别类型 */
	private String superCode;/* 上级机构编码 */
	private String storeType;/* 门店类型 */
	private String shippingPoint;/* 集货地点编码 */
	private String storeCode;/* 门店编码 */
	private String areaCode;/* 门店所属城市在省市区字典中的编码 */
	private String actionCode;
	private Date updateTime;

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSuperCode() {
		return superCode;
	}

	public void setSuperCode(String superCode) {
		this.superCode = superCode;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getShippingPoint() {
		return shippingPoint;
	}

	public void setShippingPoint(String shippingPoint) {
		this.shippingPoint = shippingPoint;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

}
