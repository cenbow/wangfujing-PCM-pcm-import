package com.wangfj.product.core.controller.supplier.support;

import javax.validation.constraints.NotNull;

/**
 * 一品多供应商关系上传(输入参数)
 * 
 * @Class Name PcmShoppeProductSupplierEBusinessPara
 * @Author wangxuan
 * @Create In 2015-11-16
 */
public class PcmShoppeProductSupplierEBusinessPara {

	@NotNull(message = "{PcmShoppeProSupplyPara.STORECODE.isNotNull}")
	private String STORECODE;// 门店编号

	@NotNull(message = "{PcmShoppeProSupplyPara.MATNR.isNotNull}")
	private String MATNR;// 商品编码

	@NotNull(message = "{PcmShoppeProSupplyPara.S_MATNR.isNotNull}")
	private String S_MATNR;// 专柜商品编码

	@NotNull(message = "{PcmShoppeProSupplyPara.LIFNR.isNotNull}")
	private String LIFNR;// 商品对应的供应商编码

	@NotNull(message = "{PcmShoppeProSupplyPara.ACTIONCODE.isNotNull}")
	private String ACTIONCODE;// 本条记录对应的操作 (A添加；U更新；D删除)

	private String ACTIONDATE;// 操作时间（格式为yyyyMMdd.HHmmssZ)

	private String ACTIONPERSON;// 操作人

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

	public String getS_MATNR() {
		return S_MATNR;
	}

	public void setS_MATNR(String s_MATNR) {
		S_MATNR = s_MATNR;
	}

	public String getLIFNR() {
		return LIFNR;
	}

	public void setLIFNR(String lIFNR) {
		LIFNR = lIFNR;
	}

	public String getACTIONCODE() {
		return ACTIONCODE;
	}

	public void setACTIONCODE(String aCTIONCODE) {
		ACTIONCODE = aCTIONCODE;
	}

	public String getACTIONDATE() {
		return ACTIONDATE;
	}

	public void setACTIONDATE(String aCTIONDATE) {
		ACTIONDATE = aCTIONDATE;
	}

	public String getACTIONPERSON() {
		return ACTIONPERSON;
	}

	public void setACTIONPERSON(String aCTIONPERSON) {
		ACTIONPERSON = aCTIONPERSON;
	}

	@Override
	public String toString() {
		return "PcmShoppeProductSupplierEBusinessPara [STORECODE=" + STORECODE + ", MATNR=" + MATNR
				+ ", S_MATNR=" + S_MATNR + ", LIFNR=" + LIFNR + ", ACTIONCODE=" + ACTIONCODE
				+ ", ACTIONDATE=" + ACTIONDATE + ", ACTIONPERSON=" + ACTIONPERSON + "]";
	}

}
