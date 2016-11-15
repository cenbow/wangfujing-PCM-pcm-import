package com.wangfj.product.PAD.controller.support;

import java.util.List;

import com.wangfj.product.stocks.domain.vo.EdiStockDto;

public class PcmEdiProductStockPara {
	/**
	 * 专柜商品编码
	 */
	private List<EdiStockDto> shoppeProSids;

	/**
	 * 渠道商品编码
	 */
	private String channelCode;

	/**
	 * 库位
	 */
	private Integer stockTypeSid;

	public List<EdiStockDto> getShoppeProSids() {
		return shoppeProSids;
	}

	public void setShoppeProSids(List<EdiStockDto> shoppeProSids) {
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
