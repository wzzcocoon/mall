package cn.wzz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wzz.bean.OBJECT_T_MALL_ATTR;
import cn.wzz.mapper.AttrMapper;
import cn.wzz.service.AttrService;

@Service
public class AttrServiceImpl implements AttrService {

	@Autowired
	private AttrMapper attrMapper;
	
	@Override
	public void save_attr(int flbh2, List<OBJECT_T_MALL_ATTR> list_attr) {

		for (int i = 0; i < list_attr.size(); i++) {
			//插入属性，返回主键
			OBJECT_T_MALL_ATTR attr = list_attr.get(i);
			attrMapper.insert_attr(flbh2, attr);
			
			//获取返回主键，批量插入属性值
			attrMapper.insert_values(attr.getId(), attr.getList_value());
		}
	}

	@Override
	public List<OBJECT_T_MALL_ATTR> get_attr_list(int flbh2) {
		return attrMapper.select_attr_list(flbh2);
	}

}
