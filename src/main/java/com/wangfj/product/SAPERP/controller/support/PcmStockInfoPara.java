package com.wangfj.product.SAPERP.controller.support;

/**
 * 库存查询
 * 
 * @Class Name PcmStockInfoPara
 * @Author kongqf
 * @Create In 2015年12月25日
 */
public class PcmStockInfoPara {
	/**
	 * 专柜商品编码
	 */
	private String shoppeProSid;

	/**
	 * 库位
	 */
	private Integer stockTypeSid;

	/**
	 * 渠道
	 */
	private Integer channelSid;

	/**
	 * 库存数量
	 */
	private Integer proSum;

	/**
	 * 成功：ture,失败:false
	 */
	private String success;

	/**
	 * 错误信息
	 */
	private String errorMsg;

	public String getShoppeProSid() {
		return shoppeProSid;
	}

	public void setShoppeProSid(String shoppeProSid) {
		this.shoppeProSid = shoppeProSid;
	}

	public Integer getStockTypeSid() {
		return stockTypeSid;
	}

	public void setStockTypeSid(Integer stockTypeSid) {
		this.stockTypeSid = stockTypeSid;
	}

	public Integer getChannelSid() {
		return channelSid;
	}

	public void setChannelSid(Integer channelSid) {
		this.channelSid = channelSid;
	}

	public Integer getProSum() {
		return proSum;
	}

	public void setProSum(Integer proSum) {
		this.proSum = proSum;
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

	@Override
	public String toString() {
		return "PcmStockInfoPara [shoppeProSid=" + shoppeProSid + ", stockTypeSid=" + stockTypeSid
				+ ", channelSid=" + channelSid + ", proSum=" + proSum + ", success=" + success
				+ ", errorMsg=" + errorMsg + "]";
	}

}
