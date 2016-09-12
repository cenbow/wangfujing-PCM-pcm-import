package com.wangfj.product.core.controller.supplier.support;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * 供应商主数据从门店ERP上传到Pcm（参数）
 *
 * @Class Name PcmSupplyInfoPara
 * @Author wangxuan
 * @Create In 2015-8-25
 */
public class PcmSupplierEBusinessPara {

    @NotNull(message = "{PcmSupplyInfoPara.STORECODE.isNotNull}")
    @JsonProperty(value = "STORECODE")
    private String STORECODE;// 中台门店编码

    @NotNull(message = "{PcmSupplyInfoPara.LIFNR.isNotNull}")
    @JsonProperty(value = "LIFNR")
    private String LIFNR;// 供应商编码

    @NotNull(message = "{PcmSupplyInfoPara.NAME1.isNotNull}")
    @JsonProperty(value = "NAME1")
    private String NAME1;// 供应商名称

    /**
     * 经营方式（Z001经销，Z002代销，Z003联营，Z004平台服务，Z005租赁）
     * （门店ERP内部用的是0/2/3/9，上传前需要进行相应的转换-Z001格式上传）
     */
    @JsonProperty(value = "KTOKK")
    private String KTOKK;// 经营方式

    @JsonProperty(value = "TEL_NUMBER")
    private String TEL_NUMBER;// 电话

    @JsonProperty(value = "SMTP_ADDR")
    private String SMTP_ADDR;// e_mail

    @JsonProperty(value = "FAX_NUMBER")
    private String FAX_NUMBER;// 传真

    @JsonProperty(value = "COUNTRY")
    private String COUNTRY;// 国家

    @JsonProperty(value = "CITY1")
    private String CITY1;// 城市

    @JsonProperty(value = "REGIO")
    private String REGIO;// 地区代码

    @JsonProperty(value = "ZZREGION")
    private String ZZREGION;// 市内外（1市内；2省内市外；3国内省外；4国外）转成文字信息上传

    @JsonProperty(value = "STREET")
    private String STREET;// 企业地址

    @JsonProperty(value = "POST_CODE1")
    private String POST_CODE1;// 邮编

    @JsonProperty(value = "BRSCH")
    private String BRSCH;// 所属行业 （转成文字信息上传，即企业所在行业信息，例如”制造业”）

    @JsonProperty(value = "ZZORG")
    private String ZZORG;// 组织机构代码证号

    @NotNull(message = "{PcmSupplyInfoPara.ZZLICENSE.isNotNull}")
    @JsonProperty(value = "ZZLICENSE")
    private String ZZLICENSE;// 营业执照号

    /**
     * 纳税类别（门店ERP用的是：1增值税一般纳税人；2：小规模纳税人；3：交纳营业税；4：零税率；5自然人。需要转成文字上传给主数据ERP）
     */
    @JsonProperty(value = "FITYP")
    private String FITYP;// 纳税类别

    @JsonProperty(value = "STCD1")
    private String STCD1;// 税号

    @JsonProperty(value = "ZZNAME_B")
    private String ZZNAME_B;// 银行（开户行名称）

    @JsonProperty(value = "ZZBANK")
    private String ZZBANK;// 银行账号

    @JsonProperty(value = "ZZPROPERTY")
    private String ZZPROPERTY;// 企业性质（转成文字信息上传）

    @JsonProperty(value = "ZZID_NAME")
    private String ZZID_NAME;// 法人代表

    @JsonProperty(value = "ZZID_NUM")
    private String ZZID_NUM;// 法人身份证号

    @JsonProperty(value = "ZZCONTACT")
    private String ZZCONTACT;// 联系人

    @JsonProperty(value = "ZZCON_NUM")
    private String ZZCON_NUM;// 联系人身份证号

    @JsonProperty(value = "ZZMWSKZ")
    private String ZZMWSKZ;// 税率（如果对于17%的税率，传过来的值就是17）

    @JsonProperty(value = "ZZRETURNV")
    private String ZZRETURNV;// 退货至供应商(Y/N)

    @JsonProperty(value = "ZZJOIN_SITE")
    private String ZZJOIN_SITE;// 联营商品客退地点（仅仅针对电商。如果ZZRETURN为Y，这个字段保存客户的退货地址，不超过200个中文字符。否则为空）

    @JsonProperty(value = "APART_ORDER")
    private String APART_ORDER;// 拆单标识(Y N)

    @JsonProperty(value = "DROPSHIP")
    private String DROPSHIP;// 区分奥莱和其它虚库标识（Y N）

    @JsonProperty(value = "ACTION_CODE")
    private String ACTION_CODE;// 操作动作(A/U/D)

    @JsonProperty(value = "ACTION_DATE")
    private String ACTION_DATE;// 操作时间（yyyymmdd.HHMMSS+0800）

    @JsonProperty(value = "ACTION_PERSION")
    private String ACTION_PERSION;// 操作人

    @JsonProperty(value = "ZLY_FLAG")
    private String ZLY_FLAG;// 虚库标志（Y/N）

    @JsonProperty(value = "ZZXXHC_FLAG")
    private String ZZXXHC_FLAG;// 先销后采(Y/N)

    public String getSTORECODE() {
        return STORECODE;
    }

