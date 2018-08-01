package cn.wzz.service;

import java.util.List;

import cn.wzz.bean.T_MALL_SHOPPINGCAR;
import cn.wzz.bean.T_MALL_USER_ACCOUNT;

public interface CartService {

	boolean if_cart_exists(T_MALL_SHOPPINGCAR cart);

	void add_cart(T_MALL_SHOPPINGCAR cart);

	void update_cart(T_MALL_SHOPPINGCAR t_MALL_SHOPPINGCAR);

	List<T_MALL_SHOPPINGCAR> get_list_cart_by_user(T_MALL_USER_ACCOUNT select_user);


}
