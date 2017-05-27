package zk.console.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;

import zk.console.client.GsonUtil;

public class TreeNodeUtil {

	public static final String PROJECT_NAME = "myzk";

	public static List<Object> getTreeNodeList(String parentpath, List<String> Nodes) {
		if ((Nodes == null) || Nodes.isEmpty()) {
			return Collections.emptyList();
		}
		StringBuilder sb = new StringBuilder(200 * Nodes.size());
		sb.append("[");
		for (String node : Nodes) {
			sb.append("{\"state\":\"closed\",\"attributes\":{\"rel\":\"chv\",\"path\":\"");
			if ((parentpath != null) && !"/".equals(parentpath)) {
				sb.append(parentpath);
			}
			sb.append("/").append(node).append("\"},");
			sb.append("\"data\":{\"title\":\"").append(node).append("\",\"icon\":\"ou.png\",\"attributes\":{\"href\":\"");
			sb.append("/").append(PROJECT_NAME).append("/zk/getNode.do?path=");
			if ((parentpath != null) && !"/".equals(parentpath)) {
				sb.append(parentpath);
			}
			sb.append("/").append(node).append("\"}}},");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		@SuppressWarnings("unchecked")
		List<Object> fromJson = GsonUtil.createGson().fromJson(sb.toString(), List.class);
		return fromJson;
	}

	public static void main(String[] args) {
		List<String> nodes = new ArrayList<String>();
		nodes.add("test2");
		nodes.add("config");
		nodes.add("test");
		nodes.add("zookeeper");

		System.out.println(JSON.toJSON(getTreeNodeList("/", nodes)));
	}
}
