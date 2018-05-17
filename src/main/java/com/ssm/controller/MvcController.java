package com.ssm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ssm.dao.UserMapper;

@Controller
@RequestMapping("/test")
public class MvcController {
	@Autowired
	private UserMapper userMapper;
	@RequestMapping("tojson.json")
	public JSONObject testJson(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		result.put("user", userMapper.selectByPrimaryKey(1L));
		return result;
	}
	
	@RequestMapping("tojson.html")
	public ModelAndView testHtml(HttpServletRequest request, HttpServletResponse response,ModelAndView mav) {

		mav.addObject("what", "qqqqq");
		mav.setViewName("666");
		return mav;
	}

}
