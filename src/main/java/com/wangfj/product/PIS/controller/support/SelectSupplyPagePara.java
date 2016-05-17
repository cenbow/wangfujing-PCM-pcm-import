/**
 * @Probject Name: pcm-core
 * @Path: com.wangfj.product.PIS.controller.supportSelectSupplyPagePara.java
 * @Create By wuxiong
 * @Create In 2015年7月22日 下午9:44:43
 * TODO
 */
package com.wangfj.product.PIS.controller.support;

/**
 * @Class Name SelectSupplyPagePara
 * @Author wuxiong
 * @Create In 2015年7月22日
 */
public class SelectSupplyPagePara {
	private String supplyCode;// 供应商编码
	private Integer supplierLevel;// 供应商级别
	private String supplierName;// 中台供应商名称
	private String storeCode;// 门店编码
	private Integer operateMode;// 经营方式
	private String operateStatus;// 经营状态
	private String approvalDate;// 供应商注册日期
	private String returnSupply;// 退货至供应商
	private String joinSite;// 联营商品客退地点
	/** 当前页的起始索引,从1开始 */
	protected int start = 1;

	/** mysql 分页 */
	protected int limit = 10;

	public String getSupplyCode() {
		return supplyCode;
	}

	public void setSupplyCode(String supplyCode) {
		this.supplyCode = supplyCode;
	}

	public Integer getSupplierLevel() {
		return supplierLevel;
	}

	public void setSupplierLevel(Integer supplierLevel) {
		this.supplierLevel = supplierLevel;
	}

	public Integer getOperateMode() {
		return operateMode;
	}

	public void setOperateMode(Integer operateMode) {
		this.operateMode = operateMode;
	}

	public String getReturnSupply() {
		return returnSupply;
	}

	public void setReturnSupply(String returnSupply) {
		this.returnSupply = returnSupply;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getOperateStatus() {
		return operateStatus;
	}

	public void setOperateStatus(String operateStatus) {
		this.operateStatus = operateStatus;
	}

	public String getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getJoinSite() {
		return joinSite;
	}

	public void setJoinSite(String joinSite) {
		this.joinSite = joinSite;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
