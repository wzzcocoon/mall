package cn.wzz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wzz.bean.T_MALL_PRODUCT;
import cn.wzz.bean.T_MALL_SKU;
import cn.wzz.bean.T_MALL_SKU_ATTR_VALUE;
import cn.wzz.mapper.SkuMapper;
import cn.wzz.service.SkuService;

@Service
public class SkuServiceImpl implements SkuService {

	@Autowired
	private SkuMapper skuMapper;

	@Override
	public void save_sku(T_MALL_SKU sku, T_MALL_PRODUCT spu, List<T_MALL_SKU_ATTR_VALUE> list_attr) {
		//保存sku信息，返回主键
		sku.setShp_id(spu.getId());
		skuMapper.insert_sku(sku);
		//根据sku主键，批量属性关联表
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("sku_id", sku.getId());
		map.put("shp_id", spu.getId());
		map.put("list_av",list_attr);
		skuMapper.insert_sku_av(map);
	}
	

}
