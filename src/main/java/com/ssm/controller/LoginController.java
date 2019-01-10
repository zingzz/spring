package com.ssm.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {


	@RequestMapping("/login")
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, String username,
			String password) {
		ModelAndView mv = new ModelAndView();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		// 记录
		token.setRememberMe(false);
		// subject权限对象
		Subject subject = SecurityUtils.getSubject();
		System.out.println(subject.isAuthenticated());
		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		// 判断是否已登
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		try {
			// 提交申请，验证能不能通过，也回调reaml里的doGetAuthenticationInfo验证是否通过
			subject.login(token);
			System.out.println(exceptionClassName);
		} catch (UnknownAccountException ex) {// 用户名没有找�?
			mv.addObject("msg", "用户未找�?");
			// ex.printStackTrace();
		} catch (IncorrectCredentialsException ex) {// 用户名密码不匹配
			mv.addObject("msg", "密码不正�?");
			// map.put("msg", "密码不正�?");
			// ex.printStackTrace();
		} catch (AuthenticationException e) {// 其他的登录错�?
			mv.addObject("msg", "其他错误");
			// e.printStackTrace();
		} catch (Exception e) {
			mv.addObject("msg", "登录异常");
			// e.printStackTrace();
		}

		// 验证是否成功登录的方�?
		if (subject.isAuthenticated()) {
			return new ModelAndView("redirect:/index.jsp");
		} else {
			// mv.setViewName("redirect:/login.jsp");
			mv.setViewName("login"); // 此处偷懒，一般是ajax请求，或重定向时将失败传�?
		}
		// return new ModelAndView("redirect:/login.jsp");
		return mv;
	}
	// �?�?
	@RequestMapping("/logout")
	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
	}
}
