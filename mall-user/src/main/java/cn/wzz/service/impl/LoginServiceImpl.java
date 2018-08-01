package cn.wzz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wzz.bean.T_MALL_USER_ACCOUNT;
import cn.wzz.mapper.LoginMapper;
import cn.wzz.service.LoginService;
import cn.wzz.util.MyRoutingDataSource;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private LoginMapper loginMapper;
	
	@Override
	public T_MALL_USER_ACCOUNT login(T_MALL_USER_ACCOUNT user) {
		MyRoutingDataSource.setKey("1");
		return loginMapper.select_user(user);
	}

	@Override
	public T_MALL_USER_ACCOUNT login2(T_MALL_USER_ACCOUNT user) {
		MyRoutingDataSource.setKey("2");
		return loginMapper.select_user(user);
	}

}
