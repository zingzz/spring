package com.ssm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/test")
public class MvcController {
	@RequestMapping("tojson.json")
	public JSONObject testJson(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		result.put("hehe", "1111");
		return result;
	}
	
	@RequestMapping("tojson.html")
	public ModelAndView testHtml(HttpServletRequest request, HttpServletResponse response,ModelAndView mav) {

		mav.addObject("what", "the best");
		mav.setViewName("666666");
		return mav;
	}

}
