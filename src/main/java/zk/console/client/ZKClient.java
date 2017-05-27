package zk.console.client;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.PathUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import zk.console.utils.PropertiesUtil;

public class ZKClient {
	private String zkhost;
	private static final Logger logger = Logger.getLogger(ZKClient.class);

	/**
	 * CuratorFramework client
	 */
	private final CuratorFramework client;
	/**
	 * ZKClient单例
	 */
	private static final ZKClient zkclient = new ZKClient();

	/**
	 * 获取zkclient实例（单例）
	 * 
	 * @return
	 */
	public static ZKClient getInstance() {
		return zkclient;
	}

	private ZKClient() {
		String connectString = PropertiesUtil.getProperty("zkhost", "/zk.properties");
		zkhost = connectString;
		client = CuratorFrameworkFactory.builder().connectString(connectString).retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).connectionTimeoutMs(5000)
				.build();
		client.start();
		// 等待与zk服务器连接建立完成后才返回实例
		waitUntilZkStart(connectString);
	}

	private void waitUntilZkStart(String zkConnectString) {
		CountDownLatch latch = new CountDownLatch(1);
		client.getConnectionStateListenable().addListener(new ConnectionWatcher(latch));
		try {
			boolean connectedResult = latch.await(8, TimeUnit.SECONDS);
			// 超过8秒，抛出例外
			if (!connectedResult) {
				logger.error("start zk latch.await() overtime. zkConnectString=" + zkConnectString);
				throw new RuntimeException("ZooKeeper client connecting overtime...");
			}
		} catch (InterruptedException e) {
			logger.error("start zk latch.await() error. zkConnectString=" + zkConnectString, e);
			Thread.currentThread().interrupt();
		}
	}

	private class ConnectionWatcher implements ConnectionStateListener {
		CountDownLatch latch;

		ConnectionWatcher(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void stateChanged(CuratorFramework client, ConnectionState newState) {
			if (newState == ConnectionState.CONNECTED) {
				latch.countDown();
				// zkclient实例第一次生成时使用
				client.getConnectionStateListenable().removeListener(this);
			}
		}
	}

	private String validatePath(String path) {
		// 去掉最后的'/'
		if ((path != null) && !"".equals(path.trim()) && !"/".equals(path) && path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		return PathUtils.validatePath(path);
	}

	/**
	 * 获取指定路径的所有子节点名（只获取子节点的第一层） 如果path下没有子节点，返回空list
	 * 
	 * @param path
	 *            路径
	 * @return
	 */
	public List<String> getSubNodes(String path) {
		path = validatePath(path);
		checkConnect();
		List<String> result = Collections.emptyList();
		try {
			result = client.getChildren().forPath(path);
		} catch (KeeperException.NoNodeException e) {
			logger.info("getSubNodes() failure. parent node don not have sub nodes. path=" + path);
		} catch (Exception e) {
			logger.error("getSubNodes() failure. path=" + path, e);
		}
		return result;
	}

	public boolean create(String path, String data, CreateMode mode) {
		path = validatePath(path);
		checkConnect();
		boolean result = false;
		try {
			if (data == null) {
				client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, "".getBytes("utf-8"));
			} else {
				client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data.getBytes("utf-8"));
			}
			result = true;
		} catch (KeeperException.NodeExistsException e) {
			logger.warn("create node failure, node also exists. path=" + path, e);
		} catch (KeeperException.NoChildrenForEphemeralsException e) {
			logger.warn("create node failure, parent node can not be temp node. path=" + path, e);
		} catch (Exception e) {
			logger.error("create node failure. path=" + path, e);
		}
		return result;
	}

	public Stat updateWithVersion(String path, String data, int version) {
		path = validatePath(path);
		checkConnect();
		Stat result = null;
		try {
			if (data == null) {
				result = client.setData().withVersion(version).forPath(path, "".getBytes("utf-8"));
			} else {
				result = client.setData().withVersion(version).forPath(path, data.getBytes("utf-8"));
			}
		} catch (KeeperException.NoChildrenForEphemeralsException e) {
			logger.warn("update node failure, parent node can not be temp node. path=" + path, e);
		} catch (KeeperException.BadVersionException e) {
			logger.warn("update node failure, current version is old, can not update. path=" + path + ",version=" + version, e);
		} catch (Exception e) {
			logger.error("update node failure. path=" + path, e);
		}
		return result;
	}

	public Stat update(String path, String data) {
		path = validatePath(path);
		checkConnect();
		Stat result = null;
		try {
			if (data == null) {
				result = client.setData().forPath(path, "".getBytes("utf-8"));
			} else {
				result = client.setData().forPath(path, data.getBytes("utf-8"));
			}
		} catch (KeeperException.NoChildrenForEphemeralsException e) {
			logger.warn("update node failure, parent node can not be temp node. path=" + path, e);
		} catch (Exception e) {
			logger.error("update node failure. path=" + path, e);
		}
		return result;
	}

	/**
	 * 删除zk节点
	 * 
	 * @param path
	 *            删除节点的路径
	 * 
	 * **/
	public boolean delete(String path) {
		path = validatePath(path);
		checkConnect();
		boolean result = true;
		try {
			client.delete().guaranteed().forPath(path);
		} catch (Exception e) {
			result = false;
			logger.error("delete node failure. path=" + path, e);
		}

		return result;
	}

	/**
	 * 获取节点数据内容。<br>
	 * 如果节点不存在，返回null。
	 * 
	 * @param namespace
	 *            命名空间
	 * @param _clientId
	 *            工程ID
	 * @param path
	 *            相对路径
	 * @param clazz
	 *            返回结果类型
	 */
	public String getData(String path) {
		path = validatePath(path);
		checkConnect();
		try {
			if (exist(path)) {
				byte[] result = client.getData().forPath(path);
				if (null != result) {
					return new String(result, "utf-8");
				} else {
					return "";
				}

			}
		} catch (KeeperException.NoNodeException e) {
			logger.warn("No Node exists. path=" + path, e);
			return null;
		} catch (Exception e) {
			logger.error("getData() error. path=" + path, e);
			return null;
		}
		return null;
	}

	public boolean isConnected() {
		return client.getZookeeperClient().isConnected();
	}

	/**
	 * 类销毁之前调用
	 */
	@PreDestroy
	private void close() {
		logger.info("Call close of ZKClient");
		client.close();
		logger.info("ZKClient is closed");
	}

	public boolean exist(String path) {
		path = validatePath(path);
		checkConnect();
		try {
			Stat stat = client.checkExists().forPath(path);
			return stat != null;
		} catch (Exception e) {
			logger.warn("check exist error. path=" + path, e);
		}
		return false;
	}

	public Stat getStat(String path) {
		path = validatePath(path);
		checkConnect();
		try {
			return client.checkExists().forPath(path);
		} catch (Exception e) {
			logger.error("check exist error. path=" + path, e);
		}
		return null;
	}

	/**
	 * 检查连接状态，如果连接不可用，则抛出异常
	 */
	private void checkConnect() {
		int times = 5;
		boolean status = isConnected();
		// 每隔1秒尝试一次，5次都失败的话，抛出异常
		while (!status && (times > 0)) {
			times--;
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// do nothing
			}
			status = isConnected();
		}
		if (!status && (times <= 0)) {
			logger.warn("ZooKeeper client connect invalid.");
			throw new RuntimeException("ZooKeeper client connect invalid.");
		}
	}

	public String getZkhost() {
		return zkhost;
	}

	public void setZkhost(String zkhost) {
		this.zkhost = zkhost;
	}
}
