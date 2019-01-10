package com.ssm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ssm.cache.JedisUtil;
import com.ssm.dao.UserMapper;
import com.ssm.entity.UUser;

@Controller
@RequestMapping("/test")
public  class MvcController {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisUtil.Strings jdString;	
	
	@RequestMapping("tojson.json")
	public JSONObject testJson(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			System.out.println("???"+jdString.get("qq"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("user", userMapper.selectByPrimaryKey(1L));
		return result;
	}
	@RequestMapping("/to.json")
	@ResponseBody
	public JSONObject testJson(UUser user) {
		JSONObject result = new JSONObject();
		result.put("user", "aa");
		result.put("pwd", "bB");
		return result;
	}
	@RequestMapping("tojson.html")
	public ModelAndView testHtml(HttpServletRequest request, HttpServletResponse response,ModelAndView mav) {
		jdString.set("qq", "bb");
		System.out.println(jdString.get("qq"));
		mav.addObject("what", "qqqqq");
		mav.setViewName("666");
		return mav;
	}

}
