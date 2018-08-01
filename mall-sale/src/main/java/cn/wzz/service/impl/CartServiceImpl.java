package cn.wzz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wzz.bean.T_MALL_SHOPPINGCAR;
import cn.wzz.bean.T_MALL_USER_ACCOUNT;
import cn.wzz.mapper.CartMapper;
import cn.wzz.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartMapper cartMapper;

	@Override
	public boolean if_cart_exists(T_MALL_SHOPPINGCAR cart) {
		boolean b = false;
		int count = cartMapper.select_cart_exists(cart);
		if(count > 0) {
			b = true;
		}
		return b;
	}

	@Override
	public void add_cart(T_MALL_SHOPPINGCAR cart) {
		cartMapper.insert_cart(cart);
	}

	@Override
	public void update_cart(T_MALL_SHOPPINGCAR cart) {
		cartMapper.update_cart(cart);
	}

	@Override
	public List<T_MALL_SHOPPINGCAR> get_list_cart_by_user(T_MALL_USER_ACCOUNT select_user) {
		return cartMapper.select_list_cart_by_user(select_user);
	}
}
