package cn.wzz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wzz.bean.OBJECT_T_MALL_DETAIL_SKU;
import cn.wzz.bean.OBJECT_T_MALL_SKU;
import cn.wzz.bean.T_MALL_SKU;
import cn.wzz.bean.T_MALL_SKU_ATTR_VALUE;
import cn.wzz.mapper.ListMapper;
import cn.wzz.service.ListService;

@Service
public class ListServiceImpl implements ListService {

	@Autowired
	private ListMapper listMapper;
	
	@Override
	public List<OBJECT_T_MALL_SKU> get_list_by_flbh2(int flbh2) {
		return listMapper.select_list_by_flbh2(flbh2);
	}

	@Override
	public List<OBJECT_T_MALL_SKU> get_list_by_attr(List<T_MALL_SKU_ATTR_VALUE> list_attr, int flbh2) {
		StringBuffer subSql = new StringBuffer();
		//根据属性集合拼接条件过滤的sql
		subSql.append(" and sku.id in ( select sku0.sku_id from ");

		if (list_attr != null && list_attr.size() > 0) {
			for (int i = 0; i < list_attr.size(); i++) {
				subSql.append(
						" (select sku_id from t_mall_sku_attr_value where shxm_id = " + list_attr.get(i).getShxm_id()
								+ " and shxzh_id = " + list_attr.get(i).getShxzh_id() + ") sku" + i + " ");
				if ((i + 1) < list_attr.size() && list_attr.size() > 1) {
					subSql.append(" , ");
				}
			}
			if (list_attr.size() > 1) {
				subSql.append(" where ");

				for (int i = 0; i < list_attr.size(); i++) {
					if ((i + 1) < list_attr.size()) {
						subSql.append(" sku" + i + ".sku_id=" + "sku" + (i + 1) + ".sku_id");
						
						if(list_attr.size()>2&&i  < (list_attr.size()- 2)){
							subSql.append(" and ");
						}
					}
				}
			}
		}

		subSql.append(" ) ");
		
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("flbh2",flbh2);
		//map.put("list_attr",list_attr);
		map.put("subSql", subSql);
		
		return listMapper.select_list_by_attr(map);
	}

	@Override
	public OBJECT_T_MALL_DETAIL_SKU get_sku_detail(int sku_id, int spu_id) {
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("sku_id",sku_id);
		map.put("spu_id", spu_id);
		return listMapper.select_sku_detail(map);
	}

	@Override
	public List<T_MALL_SKU> get_list_sku_by_spu_id(int spu_id) {
		return listMapper.select_list_sku_by_spu_id(spu_id);
	}

}
