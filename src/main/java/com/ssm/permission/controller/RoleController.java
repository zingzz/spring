package com.ssm.permission.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ssm.commom.util.LoggerUtils;
import com.ssm.common.controller.BaseController;
import com.ssm.common.page.Pagination;
import com.ssm.entity.URole;
import com.ssm.permission.service.RoleService;
import com.ssm.user.manager.UserManager;
/**
 * 用户角色管理
 * 
 */
@Controller
@Scope(value="prototype")
@RequestMapping("role")
public class RoleController extends BaseController {
	@Autowired
	RoleService roleService;
	/**
	 * 角色列表
	 * @return
	 */
	@RequestMapping(value="/roleList.json")
	public JSONObject index(String findContent,ModelMap modelMap){
		JSONObject result= new JSONObject();
		modelMap.put("findContent", findContent);
		Pagination<URole> role = roleService.findPage(modelMap,pageNo,pageSize);
		result.put("roleList", role);
		return result;
	}
	/**
	 * 角色添加
	 * @param role
	 * @return
	 */
	@RequestMapping(value="/addRole.json",method=RequestMethod.POST)
	public JSONObject addRole(URole role){
		JSONObject result= new JSONObject();
		try {
			int count = roleService.insertSelective(role);
			result.put("status", 200);
			result.put("successCount", count);
		} catch (Exception e) {
			result.put("status", 500);
			result.put("message", "添加失败，请刷新后再试！");
			LoggerUtils.fmtError(getClass(), e, "添加角色报错。source[%s]",role.toString());
		}
		return result;
	}
	/**
	 * 删除角色，根据ID，但是删除角色的时候，需要查询是否有赋予给用户，如果有用户在使用，那么就不能删除。
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deleteRoleById",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteRoleById(String ids){
		return roleService.deleteRoleById(ids);
	}
	/**
	 * 我的权限页面
	 * @return
	 */
	@RequestMapping(value="/mypermission",method=RequestMethod.GET)
	public ModelAndView mypermission(){
		return new ModelAndView("permission/mypermission");
	}
	/**
	 * 我的权限 bootstrap tree data
	 * @return
	 */
	@RequestMapping(value="/getPermissionTree.json",method=RequestMethod.POST)
	public JSONObject getPermissionTree(){
		JSONObject result = new JSONObject();
		//查询我所有的角色 ---> 权限
		List<URole> roles = roleService.findNowAllPermission();
		//把查询出来的roles 转换成bootstarp 的 tree数据
		List<Map<String, Object>> data = UserManager.toTreeData(roles);
		result.put("data", data);
		return result;
	}
}
