package com.ssm.user.controller;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ssm.commom.util.LoggerUtils;
import com.ssm.commom.util.StringUtils;
import com.ssm.commom.util.VerifyCodeUtils;
import com.ssm.common.controller.BaseController;
import com.ssm.core.shiro.token.manager.TokenManager;
import com.ssm.entity.UUser;
import com.ssm.user.bo.UserOnlineBo;
import com.ssm.user.manager.UserManager;
import com.ssm.user.service.UUserService;



/**
 * 
 * 用户登录相关，不需要做登录限制
 * 
 * 
 */
@Controller
@RequestMapping("/u")
public class UserLoginController extends BaseController {

	@Resource
	UUserService userService;
	
	@RequestMapping("/tojson.json")
	public JSONObject testJson(HttpServletRequest request, HttpServletResponse response,String zz) {
		JSONObject result = new JSONObject();
		result.put("user", "aa");
//		result.put("pwd", "bB");
//		user.setEmail("999");
//		UUser jj= new UUser();
//		result.put("jj", jj);
		return result;
	}
	@RequestMapping("/tojson2.html")
	public ModelAndView testJson2(HttpServletRequest request, HttpServletResponse response, UUser us,ModelMap map) {
		ModelAndView mav = new ModelAndView();
		mav.clear();
		System.out.println(request.getParameter("pswd"));
		mav.setViewName("/index");
		return mav;
	}
	
	@RequestMapping("/tojson3.json")
	public ModelMap testJson3(HttpServletRequest request, HttpServletResponse response,UUser us,UserOnlineBo bo,String zz) {
		ModelMap  modelMap =new ModelMap();
		modelMap.addAttribute("AA", "BB");
		return modelMap;
	}
	
	
	/**
	 * 登录跳转
	 * @return
	 */
	@RequestMapping(value="/login.html",method=RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response,ModelAndView mav){
		mav.setViewName("/login");
		return mav;
	}
	/**
	 * 注册跳转
	 * @return
	 */
	@RequestMapping(value="register",method=RequestMethod.GET)
	public ModelAndView register(){
		
		return new ModelAndView("user/register");
	}
	/**
	 * 注册 && 登录
	 * @param vcode		验证码	
	 * @param entity	UUser实体
	 * @return
	 */
	@RequestMapping(value="/subRegister.json",method=RequestMethod.POST)
	public JSONObject subRegister(String vcode,UUser entity){
		JSONObject result = new JSONObject();
		if(StringUtils.isBlank(entity.getEmail()) || StringUtils.isBlank(entity.getPswd())) {
			result.put("message", "帐号、密码 不能为空！");
			return result;
		}
		result.put("status", 400);
		if(!VerifyCodeUtils.verifyCode(vcode)){
			result.put("message", "验证码不正确！");
			return result;
		}
		String email =  entity.getEmail();
		
		UUser user = userService.findUserByEmail(email);
		if(null != user){
			result.put("message", "帐号|Email已经存在！");
			return result;
		}
		Date date = new Date();
		entity.setCreateTime(date);
		entity.setLastLoginTime(date);
		//把密码md5
		entity = UserManager.md5Pswd(entity);
		//设置有效
		entity.setStatus(UUser._1);
		
		entity = userService.insert(entity);
		LoggerUtils.fmtDebug(getClass(), "注册插入完毕！", JSONObject.toJSON(entity).toString());
		entity = TokenManager.login(entity, Boolean.TRUE);
		LoggerUtils.fmtDebug(getClass(), "注册后，登录完毕！", JSONObject.toJSON(entity).toString());
		result.put("message", "注册成功！");
		result.put("status", 200);
		return result;
	}
	/**
	 * 登录提交
	 * @param entity		登录的UUser
	 * @param rememberMe	是否记住
	 * @param request		request，用来取登录之前Url地址，用来登录后跳转到没有登录之前的页面。
	 * @return
	 */
	@RequestMapping(value="/submitLogin",method=RequestMethod.POST)
	public Map<String,Object> submitLogin(UUser entity,Boolean rememberMe,HttpServletRequest request){
		JSONObject result=new JSONObject();
		try {
			entity = TokenManager.login(entity,rememberMe);
			result.put("status", 200);
			result.put("message", "登录成功");
			
			
			/**
			 * shiro 获取登录之前的地址
			 */
			SavedRequest savedRequest = WebUtils.getSavedRequest(request);
			String url = null ;
			if(null != savedRequest){
				url = savedRequest.getRequestUrl();
			}
			/**
			 * 获取上一个请求的方式，在Session不一致的情况下是获取不到的
			 * String url = (String) request.getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE);
			 */
			LoggerUtils.fmtDebug(getClass(), "获取登录之前的URL:[%s]",url);
			//如果登录之前没有地址，那么就跳转到首页。
			if(StringUtils.isBlank(url)){
				url = request.getContextPath() + "/user/index.shtml";
			}
			//跳转地址
			result.put("back_url", url);
		/**
		 * 这里其实可以直接catch Exception，然后抛出 message即可，但是最好还是各种明细catch 好点。。
		 */
		} catch (DisabledAccountException e) {
			result.put("status", 500);
			result.put("message", "帐号已经禁用。");
		} catch (Exception e) {
			result.put("status", 500);
			result.put("message", "帐号或密码错误");
		}
			
		return result;
	}
	
	/**
	 * 退出
	 * @return
	 */
	@RequestMapping(value="/logout.json",method =RequestMethod.GET)
	public JSONObject logout(){
		JSONObject result= new JSONObject();
		try {
			TokenManager.logout();
			result.put("status", 200);
		} catch (Exception e) {
			result.put("status", 500);
			logger.error("errorMessage:" + e.getMessage());
			LoggerUtils.fmtError(getClass(), e, "退出出现错误，%s。", e.getMessage());
		}
		return result;
	}
}
