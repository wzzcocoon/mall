package cn.wzz.server;

import java.util.List;

import javax.jws.WebService;

import cn.wzz.bean.T_MALL_ADDRESS;
import cn.wzz.bean.T_MALL_USER_ACCOUNT;

@WebService
public interface AddressServer {

	List<T_MALL_ADDRESS> get_addresses(T_MALL_USER_ACCOUNT user);

	T_MALL_ADDRESS get_address(int address_id);

}
