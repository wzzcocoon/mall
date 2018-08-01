package cn.wzz.service;

import cn.wzz.bean.T_MALL_USER_ACCOUNT;

public interface LoginService {
	
	//数据源1
	public T_MALL_USER_ACCOUNT login(T_MALL_USER_ACCOUNT user); 

	//数据源2
	public T_MALL_USER_ACCOUNT login2(T_MALL_USER_ACCOUNT user); 
}
