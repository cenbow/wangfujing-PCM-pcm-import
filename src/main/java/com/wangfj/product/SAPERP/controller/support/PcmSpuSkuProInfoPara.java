package com.wangfj.product.SAPERP.controller.support;

public class PcmSpuSkuProInfoPara {
	private String spuCode;// spu编码
	private String colorSid;// 色系编码
	private String height;// 高
	private String width;// 宽
	private Integer urlNum;// 数量

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	public String getColorSid() {
		return colorSid;
	}

	public void setColorSid(String colorSid) {
		this.colorSid = colorSid;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public Integer getUrlNum() {
		return urlNum;
	}

	public void setUrlNum(Integer urlNum) {
		this.urlNum = urlNum;
	}

	@Override
	public String toString() {
		return "PcmSpuSkuProInfoPara [spuCode=" + spuCode + ", colorSid=" + colorSid + ", height="
				+ height + ", width=" + width + ", urlNum=" + urlNum + "]";
	}

}
