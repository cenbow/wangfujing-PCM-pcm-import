package com.wangfj.product.EfutureERP.controller.support;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 品类信息参数
 * 
 * @Class Name PcmCategory
 * @Author duanzhaole
 * @Create In 2015年7月2日
 */
public class PcmManageCategoryPara {


	/**
	 * @Field long serialVersionUID
	 */
	private static final long serialVersionUID = 358815721526422574L;

	private Long sid;

	/**
	 * 中台门店编码
	 */
	@JsonProperty("StoreCode") 
	@NotNull(message = "{PcmCategoryPara.StoreCode.isNotNull}")
	private String StoreCode;

	/**
	 * 编码
	 */
	@JsonProperty("CODE") 
	private String CODE;

	/**
	 * 名称
	 */
	@JsonProperty("NAME") 
	@NotNull(message = "{PcmCategoryPara.NAME.isNotNull}")
	private String NAME;

	/**
	 * 上级编码
	 */
	@JsonProperty("SJCODE") 
	@NotNull(message = "{PcmCategoryPara.SJCODE.isNotNull}")
	private String SJCODE;

	/**
	 * 是否末级（Y/N）
	 */
	@JsonProperty("FLAG") 
	@NotNull(message = "{PcmCategoryPara.FLAG.isNotNull}")
	private String FLAG;

	/**
	 * 级次（1,2,3,4）（外地是3级，北京是4级）
	 */
	@JsonProperty("TYPE") 
	@NotNull(message = "{PcmCategoryPara.TYPE.isNotNull}")
	private String TYPE;

	/**
	 * 可用状态（Y/N）
	 */
	@JsonProperty("STATUS") 
	@NotNull(message = "{PcmCategoryPara.STATUS.isNotNull}")
	private String STATUS;

	/**
	 * 超市百货标志（Y/N），Y超市，N百货。对于超市大类下的所有管理分类节点，本字段为N，否则为Y。
	 */
	@JsonProperty("ISZG") 
	@NotNull(message = "{PcmCategoryPara.ISZG.isNotNull}")
	private String ISZG;

	/**
	 * 品类类型
	 */
	@JsonProperty("categoryType") 
	private Integer categoryType;

	/**
	 * A(创建)，U(更新,如果不存在则创建)，D(删除)
	 */
	@NotNull(message = "{PcmCategoryPara.actionCode.isNotNull}")
	private String actionCode;

	/**
	 * 操作发起时间戳(yyyyMMdd.HHmmssZ)，例如”20141008.101830+0800”，可以为空。
	 */
	@JsonProperty("actionDate") 
	private String actionDate;

	/**
	 * 操作员标识(英文标识)。可以为空。
	 */
	@JsonProperty("actionPerson") 
	private String actionPerson;

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	

	public String getStoreCode() {
		return StoreCode;
	}

	public void setStoreCode(String storeCode) {
		StoreCode = storeCode;
	}

	public String getCODE() {
		return CODE;
	}

	public void setCODE(String cODE) {
		CODE = cODE;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getSJCODE() {
		return SJCODE;
	}

	public void setSJCODE(String sJCODE) {
		SJCODE = sJCODE;
	}

	public String getFLAG() {
		return FLAG;
	}

	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getISZG() {
		return ISZG;
	}

	public void setISZG(String iSZG) {
		ISZG = iSZG;
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


	public Integer getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(Integer categoryType) {
		this.categoryType = categoryType;
	}

}