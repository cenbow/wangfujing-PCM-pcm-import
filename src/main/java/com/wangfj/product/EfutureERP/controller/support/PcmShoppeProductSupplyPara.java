package com.wangfj.product.EfutureERP.controller.support;

public class PcmShoppeProductSupplyPara extends BasePara {
	private Long sid;
	private String supplySid;// 供应商编码

	private String shopPuductSid;// 专柜商品编码

	private String actionCode;

	private String actionDate;

	private String actionPerson;

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getSupplySid() {
		return supplySid;
	}

	public void setSupplySid(String supplySid) {
		this.supplySid = supplySid;
	}

	public String getShopPuductSid() {
		return shopPuductSid;
	}

	public void setShopPuductSid(String shopPuductSid) {
		this.shopPuductSid = shopPuductSid;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public String getActionPerson() {
		return actionPerson;
	}

	public void setActionPerson(String actionPerson) {
		this.actionPerson = actionPerson;
	}

}
