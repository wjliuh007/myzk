/**
 * className:TestZK.java<br/>
 * @since 2017年3月9日 下午2:54:11
 * @aouthor eagle
 */
package test.zk;

import java.util.Arrays;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author Administrator
 *
 */
public class TestZK {

	static// 创建一个与服务器的连接
	ZooKeeper zk;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			zk = new ZooKeeper("192.168.226.128:2183", 5000, new Watcher() {
				// 监控所有被触发的事件
				@Override
				public void process(WatchedEvent event) {
					System.out.println("已经触发了" + event.getType() + "事件！");
				}
			});
			// 创建一个目录节点
			zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			// 创建一个子目录节点
			zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
			System.out.println(new String(zk.getData("/testRootPath", false, null)));
			// 取出子目录节点列表
			System.out.println(zk.getChildren("/testRootPath", true));
			// 修改子目录节点数据
			zk.setData("/testRootPath/testChildPathOne", "modifyChildDataOne".getBytes(), -1);
			System.out.println("目录节点状态：[" + zk.exists("/testRootPath", true) + "]");
			// 创建另外一个子目录节点
			zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
			System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo", true, null)));
			// 删除子目录节点
			zk.delete("/testRootPath/testChildPathTwo", -1);
			zk.delete("/testRootPath/testChildPathOne", -1);
			// 删除父目录节点
			zk.delete("/testRootPath", -1);
			// 关闭连接
			zk.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void getLock() throws KeeperException, InterruptedException {
		List<String> list = zk.getChildren("/root", false);
		String[] nodes = list.toArray(new String[list.size()]);
		Arrays.sort(nodes);
		if ("the".equals("/root/" + nodes[0])) {
			// doAction();
		} else {
			waitForLock(nodes[0]);
		}
	}

	void waitForLock(String lower) throws InterruptedException, KeeperException {
		Stat stat = zk.exists("/root/" + lower, true);
		if (stat != null) {
			// mutex.wait();
		} else {
			getLock();
		}
	}

}
