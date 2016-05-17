package com.wangfj.product.core.controller.support;

import javax.validation.constraints.NotNull;

/**
 * 供应商主数据从门店ERP上传到Pcm（参数）
 * 
 * @Class Name PcmSupplyInfoPara
 * @Author wangxuan
 * @Create In 2015-8-25
 */
public class PcmSupplyInfoPara {

	@NotNull(message = "{PcmSupplyInfoPara.STORECODE.isNotNull}")
	private String STORECODE;// 中台门店编码

	@NotNull(message = "{PcmSupplyInfoPara.SUPPLIERCODE.isNotNull}")
	private String SUPPLIERCODE;// 供应商编码

	@NotNull(message = "{PcmSupplyInfoPara.SUPPLIERNAME.isNotNull}")
	private String SUPPLIERNAME;// 供应商名称

	/**
	 * 经营方式（Z001经销，Z002代销，Z003联营，Z004平台服务，Z005租赁）
	 * （门店ERP内部用的是0/2/3/9，上传前需要进行相应的转换-Z001格式上传）
	 */
	private String BUSINESSPATTERN;// 经营方式

	private String SHOARTNAME;// 供应商简称（别名）

	private String TEL_NUMBER;// 电话

	private String SMTP_ADDR;// e_mail

	private String FAX_NUMBER;// 传真

	/**
	 * 状态 (门店ERP内部的供应商状态是 Y正常；T未批准；N终止；L待审批； 3淘汰；4停货；5停款；6冻结 需要转成文字信息上传)
	 */
	private String STATUS;// 状态

	private String COUNTRY;// 国家

	private String CITY1;// 城市

	private String REGIO;// 地区代码

	private String ZZREGION;// 门店地址 （转成文字信息上传，即企业所在地区信息，例如”北京市”）

	private String STREET;// 企业地址

	private String CONTACT_ADDR;// 通讯地址

	private String POST_CODE1;// 邮编

	private String ORG_CODE;// 企业代码

	private String INDUSTRY;// 所属行业 （转成文字信息上传，即企业所在行业信息，例如”制造业”）

	private String ZZORG;// 组织机构代码

	@NotNull(message = "{PcmSupplyInfoPara.ZZLICENSE.isNotNull}")
	private String ZZLICENSE;// 营业执照号

	/**
	 * 纳税类别（门店ERP用的是：1增值税一般纳税人；2：小规模纳税人；3：交纳营业税；4：零税率；5自然人。需要转成文字上传给主数据ERP）
	 */
	private String TAXTYPE;// 纳税类别

	private String STCD1;// 税号

	private String ZZNAME_BANK;// 银行（开户行名称）

	private String ZZBANK;// 银行账号

	private String REGISTERED_CAPITAL;// 注册资本

	private String ZZPROPERTY;// 企业性质（转成文字信息上传）

	private String BUSINESS_CATEGORY;// 企业类别（转成文字信息上传）

	private String ZZID_NAME;// 法人代表

	private String ZZID_NUM;// 法人身份证号

	private String LEGAL_PERSON_CONTACT;// 法人联系方式

	private String AGENT_NAME;// 代理人

	private String AGENT_NUM;// 代理人身份证号

	private String AGENT_CONTACT;// 代理人联系方式

	private String CONTACT_NAME;// 联系人

	private String CONTACT_TITLE;// 联系人职务

	private String ZZCON_NUM;// 联系人身份证号

	private String CONTACT_WAY;// 联系人联系方式

	private String BUSINESS_SCOPE;// 经营范围

	private String KEY_SUPPLIER;// 重点供应商(Y/N)

	private String TAX_RATE;// 税率（如果对于17%的税率，传过来的值就是17）

	private String INOUT_CITY;// 市内外（1市内；2省内市外；3国内省外；4国外）转成文字信息上传

	private String ADMISSIONDATE;// 准入日期，只到日期（yyyymmdd）

	private String ZZRETURNV;// 退货至供应商(Y/N)

	private String ZZJOIN_SITE;// 联营商品客退地点（仅仅针对电商。如果ZZRETURN为Y，这个字段保存客户的退货地址，不超过200个中文字符。否则为空）

