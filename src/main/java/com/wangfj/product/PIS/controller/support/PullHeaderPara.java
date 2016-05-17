package com.wangfj.product.PIS.controller.support;

/**
 * 上传headerPara
 * 
 * @Class Name PullHeaderPara
 * @Author wangsy
 * @Create In 2015年7月15日
 */
public class PullHeaderPara {

	private String reset;

	/**
	 * 消息描述
	 */
	private String titile;

	/**
	 * 数量
	 */
	private String count;

	public String getReset() {
		return reset;
	}

	public void setReset(String reset) {
		this.reset = reset;
	}

	public String getTitile() {
		return titile;
	}

	public void setTitile(String titile) {
		this.titile = titile;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "PullHeaderPara [reset=" + reset + ", titile=" + titile + ", count=" + count + "]";
	}

}
