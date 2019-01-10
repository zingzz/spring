package com.ssm.permission.controller;

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
import com.ssm.entity.UPermission;
import com.ssm.permission.service.PermissionService;
/**
 * 
 * 用户权限管理
 */
@Controller
@Scope(value="prototype")
@RequestMapping("/permission")
public class PermissionController extends BaseController {
	
	@Autowired
	PermissionService permissionService;
	/**
	 * 权限列表
	 * @param findContent	查询内容
	 * @param pageNo		页码
	 * @param modelMap		参数回显
	 * @return
	 */
	@RequestMapping(value="/index.json")
	public JSONObject index(String findContent,ModelMap modelMap,Integer pageNo){
		JSONObject result=new JSONObject();
		modelMap.put("findContent", findContent);
		Pagination<UPermission> permissions = permissionService.findPage(modelMap,pageNo,pageSize);
		result.put("pageList", permissions);
		return result;
	}
	/**
	 * 权限添加
	 * @param role
	 * @return
	 */
	@RequestMapping(value="/addPermission.json",method=RequestMethod.POST)
	public JSONObject addPermission(UPermission psermission){
		JSONObject result= new JSONObject();
		try {
			UPermission entity = permissionService.insertSelective(psermission);
			result.put("status", 200);
			result.put("entity", entity);
		} catch (Exception e) {
			result.put("status", 500);
			result.put("message", "添加失败，请刷新后再试！");
			LoggerUtils.fmtError(getClass(), e, "添加权限报错。source[%s]", psermission.toString());
		}
		return result;
	}
	/**
	 * 删除权限，根据ID，但是删除权限的时候，需要查询是否有赋予给角色，如果有角色在使用，那么就不能删除。
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deletePermissionById",method=RequestMethod.POST)
	public Map<String,Object> deleteRoleById(String ids){
		return permissionService.deletePermissionById(ids);
	}
}
