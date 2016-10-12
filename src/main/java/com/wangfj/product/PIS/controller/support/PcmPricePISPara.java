package com.wangfj.product.PIS.controller.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PcmPricePISPara {

	/**
	 * 门店(中台的门店编码)(必填)
	 */
	private String storecode;

	/**
	 * 变价号（调价单号，主要用于BI统计分析用）电商传值，导入终端传空(必填)
	 */
	private String changecode;

	/**
	 * 开始日期（包含当天。只到日期，不到时分秒）yyyymmdd(必填)
	 */
	private String bdate;

	/**
	 * 结束日期（包含当天。只到日期，不到时分秒。对于门店ERP则为固定值99991231）(必填)
	 */
	private String edate;

	/**
	 * 商品降价方式 ：1,金额减;2百分比减(必填)
	 */
	private String salepricetype;

	/**
	 * 商品降价力度(必填)
	 */
	private String salepricevalue;

	/**
	 * 降价原因
	 */
	private String salepricereason;

	/**
	 * 供应商编码
	 */
	private String suppliercode;

	/**
	 * 专柜编码
	 */
	private String shoppecode;

	/**
	 * 管理分类编码
	 */
	private String categorycode;

	private String channelsid;

	/**
	 * 本条记录对应的操作 (A添加；U更新；D删除)
	 */
	private String action_code;

	/**
	 * 操作时间：yyyyMMdd.HHmmssZ
	 */
	private String action_date;

	/**
	 * 操作人
	 */
	private String action_persion;

	/**
	 * guid
	 */
	private String guid;

	public String getStorecode() {
		return storecode;
	}

	public void setStorecode(String storecode) {
		this.storecode = storecode;
	}

	public String getChangecode() {
		return changecode;
	}

	public void setChangecode(String changecode) {
		this.changecode = changecode;
	}

	public String getBdate() {
		return bdate;
	}

	public void setBdate(String bdate) {
		this.bdate = bdate;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getSalepricetype() {
		return salepricetype;
	}

	public void setSalepricetype(String salepricetype) {
		this.salepricetype = salepricetype;
	}

	public String getSalepricevalue() {
		return salepricevalue;
	}

	public void setSalepricevalue(String salepricevalue) {
		this.salepricevalue = salepricevalue;
	}

	public String getSalepricereason() {
		return salepricereason;
	}

	public void setSalepricereason(String salepricereason) {
		this.salepricereason = salepricereason;
	}

	/**
	 * 供应商编码
	 */
	public String getSuppliercode() {
		return suppliercode;
	}

	/**
	 * 供应商编码
	 */
	public void setSuppliercode(String suppliercode) {
		this.suppliercode = suppliercode;
	}

	/**
	 * 专柜编码
	 */
	public String getShoppecode() {
		return shoppecode;
	}

	/**
	 * 专柜编码
	 */
	public void setShoppecode(String shoppecode) {
		this.shoppecode = shoppecode;
	}

	public String getCategorycode() {
		return categorycode;
	}

	public void setCategorycode(String categorycode) {
		this.categorycode = categorycode;
	}

	public String getChannelsid() {
		return channelsid;
	}

	public void setChannelsid(String channelsid) {
		this.channelsid = channelsid;
	}

	public String getAction_persion() {
		return action_persion;
	}

	public void setAction_persion(String action_persion) {
		this.action_persion = action_persion;
	}

	public String getAction_code() {
		return action_code;
	}

	public void setAction_code(String action_code) {
		this.action_code = action_code;
	}

	public String getAction_date() {
		return action_date;
	}

	public void setAction_date(String action_date) {
		this.action_date = action_date;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Override
	public String toString() {
		return "PcmPricePISPara [storecode=" + storecode + ", changecode=" + changecode + ", bdate="
				+ bdate + ", edate=" + edate + ", salepricetype=" + salepricetype
				+ ", salepricevalue=" + salepricevalue + ", salepricereason=" + salepricereason
				+ ", suppliercode=" + suppliercode + ", shoppecode=" + shoppecode
				+ ", categorycode=" + categorycode + ", channelsid=" + channelsid + ", action_code="
				+ action_code + ", action_date=" + action_date + ", action_persion="
				+ action_persion + ", guid=" + guid + "]";
	}

}
