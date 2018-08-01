package cn.wzz.server;

import javax.jws.WebService;

import cn.wzz.bean.T_MALL_USER_ACCOUNT;

@WebService
public interface LoginServer {
	//数据源1
	public String login(T_MALL_USER_ACCOUNT user);

	//数据源2
	public String login2(T_MALL_USER_ACCOUNT user);
}
