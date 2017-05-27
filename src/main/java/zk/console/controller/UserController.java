package zk.console.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import zk.console.model.User;
import zk.console.service.UserService;
import zk.console.utils.PropertiesUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource(name = "userServiceImpl")
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String gotoLogin(HttpServletRequest request, Model model) {
		return "login";
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		User userLogin = new User();
		userLogin.setUsername(request.getParameter("username"));
		userLogin.setPassword(request.getParameter("password"));

		String passwd = PropertiesUtil.getProperty(userLogin.getUsername(), "/user.properties");

		if ((passwd != null) && passwd.equals(userLogin.getPassword())) {

			model.addAttribute("user", userLogin.getUsername());
			request.getSession().setAttribute("user", userLogin);
			request.getSession().setMaxInactiveInterval(120);
		} else {
			model.addAttribute("msg", "用户名或秘密错误，请重试");
			return "login";
		}
		return "zkindex";
	}

	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	public String registerIndex(HttpServletRequest request, Model model) {

		// ModelAndView mav=new ModelAndView();

		User userRegister = new User();
		String username2 = request.getParameter("username");
		String password2 = request.getParameter("password");
		userRegister.setUsername(username2);
		userRegister.setPassword(password2);
		System.out.println(username2 + "密码是" + password2);

		if (userRegister.getUsername() != null) {
			if (request.getParameter("age") == null) {
				userRegister.setAge(25);
			} else {
				userRegister.setAge(Integer.parseInt(request.getParameter("age")));
			}
			System.out.println(userRegister.getUsername() + " 显示注册的名字");

		} else {

			User user2 = new User();
			user2.setUsername("pm");
			user2.setPassword("pm");
			user2.setAge(45);
			userRegister = user2;
		}

		int id = userService.inster(userRegister);

		User user = userService.getUserById(id);

		System.out.println(user.toString() + "insert user success");

		model.addAttribute("user", user);

		return "indexuser";
	}

	@RequestMapping(value = "/logout.do", method = RequestMethod.POST)
	public String logout(HttpServletRequest request, Model model) {
		Object user = request.getSession().getAttribute("user");
		if (!userService.checkUser(user)) {
			return "login";
		}
		request.getSession().setAttribute("user", null);

		return "login";
	}

}
