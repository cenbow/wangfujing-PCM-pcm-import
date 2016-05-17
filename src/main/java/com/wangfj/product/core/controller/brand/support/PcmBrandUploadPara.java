package com.wangfj.product.core.controller.brand.support;

/**
 * Created by wangxuan on 2016-03-23 0023.
 * 门店品牌上传参数
 */
public class PcmBrandUploadPara {

    private String superCode;//集团牌编码

    private String storeCode;//门店编码

    private String code;//门店品牌编码

    private String name;//门店品牌名称

    private String storeType;//门店类型（0-全局，1-电商、2-北京，3-其他门店）

    private String Name2;//门店品牌第二名称

    private String actionCode;//本条记录对应的操作

    private String actionDate;

    private String actionPerson;

    public String getSuperCode() {
        return superCode;
    }

    public void setSuperCode(String superCode) {
        this.superCode = superCode;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getName2() {
        return Name2;
    }

    public void setName2(String name2) {
        Name2 = name2;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionPerson() {
        return actionPerson;
    }

    public void setActionPerson(String actionPerson) {
        this.actionPerson = actionPerson;
    }

    @Override
    public String toString() {
        return "PcmBrandUploadPara{" +
                "superCode='" + superCode + '\'' +
                ", storeCode='" + storeCode + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", storeType='" + storeType + '\'' +
                ", Name2='" + Name2 + '\'' +
                ", actionCode='" + actionCode + '\'' +
                ", actionDate='" + actionDate + '\'' +
                ", actionPerson='" + actionPerson + '\'' +
                '}';
    }
}
