package cn.wzz.mapper;


import java.util.List;

import cn.wzz.bean.T_MALL_ADDRESS;
import cn.wzz.bean.T_MALL_USER_ACCOUNT;

public interface AddressMapper {

	List<T_MALL_ADDRESS> select_addresses(T_MALL_USER_ACCOUNT user);

	T_MALL_ADDRESS select_address(int address_id);

}
