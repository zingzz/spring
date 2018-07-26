
package com.ssm.service;

import java.util.List;

import com.ssm.entity.User;

public interface IUserService {

	List<User> getUserList();
	User getUserByAccount(String account);
}
