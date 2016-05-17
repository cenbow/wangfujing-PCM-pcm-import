/**
 * @Probject Name: pcm-core
 * @Path: com.wangfj.product.core.controller.supportCategoryPropsDictPara.java
 * @Create By duanzhaole
 * @Create In 2015年7月31日 上午10:21:41
 * TODO
 */
package com.wangfj.product.core.controller.support;

/**
 * 品类属性para参数
 * 
 * @Class Name CategoryPropsDictPara
 * @Author duanzhaole
 * @Create In 2015年7月31日
 */
public class CategoryPropsDictPara {

	/**
	 * 属性名
	 */
	private String propsName;
	/**
	 * 属性描述
	 */
	private String propsDesc;

	/**
	 * 属性code
	 */
	private String propsCode;

	/**
	 * 状态：0 可用，1禁用 默认为0
	 */
	private Long status;


	private String id;

	private String sid;

	private String insert1;

	private String update1;

	private String delete1;

	/**
	 * 渠道sid
	 */
	private Long channelSid;

	private Integer limit = 20;
	private Integer start = 0;

	public String getPropsName() {
		return propsName;
	}

	public void setPropsName(String propsName) {
		this.propsName = propsName;
	}

	public String getPropsDesc() {
		return propsDesc;
	}

	public void setPropsDesc(String propsDesc) {
		this.propsDesc = propsDesc;
	}

	public String getPropsCode() {
		return propsCode;
	}

	public void setPropsCode(String propsCode) {
		this.propsCode = propsCode;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getInsert1() {
		return insert1;
	}

	public void setInsert1(String insert1) {
		this.insert1 = insert1;
	}

	public String getUpdate1() {
		return update1;
	}

	public void setUpdate1(String update1) {
		this.update1 = update1;
	}

	public String getDelete1() {
		return delete1;
	}

	public void setDelete1(String delete1) {
		this.delete1 = delete1;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Long getChannelSid() {
		return channelSid;
	}

	public void setChannelSid(Long channelSid) {
		this.channelSid = channelSid;
	}

}
