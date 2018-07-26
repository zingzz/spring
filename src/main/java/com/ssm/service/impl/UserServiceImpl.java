/**  
 * @Project: MySSM
 * @Title: UserServiceImpl.java
 * @Package com.myssm.yuan.service.impl
 * @author yuan
 * @date 2016��6��23�� ����3:46:48
 * @Copyright: 2016
 * @version V1.0  
*/

package com.ssm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.dao.UserMapper;
import com.ssm.entity.User;
import com.ssm.service.IUserService;


@Service
public class UserServiceImpl implements IUserService{

	@Autowired
	private UserMapper userDao;
	
	@Override
	public List<User> getUserList() {
		return this.userDao.getUserList();
	}
	@Override
	public User getUserByAccount(String account) {
		return this.userDao.getUserByAccount(account);
	}

}