    public void setSTORECODE(String sTORECODE) {
        STORECODE = sTORECODE == null ? null : sTORECODE.trim();
    }

    public String getLIFNR() {
        return LIFNR;
    }

    public void setLIFNR(String lIFNR) {
        LIFNR = lIFNR == null ? null : lIFNR.trim();
    }

    public String getNAME1() {
        return NAME1;
    }

    public void setNAME1(String nAME1) {
        NAME1 = nAME1 == null ? null : nAME1.trim();
    }

    public String getKTOKK() {
        return KTOKK;
    }

    public void setKTOKK(String kTOKK) {
        KTOKK = kTOKK == null ? null : kTOKK.trim();
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

    public String getPOST_CODE1() {
        return POST_CODE1;
    }

    public void setPOST_CODE1(String pOST_CODE1) {
        POST_CODE1 = pOST_CODE1 == null ? null : pOST_CODE1.trim();
    }

    public String getBRSCH() {
        return BRSCH;
    }

    public void setBRSCH(String bRSCH) {
        BRSCH = bRSCH == null ? null : bRSCH.trim();
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

    public String getFITYP() {
        return FITYP;
    }

    public void setFITYP(String fITYP) {
        FITYP = fITYP == null ? null : fITYP.trim();
    }

    public String getSTCD1() {
        return STCD1;
    }

    public void setSTCD1(String sTCD1) {
        STCD1 = sTCD1 == null ? null : sTCD1.trim();
    }

    public String getZZNAME_B() {
        return ZZNAME_B;
    }

    public void setZZNAME_B(String zZNAME_B) {
        ZZNAME_B = zZNAME_B == null ? null : zZNAME_B.trim();
    }

    public String getZZBANK() {
        return ZZBANK;
    }

    public void setZZBANK(String zZBANK) {
        ZZBANK = zZBANK == null ? null : zZBANK.trim();
    }

    public String getZZPROPERTY() {
        return ZZPROPERTY;
    }

    public void setZZPROPERTY(String zZPROPERTY) {
        ZZPROPERTY = zZPROPERTY == null ? null : zZPROPERTY.trim();
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

    public String getZZCONTACT() {
        return ZZCONTACT;
    }

    public void setZZCONTACT(String zZCONTACT) {
        ZZCONTACT = zZCONTACT == null ? null : zZCONTACT.trim();
    }

    public String getZZCON_NUM() {
        return ZZCON_NUM;
    }

    public void setZZCON_NUM(String zZCON_NUM) {
        ZZCON_NUM = zZCON_NUM == null ? null : zZCON_NUM.trim();
    }

    public String getZZMWSKZ() {
        return ZZMWSKZ;
    }

    public void setZZMWSKZ(String zZMWSKZ) {
        ZZMWSKZ = zZMWSKZ == null ? null : zZMWSKZ.trim();
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

    public String getZLY_FLAG() {
        return ZLY_FLAG;
    }

    public void setZLY_FLAG(String zLY_FLAG) {
        ZLY_FLAG = zLY_FLAG;
    }

    public String getZZXXHC_FLAG() {
        return ZZXXHC_FLAG;
    }

    public void setZZXXHC_FLAG(String zZXXHC_FLAG) {
        ZZXXHC_FLAG = zZXXHC_FLAG;
    }

    @Override
    public String toString() {
        return "PcmSupplierEBusinessPara [STORECODE=" + STORECODE + ", LIFNR=" + LIFNR + ", NAME1="
                + NAME1 + ", KTOKK=" + KTOKK + ", TEL_NUMBER=" + TEL_NUMBER + ", SMTP_ADDR="
                + SMTP_ADDR + ", FAX_NUMBER=" + FAX_NUMBER + ", COUNTRY=" + COUNTRY + ", CITY1="
                + CITY1 + ", REGIO=" + REGIO + ", ZZREGION=" + ZZREGION + ", STREET=" + STREET
                + ", POST_CODE1=" + POST_CODE1 + ", BRSCH=" + BRSCH + ", ZZORG=" + ZZORG
                + ", ZZLICENSE=" + ZZLICENSE + ", FITYP=" + FITYP + ", STCD1=" + STCD1
                + ", ZZNAME_B=" + ZZNAME_B + ", ZZBANK=" + ZZBANK + ", ZZPROPERTY=" + ZZPROPERTY
                + ", ZZID_NAME=" + ZZID_NAME + ", ZZID_NUM=" + ZZID_NUM + ", ZZCONTACT="
                + ZZCONTACT + ", ZZCON_NUM=" + ZZCON_NUM + ", ZZMWSKZ=" + ZZMWSKZ + ", ZZRETURNV="
                + ZZRETURNV + ", ZZJOIN_SITE=" + ZZJOIN_SITE + ", APART_ORDER=" + APART_ORDER
                + ", DROPSHIP=" + DROPSHIP + ", ACTION_CODE=" + ACTION_CODE + ", ACTION_DATE="
                + ACTION_DATE + ", ACTION_PERSION=" + ACTION_PERSION + ", ZLY_FLAG=" + ZLY_FLAG
                + ", ZZXXHC_FLAG=" + ZZXXHC_FLAG + "]";
    }

}
