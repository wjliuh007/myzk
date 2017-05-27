package zk.console.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zk.console.client.TreeNodeUtil;
import zk.console.client.ZKClient;
import zk.console.model.User;
import zk.console.service.UserService;

@Controller
@RequestMapping("/zk")
public class ZkController {
	@Resource(name = "userServiceImpl")
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/tree.do", method = RequestMethod.GET)
	public String tree(HttpServletRequest request, Model model) {
		Object user = request.getSession().getAttribute("user");
		if (!userService.checkUser(user)) {
			return "login";
		}
		String path = request.getParameter("path");
		if ((path == null) || "".endsWith(path)) {
			path = "/";
		}
		model.addAttribute("path", path);
		model.addAttribute("host", ZKClient.getInstance().getZkhost());
		return "tree";
	}

	@RequestMapping(value = "/children.do", method = RequestMethod.GET)
	public @ResponseBody List<Object> children(HttpServletRequest request, Model model) {
		Object user = request.getSession().getAttribute("user");
		if (!userService.checkUser(user)) {
			return null;
		}
		String path = request.getParameter("path");
		if ((path == null) || "".endsWith(path)) {
			path = "/";
		}
		List<String> nodeList = ZKClient.getInstance().getSubNodes(path);
		if ("/".equals(path)) {
			nodeList.remove("zookeeper");
		}

		List<Object> result = TreeNodeUtil.getTreeNodeList(path, nodeList);

		return result;

	}

	@RequestMapping(value = "/getNode.do", method = RequestMethod.GET)
	public String getNode(HttpServletRequest request, Model model) {
		Object user = request.getSession().getAttribute("user");
		if (!userService.checkUser(user)) {
			return "login";
		}
		String path = request.getParameter("path");
		if ((path == null) || "".endsWith(path)) {
			path = "/";
		}
		String data = ZKClient.getInstance().getData(path);
		Stat stat = ZKClient.getInstance().getStat(path);

		model.addAttribute("path", path);
		model.addAttribute("data", data);
		model.addAttribute("stat", stat);
		model.addAttribute("user", ((User) user).getUsername());
		return "data";
	}

	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	public @ResponseBody Object editNode(HttpServletRequest request) {
		Object user = request.getSession().getAttribute("user");
		if (!userService.checkUser(user)) {
			return "login";
		}
		String path = request.getParameter("path");
		if ((path == null) || "".endsWith(path)) {
			path = "/";
		}
		String data = request.getParameter("new_data");
		String version = request.getParameter("version");
		int versionInt = 0;
		if (!StringUtils.isEmpty(version)) {
			versionInt = Integer.parseInt(version);
		}

		Stat stat = ZKClient.getInstance().updateWithVersion(path, data, versionInt);

		// model.addAttribute("path", path);
		// model.addAttribute("data", data);
		// model.addAttribute("stat", stat);
		// model.addAttribute("user", ((User) user).getUsername());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", "修改成功！");
		map.put("status", stat);
		return map;
	}

	@RequestMapping(value = "/gotoadd.do", method = RequestMethod.GET)
	public String gotoAdd(HttpServletRequest request, Model model) {
		Object user = request.getSession().getAttribute("user");
		if (!userService.checkUser(user)) {
			return "login";
		}
		String path = request.getParameter("path");
		if ((path == null) || "".endsWith(path)) {
			path = "/";
		}

		model.addAttribute("path", path);
		model.addAttribute("user", ((User) user).getUsername());
		return "create";
	}

	@RequestMapping(value = "/create.do", method = RequestMethod.POST)
	public @ResponseBody String createNode(HttpServletRequest request, Model model) {
		Object user = request.getSession().getAttribute("user");
		if (!userService.checkUser(user)) {
			return "login";
		}
		String path = request.getParameter("path");
		if ((path == null) || "".endsWith(path.trim()) || "/".equals(path.trim())) {
			return "Create ng";
		}
		String data = request.getParameter("data");
		int type = Integer.parseInt(request.getParameter("flag"));
		CreateMode mode = null;
		switch (type) {
		case 0:
			mode = CreateMode.PERSISTENT;
			break;
		case 1:
			mode = CreateMode.EPHEMERAL;
			break;
		case 2:
			mode = CreateMode.PERSISTENT_SEQUENTIAL;
			break;
		case 3:
			mode = CreateMode.EPHEMERAL_SEQUENTIAL;
			break;
		default:
			break;
		}

		boolean res = ZKClient.getInstance().create(path, data, mode);
		if (res) {
			return "Create ok";
		}
		return "Create ng";
	}

	@RequestMapping(value = "/delete.do", method = RequestMethod.POST)
	public @ResponseBody String deleteNode(HttpServletRequest request, Model model) {
		Object user = request.getSession().getAttribute("user");
		if (!userService.checkUser(user)) {
			return "login";
		}
		String path = request.getParameter("path");
		if ((path == null) || "".endsWith(path.trim()) || "/".equals(path.trim())) {
			return "Delete ng";
		}

		boolean res = ZKClient.getInstance().delete(path);
		if (res) {
			return "Delete ok";
		}
		return "Delete ng";
	}

}
