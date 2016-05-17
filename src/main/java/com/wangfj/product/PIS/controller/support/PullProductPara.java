package com.wangfj.product.PIS.controller.support;

import java.util.List;

/**
 * 上传使用Para
 * 
 * @Class Name PullProductPara
 * @Author wangsy
 * @Create In 2015年7月15日
 */
public class PullProductPara {

	/**
	 * @Field long serialVersionUID
	 */
	private static final long serialVersionUID = -2918734105564782563L;

	/**
	 * 版本
	 */
	private String version;

	/**
	 * Header
	 */
	private PullHeaderPara header;

	/**
	 * PullDataPara
	 */
	private List<PullDataPara> data;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public PullHeaderPara getHeader() {
		return header;
	}

	public void setHeader(PullHeaderPara header) {
		this.header = header;
	}

	public List<PullDataPara> getData() {
		return data;
	}

	public void setData(List<PullDataPara> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PullProductPara [version=" + version + ", header=" + header + ", data=" + data
				+ "]";
	}

}
