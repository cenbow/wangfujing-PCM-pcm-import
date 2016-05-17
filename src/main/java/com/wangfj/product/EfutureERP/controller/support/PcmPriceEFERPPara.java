package com.wangfj.product.EfutureERP.controller.support;

/**
 * 变价请求信息(促销中心)
 * 
 * @Class Name PcmPriceEFERPPara
 * @Author kongqf
 * @Create In 2015年11月25日
 */
public class PcmPriceEFERPPara {
	/**
	 * 主键
	 */
	private String NSEQ;
	/**
	 * 组织代码（门店编码)
	 */
	private String COCODE;
	/**
	 * 专柜（专柜商品编码)
	 */
	private String CSLCODE;
	/**
	 * 商品编码
	 */
	private String MATNR;
	/**
	 * 子商品代码（专柜商品编码）
	 */
	private String CRPSCODE;
	/**
	 * 促销价
	 */
	private String NSPRICE;
	/**
	 * 原价-吊牌价
	 */
	private String NOPRICE;
	/**
	 * 零售价
	 */
	private String NPRICE;
	/**
	 * 活动ID
	 */
	private String NEVTID;
	/**
	 * 起始日期
	 */
	private String DTHIS;
	/**
	 * 结束日期
	 */
	private String DEND;
	/**
	 * 零售商代码
	 */
	private String NRID;
	/**
	 * 经营公司代码
	 */
	private String NCORP;
	/**
	 * 组织代码
	 */
	private String NOID;
	/**
	 * 渠道代码
	 */
	private String NCHL;
	/**
	 * 部门(柜组)
	 */
	private String NDID;
	/**
	 * 专柜
	 */
	private String NSLID;

	/**
	 * 商品
	 */
	private String NRPID;
	/**
	 * 商品代码
	 */
	private String NRSPID;
	/**
	 * 商品条码
	 */
	private String CBAR;
	/**
	 * 顾客类型
	 */
	private String CCTYP;
	/**
	 * 商品类型
	 */
	private String NPTYP;
	/**
	 * 渠道代码
	 */
	private String CCHLCODE;
	/**
	 * 部门(柜组)
	 */
	private String CDCODE;
	/**
	 * 商品代码
	 */
	private String CRPCODE;
	/**
	 * 促销标签
	 */
	private String CEVTTAG;
	/**
	 * 促销标签描述
	 */
	private String CDESC;
	/**
	 * 备注
	 */
	private String CNOTE;
	/**
	 * 状态
	 */
	private String NSTA;
	/**
	 * 创建日期
	 */
	private String TCRD;
	/**
	 * 修改日期
	 */
	private String TMDD;

	public String getNSEQ() {
		return NSEQ;
	}

	public void setNSEQ(String nSEQ) {
		NSEQ = nSEQ;
	}

	public String getCOCODE() {
		return COCODE;
	}

	public void setCOCODE(String cOCODE) {
		COCODE = cOCODE;
	}

	public String getCSLCODE() {
		return CSLCODE;
	}

	public void setCSLCODE(String cSLCODE) {
		CSLCODE = cSLCODE;
	}

	public String getCRPSCODE() {
		return CRPSCODE;
	}

	public void setCRPSCODE(String cRPSCODE) {
		CRPSCODE = cRPSCODE;
	}

	public String getNSPRICE() {
		return NSPRICE;
	}

	public void setNSPRICE(String nSPRICE) {
		NSPRICE = nSPRICE;
	}

	public String getNOPRICE() {
		return NOPRICE;
	}

	public void setNOPRICE(String nOPRICE) {
		NOPRICE = nOPRICE;
	}

	public String getNPRICE() {
		return NPRICE;
	}

	public void setNPRICE(String nPRICE) {
		NPRICE = nPRICE;
	}

	public String getNEVTID() {
		return NEVTID;
	}

	public void setNEVTID(String nEVTID) {
		NEVTID = nEVTID;
	}

	public String getDTHIS() {
		return DTHIS;
	}

	public void setDTHIS(String dTHIS) {
		DTHIS = dTHIS;
	}

	public String getDEND() {
		return DEND;
	}

	public void setDEND(String dEND) {
		DEND = dEND;
	}

	public String getNRID() {
		return NRID;
	}

	public void setNRID(String nRID) {
		NRID = nRID;
	}

	public String getNCORP() {
		return NCORP;
	}

	public void setNCORP(String nCORP) {
		NCORP = nCORP;
	}

	public String getNOID() {
		return NOID;
	}

	public void setNOID(String nOID) {
		NOID = nOID;
	}

	public String getNCHL() {
		return NCHL;
	}

	public void setNCHL(String nCHL) {
		NCHL = nCHL;
	}

	public String getNDID() {
		return NDID;
	}

	public void setNDID(String nDID) {
		NDID = nDID;
	}

	public String getNSLID() {
		return NSLID;
	}

	public void setNSLID(String nSLID) {
		NSLID = nSLID;
	}

	public String getNRPID() {
		return NRPID;
	}

	public void setNRPID(String nRPID) {
		NRPID = nRPID;
	}

	public String getNRSPID() {
		return NRSPID;
	}

	public void setNRSPID(String nRSPID) {
		NRSPID = nRSPID;
	}

	public String getCBAR() {
		return CBAR;
	}

	public void setCBAR(String cBAR) {
		CBAR = cBAR;
	}

	public String getCCTYP() {
		return CCTYP;
	}

	public void setCCTYP(String cCTYP) {
		CCTYP = cCTYP;
	}

	public String getNPTYP() {
		return NPTYP;
	}

	public void setNPTYP(String nPTYP) {
		NPTYP = nPTYP;
	}

	public String getCCHLCODE() {
		return CCHLCODE;
	}

	public void setCCHLCODE(String cCHLCODE) {
		CCHLCODE = cCHLCODE;
	}

	public String getCDCODE() {
		return CDCODE;
	}

	public void setCDCODE(String cDCODE) {
		CDCODE = cDCODE;
	}

	public String getCRPCODE() {
		return CRPCODE;
	}

	public void setCRPCODE(String cRPCODE) {
		CRPCODE = cRPCODE;
	}

	public String getCEVTTAG() {
		return CEVTTAG;
	}

	public void setCEVTTAG(String cEVTTAG) {
		CEVTTAG = cEVTTAG;
	}

	public String getCDESC() {
		return CDESC;
	}

	public void setCDESC(String cDESC) {
		CDESC = cDESC;
	}

	public String getCNOTE() {
		return CNOTE;
	}

	public void setCNOTE(String cNOTE) {
		CNOTE = cNOTE;
	}

	public String getNSTA() {
		return NSTA;
	}

	public void setNSTA(String nSTA) {
		NSTA = nSTA;
	}

	public String getTCRD() {
		return TCRD;
	}

	public void setTCRD(String tCRD) {
		TCRD = tCRD;
	}

	public String getTMDD() {
		return TMDD;
	}

	public void setTMDD(String tMDD) {
		TMDD = tMDD;
	}

	public String getMATNR() {
		return MATNR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

}
