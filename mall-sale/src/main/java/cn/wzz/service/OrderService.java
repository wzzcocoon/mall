package cn.wzz.service;

import org.springframework.web.bind.annotation.ModelAttribute;

import cn.wzz.bean.OBJECT_T_MALL_ORDER;
import cn.wzz.bean.T_MALL_ADDRESS;
import cn.wzz.exception.OverSaleException;

public interface OrderService {
	
	public void save_order(@ModelAttribute("order")OBJECT_T_MALL_ORDER order,T_MALL_ADDRESS address);

	public void paySuccess (OBJECT_T_MALL_ORDER order) throws OverSaleException;
	
}
