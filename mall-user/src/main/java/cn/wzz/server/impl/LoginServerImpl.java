package cn.wzz.server.impl;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import cn.wzz.bean.T_MALL_USER_ACCOUNT;
import cn.wzz.server.LoginServer;
import cn.wzz.service.LoginService;

public class LoginServerImpl implements LoginServer{
	
	@Autowired
	private LoginService loginService;

	/*		//SOAP风格
	@Override
	public String login(T_MALL_USER_ACCOUNT user) {
		T_MALL_USER_ACCOUNT login = loginService.login(user);
		Gson gson = new Gson();
		return gson.toJson(login);
	}
	*/
	
	//RestFul
	@Override
	@Path("login") 
	@GET 
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public String login(@BeanParam T_MALL_USER_ACCOUNT user) {
		T_MALL_USER_ACCOUNT login = loginService.login(user);
		Gson gson = new Gson();
		return gson.toJson(login);
	}

	@Override
	@Path("login2") 
	@GET 
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public String login2(@BeanParam T_MALL_USER_ACCOUNT user) {
		T_MALL_USER_ACCOUNT login = loginService.login2(user);
		Gson gson = new Gson();
		return gson.toJson(login);
	}
}
