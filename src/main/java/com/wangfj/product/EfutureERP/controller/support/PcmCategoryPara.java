package com.wangfj.product.EfutureERP.controller.support;

import javax.validation.constraints.NotNull;

import com.wangfj.product.core.controller.support.base.para.BasePara;

/**
 * 品类信息参数
 * 
 * @Class Name PcmCategory
 * @Author duanzhaole
 * @Create In 2015年7月2日
 */
public class PcmCategoryPara extends BasePara {

	/**
	 * @Field long serialVersionUID
	 */
	private static final long serialVersionUID = 358815721526422574L;

	/**
	 * 中台门店编码
	 */
	@NotNull(message = "{PcmCategoryPara.shopSid.isNotNull}")
	private String shopSid;

	/**
	 * 编码
	 */
	@NotNull(message = "{PcmCategoryPara.category_sid.isNotNull}")
	private String category_sid;

	/**
	 * 名称
	 */
	@NotNull(message = "{PcmCategoryPara.name.isNotNull}")
	private String name;

	/**
	 * 上级编码
	 */
	@NotNull(message = "{PcmCategoryPara.parent_sid.isNotNull}")
	private String parent_sid;

	/**
	 * 是否末级（Y/N）
	 */
	@NotNull(message = "{PcmCategoryPara.is_leaf.isNotNull}")
	private Integer is_leaf;

	/**
	 * 级次（1,2,3,4）（外地是3级，北京是4级）
	 */
	@NotNull(message = "{PcmCategoryPara.level.isNotNull}")
	private Integer level;

	/**
	 * 可用状态（Y/N）
	 */
	@NotNull(message = "{PcmCategoryPara.status.isNotNull}")
	private Integer status;

	/**
	 * 超市百货标志（Y/N），Y超市，N百货。对于超市大类下的所有管理分类节点，本字段为N，否则为Y。
	 */
	@NotNull(message = "{PcmCategoryPara.is_market.isNotNull}")
	private Integer is_market;

	/**
	 * A(创建)，U(更新,如果不存在则创建)，D(删除)
	 */
	@NotNull(message = "{PcmCategoryPara.actionCode.isNotNull}")
	private String actionCode;

	/**
	 * 操作发起时间戳(yyyyMMdd.HHmmssZ)，例如”20141008.101830+0800”，可以为空。
	 */
	private String actionDate;

	/**
	 * 操作员标识(英文标识)。可以为空。
	 */
	private String actionPerson;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getShopSid() {
		return shopSid;
	}

	public void setShopSid(String shopSid) {
		this.shopSid = shopSid;
	}

	public String getCategory_sid() {
		return category_sid;
	}

	public void setCategory_sid(String category_sid) {
		this.category_sid = category_sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent_sid() {
		return parent_sid;
	}

	public void setParent_sid(String parent_sid) {
		this.parent_sid = parent_sid;
	}

	public Integer getIs_leaf() {
		return is_leaf;
	}

	public void setIs_leaf(Integer is_leaf) {
		this.is_leaf = is_leaf;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIs_market() {
		return is_market;
	}

	public void setIs_market(Integer is_market) {
		this.is_market = is_market;
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
		return "PcmCategoryPara [shopSid=" + shopSid + ", category_sid="
				+ category_sid + ", name=" + name + ", parent_sid=" + parent_sid + ", is_leaf="
				+ is_leaf + ", level=" + level + ", status=" + status + ", is_market=" + is_market
				+ ", actionCode=" + actionCode + ", actionDate=" + actionDate + ", actionPerson="
				+ actionPerson + "]";
	}

}