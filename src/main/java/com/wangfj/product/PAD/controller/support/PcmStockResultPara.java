package com.wangfj.product.PAD.controller.support;

public class PcmStockResultPara {
	/**
	 * 专柜商品编码
	 */
	private String supplyProductId;
	/**
	 * 是否成功：true成功，false失败
	 */
	private String success;
	/**
	 * 错误内容
	 */
	private String errorMsg;
	/**
	 * 错误编码
	 */
	private String errorCode;

	public String getSupplyProductId() {
		return supplyProductId;
	}

	public void setSupplyProductId(String supplyProductId) {
		this.supplyProductId = supplyProductId;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "PcmStockResultPara [supplyProductId=" + supplyProductId + ", success=" + success
				+ ", errorMsg=" + errorMsg + ", errorCode=" + errorCode + "]";
	}

}
