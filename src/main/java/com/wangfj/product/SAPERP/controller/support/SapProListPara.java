package com.wangfj.product.SAPERP.controller.support;

public class SapProListPara {
	private String PROCODE;//商品编码
	private String ACTIONCODE;//操作类型
	public String getPROCODE() {
		return PROCODE;
	}
	public void setPROCODE(String pROCODE) {
		PROCODE = pROCODE;
	}
	public String getACTIONCODE() {
		return ACTIONCODE;
	}
	public void setACTIONCODE(String aCTIONCODE) {
		ACTIONCODE = aCTIONCODE;
	}
	@Override
	public String toString() {
		return "SapProListPara [PROCODE=" + PROCODE + ", ACTIONCODE="
				+ ACTIONCODE + "]";
	}
	
}
