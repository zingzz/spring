/**  
 * @Project: MySSM
 * @Title: UserController.java
 * @Package com.myssm.yuan.controller
 * @author yuan
 * @date 2016��6��23�� ����3:45:53
 * @Copyright: 2016
 * @version V1.0  
*/

package com.ssm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.ssm.entity.UUser;
import com.ssm.entity.User;
import com.ssm.service.IUserService;
import com.ssm.user.bo.UserOnlineBo;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;
	
	@RequestMapping("/getUserList")
	public String getUserList(Model model){
		List<User> list = this.userService.getUserList();
		model.addAttribute("ulist",list);
		return "page/user";
	}
	@RequestMapping("/tojson.json")
	public JSONObject testJson(HttpServletRequest request, HttpServletResponse response,UserOnlineBo bo,UUser user,String zz) {
		JSONObject result = new JSONObject();
		result.put("user", "aa");
		result.put("pwd", "bB");
		user.setEmail("999");
		UUser jj= new UUser();
		result.put("jj", jj);
		return result;
	}
	
}
