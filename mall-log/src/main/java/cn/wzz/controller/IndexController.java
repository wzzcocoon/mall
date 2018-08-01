package cn.wzz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wzz.service.LogService;

@Controller
public class IndexController {

	@Autowired
	private LogService logService;
	
	@RequestMapping("index")
	public String index(String string) {
		logService.log(string);
		return "index";
	}
} 
