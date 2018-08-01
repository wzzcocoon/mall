package cn.wzz.service;

import java.util.List;

import cn.wzz.bean.OBJECT_T_MALL_DETAIL_SKU;
import cn.wzz.bean.OBJECT_T_MALL_SKU;
import cn.wzz.bean.T_MALL_SKU;
import cn.wzz.bean.T_MALL_SKU_ATTR_VALUE;

public interface ListService {

	/** 商品检索功能	--根据flbh2查找相应的商品*/
	List<OBJECT_T_MALL_SKU> get_list_by_flbh2(int flbh2);

	/** 属性检索功能	--在flbh2下再根据属性查找对应的商品*/
	List<OBJECT_T_MALL_SKU> get_list_by_attr(List<T_MALL_SKU_ATTR_VALUE> list_attr, int flbh2);

	/** 详情检索功能	--根据sku和spu的id查找相应的信息，形成商品的详情*/
	OBJECT_T_MALL_DETAIL_SKU get_sku_detail(int sku_id, int spu_id);

	/** sku集合检索	--根据spu的id查找出其对应的sku集合*/
	List<T_MALL_SKU> get_list_sku_by_spu_id(int spu_id);

}
