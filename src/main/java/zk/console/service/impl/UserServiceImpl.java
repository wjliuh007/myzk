package zk.console.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zk.console.dao.UserMapper;
import zk.console.model.User;
import zk.console.service.UserService;
import zk.console.utils.PropertiesUtil;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userDao;

	@Override
	public User getUserById(int id) {
		// TODO Auto-generated method stub
		System.out.println(id + userDao.selectId(id).getUsername());

		return userDao.selectId(id);
	}

	@Override
	public Boolean getLoginUser(User userLogin) {
		if ("".equals(userLogin.getUsername()) || ("".equals(userLogin.getPassword()))) {
			return false;
		} else {

			User user = userDao.selectLogin(userLogin.getUsername());
			if (user == null) {
				return false;
			}
			if (userLogin.getPassword().equals(user.getPassword())) {
				return true;
			}
			return false;
		}
	}

	@Override
	public User user(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectLogin(String username) {
		// TODO Auto-generated method stub
		return userDao.selectLogin(username);
	}

	@Override
	public User selectByPrimaryKey(int i) {
		// TODO Auto-generated method stub
		return userDao.selectByPrimaryKey(i);
	}

	@Override
	public User getUserByusername(String username) {
		// TODO Auto-generated method stub
		return userDao.selectLogin(username);
	}

	@Override
	public int inster(User user) {
		// TODO Auto-generated method stub
		int i = userDao.insert(user);
		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.liepin.conf.manager.service.UserService#checkUser(com.liepin.conf
	 * .manager.pojo.User)
	 */
	@Override
	public Boolean checkUser(Object user) {
		if (user == null) {
			return Boolean.FALSE;
		}
		User u = (User) user;
		String passwd = PropertiesUtil.getProperty(u.getUsername(), "/user.properties");
		if ((passwd != null) && passwd.equals(u.getPassword())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
