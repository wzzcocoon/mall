package cn.wzz.server.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.wzz.bean.T_MALL_ADDRESS;
import cn.wzz.bean.T_MALL_USER_ACCOUNT;
import cn.wzz.mapper.AddressMapper;
import cn.wzz.server.AddressServer;

public class AddressServerImpl implements AddressServer {

	@Autowired
	AddressMapper addressMapper;

	@Override
	public List<T_MALL_ADDRESS> get_addresses(T_MALL_USER_ACCOUNT user) {
		List<T_MALL_ADDRESS> list_address = addressMapper.select_addresses(user);
		return list_address;
	}

	@Override
	public T_MALL_ADDRESS get_address(int address_id) {
		T_MALL_ADDRESS address = addressMapper.select_address(address_id);
		return address;
	}

}
