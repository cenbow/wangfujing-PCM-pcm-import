package com.wangfj.product.PIS.controller.support;

public class PcmPriceToPISPara {
	/**
	 * 门店(中台的门店编码)
	 */
	private String storecode;

	/**
	 * 中台专柜商品编码
	 */
	private String supplierprodcode;

	/**
	 * 变价上传的唯一标识（取上传参数中的GUID的值）
	 */
	private String guid;

	/**
	 * 错误编码
	 */
	private String resultcode;

	/**
	 * 消息
	 */
	private String message;

	public String getStorecode() {
		return storecode;
	}

	public void setStorecode(String storecode) {
		this.storecode = storecode;
	}

	public String getSupplierprodcode() {
		return supplierprodcode;
	}

	public void setSupplierprodcode(String supplierprodcode) {
		this.supplierprodcode = supplierprodcode;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getResultcode() {
		return resultcode;
	}

	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
