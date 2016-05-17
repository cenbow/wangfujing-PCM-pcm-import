package com.wangfj.product.core.controller.support;

import com.wangfj.product.core.controller.support.base.para.BasePara;

/**
 * 分页查询门店品牌Controller参数
 * 
 * @Class Name SelectPcmBrandPagePara
 * @Author wangx
 * @Create In 2015-8-3
 */
public class SelectPcmBrandPagePara extends BasePara {

	private Long sid;

	private String brandSid;// 门店品牌sid

	private String brandName;// 品牌名称

	private String brandNameSecond;// 品牌第二个名字

	private String spell;// 拼音

	private Integer shopType;// 门店类型(0 北京，1 外埠 ，2 电商erp)默认为0

	private Integer brandType;// 品牌类型 0 集团品牌 1 门店品牌 （默认为0）

	private Integer currentPage = 1;// 当前页数

	private Integer pageSize = 10;// 每页大小

	/** 当前页的起始索引,从0开始 */
	private int start = 0;

	/** mysql 分页 */
	private int limit = 10;

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getBrandSid() {
		return brandSid;
	}

	public void setBrandSid(String brandSid) {
		this.brandSid = brandSid;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandNameSecond() {
		return brandNameSecond;
	}

	public void setBrandNameSecond(String brandNameSecond) {
		this.brandNameSecond = brandNameSecond;
	}

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	public Integer getShopType() {
		return shopType;
	}

	public void setShopType(Integer shopType) {
		this.shopType = shopType;
	}

	public Integer getBrandType() {
		return brandType;
	}

	public void setBrandType(Integer brandType) {
		this.brandType = brandType;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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
