/**  
 * @Project: MySSM
 * @Title: IUserService.java
 * @Package com.myssm.yuan.service
 * @author yuan
 * @Copyright: 2016
 * @version V1.0  
*/

package com.ssm.service;

import java.util.List;

import com.ssm.entity.User;

public interface IUserService {

	List<User> getUserList();
	User getUserByAccount(String account);
}
