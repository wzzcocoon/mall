package cn.wzz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wzz.bean.T_MALL_USER_ACCOUNT;

@Controller
public class LoginController {

	@ResponseBody
	//解决中文乱码
	@RequestMapping(value="login",produces="text/html; charset=UTF-8")
	public String login(T_MALL_USER_ACCOUNT users) {
		return "success";
	}
}
