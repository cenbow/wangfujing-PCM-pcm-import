package com.wangfj.product.PAD.controller.support;

public class PcmStockWcsPara {
	private String type;// 操作类型
	private String matnr;// 商品编码
	private String num;// 操作数量
	private String flag;// 库标识

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "PcmStockWcsPara [type=" + type + ", matnr=" + matnr + ", num=" + num + ", flag="
				+ flag + "]";
	}

}
