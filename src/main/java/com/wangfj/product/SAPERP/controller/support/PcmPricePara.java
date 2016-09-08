package com.wangfj.product.SAPERP.controller.support;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.wangfj.product.core.controller.support.base.para.BasePara;

/**
 * 变价请求信息
 * 
 * @Class Name PcmPricePara
 * @Author kongqf
 * @Create In 2015年7月10日
 */
public class PcmPricePara extends BasePara {
	/**
	 * 门店(中台的门店编码)
	 */
	// @NotNull(message = "{PcmPricePara.storecode.isNotNull}")
	@JsonProperty(value = "STORECODE")
	private String STORECODE;

	/**
	 * 商品编码 电商ERP上传电商商品编码 门店ERP上传的是门店ERP自己的商品编码 导入终端为空
	 */
	@JsonProperty(value = "MATNR")
	private String MATNR;

	/**
	 * 中台专柜商品编码 （电商ERP，门店ERP和导入终端上传的是中台供应商商品编码） 电商和导入终端不为空，门店ERP可能传空
	 */
	// @NotNull(message = "{PcmPricePara.supplierprodcode.isNotNull}")
	@JsonProperty(value = "SUPPLIERPRODCODE")
	private String SUPPLIERPRODCODE;

	/**
	 * 售价
	 */
	// @NotNull(message = "{PcmPricePara.zsprice.isNotNull}")
	@JsonProperty(value = "ZSPRICE")
	private String ZSPRICE;

	/**
	 * 中台专柜编码
	 */
	@JsonProperty(value = "SITECODE")
	private String SITECODE;

	/**
	 * 货币单位 对于门店ERP是固定值RMB 对于电商ERP则根据系统当前设置提供
	 */
	@JsonProperty(value = "WAERS")
	private String WAERS;

	/**
	 * 开始日期（包含当天。只到日期，不到时分秒）yyyymmdd
	 */
	// @NotNull(message = "{PcmPricePara.bdate.isNotNull}")
	@JsonProperty(value = "BDATE")
	private String BDATE;

	/**
	 * 结束日期（包含当天。只到日期，不到时分秒。对于门店ERP则为固定值99991231）
	 */
	// @NotNull(message = "{PcmPricePara.edate.isNotNull}")
	@JsonProperty(value = "EDATE")
	private String EDATE;

	/**
	 * 变价号
	 */
	// @NotNull(message = "{PcmPricePara.changecode.isNotNull}")
	@JsonProperty(value = "CHANGECODE")
	private String CHANGECODE;

	/**
	 * 本条记录对应的操作 (A添加；U更新；D删除)
	 */
	@JsonProperty(value = "ACTION_CODE")
	private String ACTION_CODE;

	/**
	 * 操作时间：yyyyMMdd.HHmmssZ
	 */
	@JsonProperty(value = "ACTION_DATE")
	private String ACTION_DATE;

	/**
	 * 操作人
	 */
	@JsonProperty(value = "ACTION_PERSION")
	private String ACTION_PERSION;

	/**
	 * 渠道
	 */
	private String channelsid;

	/**
	 * 唯一标识（导入终端自动生成）
	 */
	// @NotNull(message = "{PcmPriceERPPara.guid.isNotNull}")
	private String guid;

	public String getSTORECODE() {
		return STORECODE;
	}

	public void setSTORECODE(String sTORECODE) {
		STORECODE = sTORECODE;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public String getSUPPLIERPRODCODE() {
		return SUPPLIERPRODCODE;
	}

	public void setSUPPLIERPRODCODE(String sUPPLIERPRODCODE) {
		SUPPLIERPRODCODE = sUPPLIERPRODCODE;
	}

	public String getZSPRICE() {
		return ZSPRICE;
	}

	public void setZSPRICE(String zSPRICE) {
		ZSPRICE = zSPRICE;
	}

	public String getSITECODE() {
		return SITECODE;
	}

	public void setSITECODE(String sITECODE) {
		SITECODE = sITECODE;
	}

	public String getWAERS() {
		return WAERS;
	}

	public void setWAERS(String wAERS) {
		WAERS = wAERS;
	}

	public String getBDATE() {
		return BDATE;
	}

	public void setBDATE(String bDATE) {
		BDATE = bDATE;
	}

	public String getEDATE() {
		return EDATE;
	}

	public void setEDATE(String eDATE) {
		EDATE = eDATE;
	}

	public String getCHANGECODE() {
		return CHANGECODE;
	}

	public void setCHANGECODE(String cHANGECODE) {
		CHANGECODE = cHANGECODE;
	}

	public String getACTION_CODE() {
		return ACTION_CODE;
	}

	public void setACTION_CODE(String aCTION_CODE) {
		ACTION_CODE = aCTION_CODE;
	}

	public String getACTION_DATE() {
		return ACTION_DATE;
	}

	public void setACTION_DATE(String aCTION_DATE) {
		ACTION_DATE = aCTION_DATE;
	}

	public String getACTION_PERSION() {
		return ACTION_PERSION;
	}

	public void setACTION_PERSION(String aCTION_PERSION) {
		ACTION_PERSION = aCTION_PERSION;
	}

	public String getChannelsid() {
		return channelsid;
	}

	public void setChannelsid(String channelsid) {
		this.channelsid = channelsid;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Override
	public String toString() {
		return "PcmPricePara [STORECODE=" + STORECODE + ", MATNR=" + MATNR + ", SUPPLIERPRODCODE="
				+ SUPPLIERPRODCODE + ", ZSPRICE=" + ZSPRICE + ", SITECODE=" + SITECODE + ", WAERS="
				+ WAERS + ", BDATE=" + BDATE + ", EDATE=" + EDATE + ", CHANGECODE=" + CHANGECODE
				+ ", ACTION_CODE=" + ACTION_CODE + ", ACTION_DATE=" + ACTION_DATE
				+ ", ACTION_PERSION=" + ACTION_PERSION + ", channelsid=" + channelsid + ", guid="
				+ guid + "]";
	}

}
