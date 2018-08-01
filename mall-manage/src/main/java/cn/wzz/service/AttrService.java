package cn.wzz.service;

import java.util.List;

import cn.wzz.bean.OBJECT_T_MALL_ATTR;

public interface AttrService {

	void save_attr(int flbh2, List<OBJECT_T_MALL_ATTR> list_attr);

	List<OBJECT_T_MALL_ATTR> get_attr_list(int flbh2);

}
