package cn.wzz.mapper;

import java.util.List;
import java.util.Map;

import cn.wzz.bean.OBJECT_T_MALL_DETAIL_SKU;
import cn.wzz.bean.OBJECT_T_MALL_SKU;
import cn.wzz.bean.T_MALL_SKU;

public interface ListMapper {

	List<OBJECT_T_MALL_SKU> select_list_by_flbh2(int flbh2);

	List<OBJECT_T_MALL_SKU> select_list_by_attr(Map<Object, Object> map);

	OBJECT_T_MALL_DETAIL_SKU select_sku_detail(Map<Object, Object> map);

	List<T_MALL_SKU> select_list_sku_by_spu_id(int spu_id);

}
