package com.wangfj.product.PAD.controller.support;

import java.util.List;

public class PcmEdiProductStockPara {
	/**
	 * 专柜商品编码
	 */
	private List<String> shoppeProSids;

	/**
	 * 渠道商品编码
	 */
	private String channelCode;

	/**
	 * 库位
	 */
	private Integer stockTypeSid;

	public List<String> getShoppeProSids() {
		return shoppeProSids;
	}

	public void setShoppeProSids(List<String> shoppeProSids) {
		this.shoppeProSids = shoppeProSids;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Integer getStockTypeSid() {
		return stockTypeSid;
	}

	public void setStockTypeSid(Integer stockTypeSid) {
		this.stockTypeSid = stockTypeSid;
	}

	@Override
	public String toString() {
		return "PcmEdiProductStockPara [shoppeProSids=" + shoppeProSids + ", channelCode="
				+ channelCode + ", stockTypeSid=" + stockTypeSid + "]";
	}

}
