package cn.wzz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wzz.bean.T_MALL_PRODUCT;
import cn.wzz.mapper.SpuMapper;
import cn.wzz.service.SpuService;

@Service
public class SpuServiceImpl implements SpuService {
	
	@Autowired
	private SpuMapper spuMapper;

	@Override
	public void saveSpu(List<String> fileNames, T_MALL_PRODUCT spu) {
		// 1.插入spu信息返回生成的主键
		spu.setShp_tp(fileNames.get(0));	//默认第一张显示
		spuMapper.insert_spu(spu);
		
		// 2.根据spu的主键，在图片信息表中批量插入spu的图片
		//spuMapper.insert_images(spu.getId(),fileNames);
		//不建议MyBatis中传入多个参数
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("shp_id", spu.getId());
		map.put("fileNames", fileNames);
		spuMapper.insert_images(map);
	}

	@Override
	public List<T_MALL_PRODUCT> get_spu_list(int pp_id, int flbh2) {
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("pp_id", pp_id);
		map.put("flbh2", flbh2);
		return spuMapper.select_spu_list(map);
	}

}