	private String APART_ORDER;// 拆单标识(Y N)

	private String DROPSHIP;// 区分奥莱和其它虚库标识（Y N）

	private String ACTION_CODE;// 操作动作(A/U/D)

	private String ACTION_DATE;// 操作时间（yyyymmdd.HHMMSS+0800）

	private String ACTION_PERSION;// 操作人

	private String ZFLG;// 区分数据来源（门店 1 电商2）

	public String getSTORECODE() {
		return STORECODE;
	}

	public void setSTORECODE(String sTORECODE) {
		STORECODE = sTORECODE == null ? null : sTORECODE.trim();
	}

	public String getSUPPLIERCODE() {
		return SUPPLIERCODE;
	}

	public void setSUPPLIERCODE(String sUPPLIERCODE) {
		SUPPLIERCODE = sUPPLIERCODE == null ? null : sUPPLIERCODE.trim();
	}

	public String getSUPPLIERNAME() {
		return SUPPLIERNAME;
	}

	public void setSUPPLIERNAME(String sUPPLIERNAME) {
		SUPPLIERNAME = sUPPLIERNAME == null ? null : sUPPLIERNAME.trim();
	}

	public String getBUSINESSPATTERN() {
		return BUSINESSPATTERN;
	}

	public void setBUSINESSPATTERN(String bUSINESSPATTERN) {
		BUSINESSPATTERN = bUSINESSPATTERN == null ? null : bUSINESSPATTERN.trim();
	}

	public String getSHOARTNAME() {
		return SHOARTNAME;
	}

	public void setSHOARTNAME(String sHOARTNAME) {
		SHOARTNAME = sHOARTNAME == null ? null : sHOARTNAME.trim();
	}

	public String getTEL_NUMBER() {
		return TEL_NUMBER;
	}

	public void setTEL_NUMBER(String tEL_NUMBER) {
		TEL_NUMBER = tEL_NUMBER == null ? null : tEL_NUMBER.trim();
	}

	public String getSMTP_ADDR() {
		return SMTP_ADDR;
	}

	public void setSMTP_ADDR(String sMTP_ADDR) {
		SMTP_ADDR = sMTP_ADDR == null ? null : sMTP_ADDR.trim();
	}

	public String getFAX_NUMBER() {
		return FAX_NUMBER;
	}

	public void setFAX_NUMBER(String fAX_NUMBER) {
		FAX_NUMBER = fAX_NUMBER == null ? null : fAX_NUMBER.trim();
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS == null ? null : sTATUS.trim();
	}

	public String getCOUNTRY() {
		return COUNTRY;
	}

	public void setCOUNTRY(String cOUNTRY) {
		COUNTRY = cOUNTRY == null ? null : cOUNTRY.trim();
	}

	public String getCITY1() {
		return CITY1;
	}

	public void setCITY1(String cITY1) {
		CITY1 = cITY1 == null ? null : cITY1.trim();
	}

	public String getREGIO() {
		return REGIO;
	}

	public void setREGIO(String rEGIO) {
		REGIO = rEGIO == null ? null : rEGIO.trim();
	}

	public String getZZREGION() {
		return ZZREGION;
	}

	public void setZZREGION(String zZREGION) {
		ZZREGION = zZREGION == null ? null : zZREGION.trim();
	}

	public String getSTREET() {
		return STREET;
	}

	public void setSTREET(String sTREET) {
		STREET = sTREET == null ? null : sTREET.trim();
	}

	public String getCONTACT_ADDR() {
		return CONTACT_ADDR;
	}

	public void setCONTACT_ADDR(String cONTACT_ADDR) {
		CONTACT_ADDR = cONTACT_ADDR == null ? null : cONTACT_ADDR.trim();
	}

	public String getPOST_CODE1() {
		return POST_CODE1;
	}

	public void setPOST_CODE1(String pOST_CODE1) {
		POST_CODE1 = pOST_CODE1 == null ? null : pOST_CODE1.trim();
	}

	public String getORG_CODE() {
		return ORG_CODE;
	}

	public void setORG_CODE(String oRG_CODE) {
		ORG_CODE = oRG_CODE == null ? null : oRG_CODE.trim();
	}

