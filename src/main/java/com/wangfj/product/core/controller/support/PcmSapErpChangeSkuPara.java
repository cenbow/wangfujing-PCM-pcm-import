package com.wangfj.product.core.controller.support;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PcmSapErpChangeSkuPara {
	@JsonProperty(value = "COUNTER_PROD_CODE")
	private String COUNTER_PROD_CODE;
	@JsonProperty(value = "STYLE_CODE")
	private String STYLE_CODE;
	@JsonProperty(value = "SIZE_CODE")
	private String SIZE_CODE;
	@JsonProperty(value = "COLOR_NAME")
	private String COLOR_NAME;

	public String getCOUNTER_PROD_CODE() {
		return COUNTER_PROD_CODE;
	}

	public void setCOUNTER_PROD_CODE(String cOUNTER_PROD_CODE) {
		COUNTER_PROD_CODE = cOUNTER_PROD_CODE;
	}

	public String getSTYLE_CODE() {
		return STYLE_CODE;
	}

	public void setSTYLE_CODE(String sTYLE_CODE) {
		STYLE_CODE = sTYLE_CODE;
	}

	public String getSIZE_CODE() {
		return SIZE_CODE;
	}

	public void setSIZE_CODE(String sIZE_CODE) {
		SIZE_CODE = sIZE_CODE;
	}

	public String getCOLOR_NAME() {
		return COLOR_NAME;
	}

	public void setCOLOR_NAME(String cOLOR_NAME) {
		COLOR_NAME = cOLOR_NAME;
	}

}
