package com.wangfj.product.core.controller.supplier.support;

/**
 * Created by wangxuan on 2016-10-11 0011.
 * 集团供应商从供应商平台上传到PCM参数
 */
public class PcmSupplyGroupPara {

    private String SUPPLIERCODE;// 集团供应商编码（资质编号）

    private String SUPPLIERNAME;// 供应商名称

    private String ZZPROPERTY;// 企业性质（转成文字信息上传）

    private String CONTACT_ADDR;// 通讯地址

    private String REGISTERED_CAPITAL;// 注册资本

    private String ZZID_NAME;// 法人代表

    private String ZZID_NUM;// 法人身份证号

    private String CONTACT_NAME;// 联系人

    private String SMTP_ADDR;// e_mail（联系人邮箱）

    private String CONTACT_WAY;// 联系人联系方式（联系人电话）

    private String ZZCON_NUM;// 联系人身份证号

    private String FAX_NUMBER;// 传真（联系人传真）

    private String ZZBANK;// 银行账号（开户账号）

    private String ZZNAME_BANK;// 银行（开户行名称）

    private String ZZLICENSE;// 营业执照号

    private String ZZORG;// 组织机构代码

    private String STCD1;// 税号（税务登记证）

    private String TAXPAYER_CERTIFICATE;//纳税人资格证

    private String ACCOUNT_NUMBER;//开户许可号

    private String ACTION_CODE;// 操作动作(A/U/D)

    private String ACTION_DATE;// 操作时间（yyyymmdd.HHMMSS+0800）

    private String ACTION_PERSION;// 操作人

    public String getSUPPLIERCODE() {
        return SUPPLIERCODE;
    }

    public void setSUPPLIERCODE(String SUPPLIERCODE) {
        this.SUPPLIERCODE = SUPPLIERCODE;
    }

    public String getSUPPLIERNAME() {
        return SUPPLIERNAME;
    }

    public void setSUPPLIERNAME(String SUPPLIERNAME) {
        this.SUPPLIERNAME = SUPPLIERNAME;
    }

    public String getZZPROPERTY() {
        return ZZPROPERTY;
    }

    public void setZZPROPERTY(String ZZPROPERTY) {
        this.ZZPROPERTY = ZZPROPERTY;
    }

    public String getCONTACT_ADDR() {
        return CONTACT_ADDR;
    }

    public void setCONTACT_ADDR(String CONTACT_ADDR) {
        this.CONTACT_ADDR = CONTACT_ADDR;
    }

    public String getREGISTERED_CAPITAL() {
        return REGISTERED_CAPITAL;
    }

    public void setREGISTERED_CAPITAL(String REGISTERED_CAPITAL) {
        this.REGISTERED_CAPITAL = REGISTERED_CAPITAL;
    }

    public String getZZID_NAME() {
        return ZZID_NAME;
    }

    public void setZZID_NAME(String ZZID_NAME) {
        this.ZZID_NAME = ZZID_NAME;
    }

    public String getZZID_NUM() {
        return ZZID_NUM;
    }

    public void setZZID_NUM(String ZZID_NUM) {
        this.ZZID_NUM = ZZID_NUM;
    }

    public String getCONTACT_NAME() {
        return CONTACT_NAME;
    }

    public void setCONTACT_NAME(String CONTACT_NAME) {
        this.CONTACT_NAME = CONTACT_NAME;
    }

    public String getSMTP_ADDR() {
        return SMTP_ADDR;
    }

    public void setSMTP_ADDR(String SMTP_ADDR) {
        this.SMTP_ADDR = SMTP_ADDR;
    }

    public String getCONTACT_WAY() {
        return CONTACT_WAY;
    }

    public void setCONTACT_WAY(String CONTACT_WAY) {
        this.CONTACT_WAY = CONTACT_WAY;
    }

    public String getZZCON_NUM() {
        return ZZCON_NUM;
    }

    public void setZZCON_NUM(String ZZCON_NUM) {
        this.ZZCON_NUM = ZZCON_NUM;
    }

    public String getFAX_NUMBER() {
        return FAX_NUMBER;
    }

    public void setFAX_NUMBER(String FAX_NUMBER) {
        this.FAX_NUMBER = FAX_NUMBER;
    }

    public String getZZBANK() {
        return ZZBANK;
    }

    public void setZZBANK(String ZZBANK) {
        this.ZZBANK = ZZBANK;
    }

    public String getZZNAME_BANK() {
        return ZZNAME_BANK;
    }

    public void setZZNAME_BANK(String ZZNAME_BANK) {
        this.ZZNAME_BANK = ZZNAME_BANK;
    }

    public String getZZLICENSE() {
        return ZZLICENSE;
    }

    public void setZZLICENSE(String ZZLICENSE) {
        this.ZZLICENSE = ZZLICENSE;
    }

    public String getZZORG() {
        return ZZORG;
    }

    public void setZZORG(String ZZORG) {
        this.ZZORG = ZZORG;
    }

    public String getSTCD1() {
        return STCD1;
    }

    public void setSTCD1(String STCD1) {
        this.STCD1 = STCD1;
    }

    public String getTAXPAYER_CERTIFICATE() {
        return TAXPAYER_CERTIFICATE;
    }

    public void setTAXPAYER_CERTIFICATE(String TAXPAYER_CERTIFICATE) {
        this.TAXPAYER_CERTIFICATE = TAXPAYER_CERTIFICATE;
    }

    public String getACCOUNT_NUMBER() {
        return ACCOUNT_NUMBER;
    }

    public void setACCOUNT_NUMBER(String ACCOUNT_NUMBER) {
        this.ACCOUNT_NUMBER = ACCOUNT_NUMBER;
    }

    public String getACTION_CODE() {
        return ACTION_CODE;
    }

    public void setACTION_CODE(String ACTION_CODE) {
        this.ACTION_CODE = ACTION_CODE;
    }

    public String getACTION_DATE() {
        return ACTION_DATE;
    }

    public void setACTION_DATE(String ACTION_DATE) {
        this.ACTION_DATE = ACTION_DATE;
    }

    public String getACTION_PERSION() {
        return ACTION_PERSION;
    }

    public void setACTION_PERSION(String ACTION_PERSION) {
        this.ACTION_PERSION = ACTION_PERSION;
    }
}
