package com.wangfj.product.EfutureERP.controller.support;

public class PcmPriceImportEFPara {
	private String app_key;
	private String cmd;
	private PcmEventListItemPrice data;
	private String id;
	private String operate;
	private String this_time;
	private String version;

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public PcmEventListItemPrice getData() {
		return data;
	}

	public void setData(PcmEventListItemPrice data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getThis_time() {
		return this_time;
	}

	public void setThis_time(String this_time) {
		this.this_time = this_time;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "PcmPriceImportEFPara [app_key=" + app_key + ", cmd=" + cmd + ", data=" + data
				+ ", id=" + id + ", operate=" + operate + ", this_time=" + this_time + ", version="
				+ version + "]";
	}

}