	public String getINDUSTRY() {
		return INDUSTRY;
	}

	public void setINDUSTRY(String iNDUSTRY) {
		INDUSTRY = iNDUSTRY == null ? null : iNDUSTRY.trim();
	}

	public String getZZORG() {
		return ZZORG;
	}

	public void setZZORG(String zZORG) {
		ZZORG = zZORG == null ? null : zZORG.trim();
	}

	public String getZZLICENSE() {
		return ZZLICENSE;
	}

	public void setZZLICENSE(String zZLICENSE) {
		ZZLICENSE = zZLICENSE == null ? null : zZLICENSE.trim();
	}

	public String getTAXTYPE() {
		return TAXTYPE;
	}

	public void setTAXTYPE(String tAXTYPE) {
		TAXTYPE = tAXTYPE == null ? null : tAXTYPE.trim();
	}

	public String getSTCD1() {
		return STCD1;
	}

	public void setSTCD1(String sTCD1) {
		STCD1 = sTCD1 == null ? null : sTCD1.trim();
	}

	public String getZZNAME_BANK() {
		return ZZNAME_BANK;
	}

	public void setZZNAME_BANK(String zZNAME_BANK) {
		ZZNAME_BANK = zZNAME_BANK == null ? null : zZNAME_BANK.trim();
	}

	public String getZZBANK() {
		return ZZBANK;
	}

	public void setZZBANK(String zZBANK) {
		ZZBANK = zZBANK == null ? null : zZBANK.trim();
	}

	public String getREGISTERED_CAPITAL() {
		return REGISTERED_CAPITAL;
	}

	public void setREGISTERED_CAPITAL(String rEGISTERED_CAPITAL) {
		REGISTERED_CAPITAL = rEGISTERED_CAPITAL == null ? null : rEGISTERED_CAPITAL.trim();
	}

	public String getZZPROPERTY() {
		return ZZPROPERTY;
	}

	public void setZZPROPERTY(String zZPROPERTY) {
		ZZPROPERTY = zZPROPERTY == null ? null : zZPROPERTY.trim();
	}

	public String getBUSINESS_CATEGORY() {
		return BUSINESS_CATEGORY;
	}

	public void setBUSINESS_CATEGORY(String bUSINESS_CATEGORY) {
		BUSINESS_CATEGORY = bUSINESS_CATEGORY == null ? null : bUSINESS_CATEGORY.trim();
	}

	public String getZZID_NAME() {
		return ZZID_NAME;
	}

	public void setZZID_NAME(String zZID_NAME) {
		ZZID_NAME = zZID_NAME == null ? null : zZID_NAME.trim();
	}

	public String getZZID_NUM() {
		return ZZID_NUM;
	}

	public void setZZID_NUM(String zZID_NUM) {
		ZZID_NUM = zZID_NUM == null ? null : zZID_NUM.trim();
	}

	public String getLEGAL_PERSON_CONTACT() {
		return LEGAL_PERSON_CONTACT;
	}

	public void setLEGAL_PERSON_CONTACT(String lEGAL_PERSON_CONTACT) {
		LEGAL_PERSON_CONTACT = lEGAL_PERSON_CONTACT == null ? null : lEGAL_PERSON_CONTACT.trim();
	}

	public String getAGENT_NAME() {
		return AGENT_NAME;
	}

	public void setAGENT_NAME(String aGENT_NAME) {
		AGENT_NAME = aGENT_NAME == null ? null : aGENT_NAME.trim();
	}

	public String getAGENT_NUM() {
		return AGENT_NUM;
	}

	public void setAGENT_NUM(String aGENT_NUM) {
		AGENT_NUM = aGENT_NUM == null ? null : aGENT_NUM.trim();
	}

	public String getAGENT_CONTACT() {
		return AGENT_CONTACT;
	}

	public void setAGENT_CONTACT(String aGENT_CONTACT) {
		AGENT_CONTACT = aGENT_CONTACT == null ? null : aGENT_CONTACT.trim();
	}

	public String getCONTACT_NAME() {
		return CONTACT_NAME;
	}

