package cn.wzz.service;

import java.util.List;

import cn.wzz.bean.T_MALL_PRODUCT;
import cn.wzz.bean.T_MALL_SKU;
import cn.wzz.bean.T_MALL_SKU_ATTR_VALUE;

public interface SkuService {

	void save_sku(T_MALL_SKU sku, T_MALL_PRODUCT spu, List<T_MALL_SKU_ATTR_VALUE> list_attr);

}
