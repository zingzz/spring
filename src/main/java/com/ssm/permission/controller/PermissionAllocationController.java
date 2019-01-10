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
import com.ssm.permission.bo.RolePermissionAllocationBo;
import com.ssm.permission.bo.UPermissionBo;
import com.ssm.permission.service.PermissionService;
import com.ssm.permission.service.RoleService;
/**
 * 用户权限分配
 */
@Controller
@Scope(value="prototype")
@RequestMapping("/permission")
public class PermissionAllocationController extends BaseController {
	
	@Autowired
	PermissionService permissionService;
	@Autowired
	RoleService roleService;
	/**
	 * 权限分配
	 * @param modelMap
	 * @param pageNo
	 * @param findContent
	 * @return
	 */
	@RequestMapping(value="/allocation.json")
	public JSONObject allocation(ModelMap modelMap,Integer pageNo,String findContent){
		JSONObject result = new JSONObject();
		modelMap.put("findContent", findContent);
		Pagination<RolePermissionAllocationBo> boPage = roleService.findRoleAndPermissionPage(modelMap,pageNo,pageSize);
		result.put("page", boPage);
		return result;
	}
	
	/**
	 * 根据角色ID查询权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/selectPermissionById.json")
	public JSONObject selectPermissionById(Long id){
		JSONObject result= new JSONObject();
		List<UPermissionBo> permissionBos = permissionService.selectPermissionById(id);
		result.put("data", permissionBos);
		return result;
	}
	/**
	 * 操作角色的权限
	 * @param roleId 	角色ID
	 * @param ids		权限ID，以‘,’间隔
	 * @return
	 */
	@RequestMapping(value="/addPermission2Role")
	@ResponseBody
	public Map<String,Object> addPermission2Role(Long roleId,String ids){
		return permissionService.addPermission2Role(roleId,ids);
	}
	/**
	 * 根据角色id清空权限。
	 * @param roleIds	角色ID ，以‘,’间隔
	 * @return
	 */
	@RequestMapping(value="clearPermissionByRoleIds")
	@ResponseBody
	public Map<String,Object> clearPermissionByRoleIds(String roleIds){
		return permissionService.deleteByRids(roleIds);
	}
}
