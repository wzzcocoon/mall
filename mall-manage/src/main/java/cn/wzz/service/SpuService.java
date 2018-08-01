package cn.wzz.service;

import java.util.List;

import cn.wzz.bean.T_MALL_PRODUCT;

public interface SpuService {

	void saveSpu(List<String> fileNames, T_MALL_PRODUCT spu);

	List<T_MALL_PRODUCT> get_spu_list(int pp_id, int flbh2);


}
