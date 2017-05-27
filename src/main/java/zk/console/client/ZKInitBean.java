/**
 * className:ZKInitBean.java<br/>
 * @since 2017年3月9日 下午1:58:37
 * @aouthor eagle
 */
package zk.console.client;

/**
 * @author Administrator
 *
 */
public class ZKInitBean {

	public void init() {

	}

	private ZKClient zkClient;

	public ZKClient getZkClient() {
		return zkClient;
	}

	public void setZkClient(ZKClient zkClient) {
		this.zkClient = zkClient;
	}

}
