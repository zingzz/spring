package com.ssm.permission.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ssm.common.controller.BaseController;
import com.ssm.common.page.Pagination;
import com.ssm.permission.bo.URoleBo;
import com.ssm.permission.bo.UserRoleAllocationBo;
import com.ssm.permission.service.PermissionService;
import com.ssm.user.service.UUserService;
/**
 * 
 * 用户角色分配
 * 
 */
@Controller
@Scope(value="prototype")
@RequestMapping("/role")
public class UserRoleAllocationController extends BaseController {
	@Autowired
	UUserService userService;
	@Autowired
	PermissionService permissionService;
	/**
	 * 用户角色权限分配
	 * @param modelMap
	 * @param pageNo
	 * @param findContent
	 * @return
	 */
	@RequestMapping(value="/allocation.json")
	public JSONObject allocation(ModelMap modelMap,Integer pageNo,String findContent){
		JSONObject result= new JSONObject();
		modelMap.put("findContent", findContent);
		Pagination<UserRoleAllocationBo> boPage = userService.findUserAndRole(modelMap,pageNo,pageSize);
		result.put("page", boPage);
		return result;
	}
	
	/**
	 * 根据用户ID查询权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/selectRoleByUserId.json")
	public JSONObject selectRoleByUserId(Long id){
		JSONObject result= new JSONObject();
		List<URoleBo> bos = userService.selectRoleByUserId(id);
		result.put("URoleBo", bos);
		return result;
	}
	/**
	 * 操作用户的角色
	 * @param userId 	用户ID
	 * @param ids		角色ID，以‘,’间隔
	 * @return
	 */
	@RequestMapping(value="addRole2User")
	@ResponseBody
	public Map<String,Object> addRole2User(Long userId,String ids){
		return userService.addRole2User(userId,ids);
	}
	/**
	 * 根据用户id清空角色。
	 * @param userIds	用户ID ，以‘,’间隔
	 * @return
	 */
	@RequestMapping(value="clearRoleByUserIds")
	@ResponseBody
	public Map<String,Object> clearRoleByUserIds(String userIds){
		return userService.deleteRoleByUserIds(userIds);
	}
}