	public void setCONTACT_NAME(String cONTACT_NAME) {
		CONTACT_NAME = cONTACT_NAME == null ? null : cONTACT_NAME.trim();
	}

	public String getCONTACT_TITLE() {
		return CONTACT_TITLE;
	}

	public void setCONTACT_TITLE(String cONTACT_TITLE) {
		CONTACT_TITLE = cONTACT_TITLE == null ? null : cONTACT_TITLE.trim();
	}

	public String getZZCON_NUM() {
		return ZZCON_NUM;
	}

	public void setZZCON_NUM(String zZCON_NUM) {
		ZZCON_NUM = zZCON_NUM == null ? null : zZCON_NUM.trim();
	}

	public String getCONTACT_WAY() {
		return CONTACT_WAY;
	}

	public void setCONTACT_WAY(String cONTACT_WAY) {
		CONTACT_WAY = cONTACT_WAY == null ? null : cONTACT_WAY.trim();
	}

	public String getBUSINESS_SCOPE() {
		return BUSINESS_SCOPE;
	}

	public void setBUSINESS_SCOPE(String bUSINESS_SCOPE) {
		BUSINESS_SCOPE = bUSINESS_SCOPE == null ? null : bUSINESS_SCOPE.trim();
	}

	public String getKEY_SUPPLIER() {
		return KEY_SUPPLIER;
	}

	public void setKEY_SUPPLIER(String kEY_SUPPLIER) {
		KEY_SUPPLIER = kEY_SUPPLIER == null ? null : kEY_SUPPLIER.trim();
	}

	public String getTAX_RATE() {
		return TAX_RATE;
	}

	public void setTAX_RATE(String tAX_RATE) {
		TAX_RATE = tAX_RATE == null ? null : tAX_RATE.trim();
	}

	public String getINOUT_CITY() {
		return INOUT_CITY;
	}

	public void setINOUT_CITY(String iNOUT_CITY) {
		INOUT_CITY = iNOUT_CITY == null ? null : iNOUT_CITY.trim();
	}

	public String getADMISSIONDATE() {
		return ADMISSIONDATE;
	}

	public void setADMISSIONDATE(String aDMISSIONDATE) {
		ADMISSIONDATE = aDMISSIONDATE == null ? null : aDMISSIONDATE.trim();
	}

	public String getZZRETURNV() {
		return ZZRETURNV;
	}

	public void setZZRETURNV(String zZRETURNV) {
		ZZRETURNV = zZRETURNV == null ? null : zZRETURNV.trim();
	}

	public String getZZJOIN_SITE() {
		return ZZJOIN_SITE;
	}

	public void setZZJOIN_SITE(String zZJOIN_SITE) {
		ZZJOIN_SITE = zZJOIN_SITE == null ? null : zZJOIN_SITE.trim();
	}

	public String getAPART_ORDER() {
		return APART_ORDER;
	}

	public void setAPART_ORDER(String aPART_ORDER) {
		APART_ORDER = aPART_ORDER == null ? null : aPART_ORDER.trim();
	}

	public String getDROPSHIP() {
		return DROPSHIP;
	}

	public void setDROPSHIP(String dROPSHIP) {
		DROPSHIP = dROPSHIP == null ? null : dROPSHIP.trim();
	}

	public String getACTION_CODE() {
		return ACTION_CODE;
	}

	public void setACTION_CODE(String aCTION_CODE) {
		ACTION_CODE = aCTION_CODE == null ? null : aCTION_CODE.trim();
	}

	public String getACTION_DATE() {
		return ACTION_DATE;
	}

	public void setACTION_DATE(String aCTION_DATE) {
		ACTION_DATE = aCTION_DATE == null ? null : aCTION_DATE.trim();
	}

	public String getACTION_PERSION() {
		return ACTION_PERSION;
	}

	public void setACTION_PERSION(String aCTION_PERSION) {
		ACTION_PERSION = aCTION_PERSION == null ? null : aCTION_PERSION.trim();
	}

	public String getZFLG() {
		return ZFLG;
	}

	public void setZFLG(String zFLG) {
		ZFLG = zFLG == null ? null : zFLG.trim();
	}

}
