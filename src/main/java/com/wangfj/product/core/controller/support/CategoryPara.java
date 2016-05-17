/**
 * 
 */
package com.wangfj.product.core.controller.support;

/**
 * 品类para参数
 * 
 * @Class Name CategoryPara
 * @Author duanzhaole
 * @Create In 2015年8月3日
 */
public class CategoryPara {

	private String id;

	private String channelSid;
	
	private String productSid;
	
	private String sid;

	private String name;
	private String status;

	private Integer isDisplay;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannelSid() {
		return channelSid;
	}

	public void setChannelSid(String channelSid) {
		this.channelSid = channelSid;
	}

	public String getProductSid() {
		return productSid;
	}

	public void setProductSid(String productSid) {
		this.productSid = productSid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}

}
