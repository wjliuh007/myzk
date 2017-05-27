/**
 * className:ZKInitUtilsBean.java<br/>
 * @since 2017年3月9日 下午12:50:06
 * @aouthor eagle
 */
package zk.console.client;

/**
 * @author Administrator
 *
 */
public class ZKUtilsBean {

	private ZKClient zkClient;
	private static ZKUtilsBean bean = new ZKUtilsBean();

	public static ZKUtilsBean getIntance() {
		return bean;
	}

	private ZKUtilsBean() {

	}

	public ZKClient getZkClient() {
		return zkClient;
	}

	public void setZkClient(ZKClient zkClient) {
		this.zkClient = zkClient;
	}

}
