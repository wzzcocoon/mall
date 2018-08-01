package cn.wzz.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wzz.bean.KEYWORDS_T_MALL_SKU;
import cn.wzz.bean.MODEL_T_MALL_SKU_ATTR_VALUE;
import cn.wzz.bean.OBJECT_T_MALL_ATTR;
import cn.wzz.bean.OBJECT_T_MALL_SKU;
import cn.wzz.bean.T_MALL_SKU_ATTR_VALUE;
import cn.wzz.service.AttrService;
import cn.wzz.service.ListService;
import cn.wzz.util.MyCacheUtil;
import cn.wzz.util.MyHttpGetUtil;
import cn.wzz.util.MyJsonUtil;
import cn.wzz.util.MyPropertyUtil;

@Controller
public class ListController {

	@Autowired
	private AttrService attrService;
	@Autowired
	private ListService listService;
	
	/**根据属性分类检索商品--AJAX异步查询		1. 数组传参
	@ResponseBody
	@RequestMapping("get_list_by_attr")
	public String get_list_by_attr(@RequestParam("param_array[]")String[] param_array,ModelMap map) {
		// 如果是字符串数组，需要进行切割，对数据进行处理
		return "skuList";
	}*/

	/**根据属性分类检索商品--AJAX异步查询		2. JSON传参(参数形式一)
	@ResponseBody
	@RequestMapping("get_list_by_attr")
	public String get_list_by_attr(MODEL_T_MALL_SKU_ATTR_VALUE list_attr,ModelMap map) {
		return "skuList";
	}*/
	
	/**根据属性分类检索商品--AJAX异步		2. JSON传参(参数形式二)*/
	@RequestMapping("get_list_by_attr")
	public String get_list_by_attr(MODEL_T_MALL_SKU_ATTR_VALUE list_attr,
								int flbh2,ModelMap map) {
		// 根据属性查询列表的业务
		List<OBJECT_T_MALL_SKU> list_sku = new ArrayList<OBJECT_T_MALL_SKU>();
		
		// redis缓存检索
		List<T_MALL_SKU_ATTR_VALUE> list_attr2 = list_attr.getList_attr();
		String[] keys = new String[list_attr2.size()];
		for (int i = 0; i < list_attr2.size(); i++) {
			keys[i] = "attr_"+flbh2+"_"+list_attr2.get(i).getShxm_id()+"_"+list_attr2.get(i).getShxzh_id();
		}
		String interKeys = MyCacheUtil.interKeys(keys);
		list_sku = MyCacheUtil.getList(interKeys, OBJECT_T_MALL_SKU.class);

		//缓存未命中；mysql检索
		if(list_sku==null || list_sku.size()==0) {
			//当前交叉检索的结果
			list_sku = listService.get_list_by_attr(list_attr.getList_attr(),flbh2);
			// 将检索结果同步到Redis
			// 同步redis
			for (int i = 0; i < list_attr2.size(); i++) {
				String key = keys[i];// attr_28_1_2

				// 判断redis中是否存在这个key
				boolean if_key = MyCacheUtil.if_key(key);

				if (!if_key) {
					// 根据属性id，查询出属性值集合
					// 循环属性值，拼接出attr的key
					// key对应的sku集合
					List<T_MALL_SKU_ATTR_VALUE> list_attr_for_redis = new ArrayList<T_MALL_SKU_ATTR_VALUE>();
					List<OBJECT_T_MALL_SKU> list_sku_for_redis = new ArrayList<OBJECT_T_MALL_SKU>();
					T_MALL_SKU_ATTR_VALUE attr_value = new T_MALL_SKU_ATTR_VALUE();
					attr_value.setShxm_id(list_attr2.get(i).getShxm_id());
					attr_value.setShxzh_id(list_attr2.get(i).getShxzh_id());
					list_attr_for_redis.add(attr_value);
					list_sku_for_redis = listService.get_list_by_attr(list_attr_for_redis, flbh2);

					// 再根据属性和属性值可以查询 出对应的sku集合
					// attr的可以和sku的集合循环 插入到redis
					MyCacheUtil.setKey(key, list_sku_for_redis);
				}

			}
		}
				
		map.put("list_sku", list_sku);
		return "skuList";
	}

	
	@RequestMapping("goto_list")
	public String goto_list(int flbh2,ModelMap map) {
		//flbh2属性的集合(**展示属性集合)
		List<OBJECT_T_MALL_ATTR> list_attr = attrService.get_attr_list(flbh2);
		
		/**flbh2商品的集合(**商品的分类检索-根据flbh2查询**)*/
		List<OBJECT_T_MALL_SKU> list_sku = new ArrayList<OBJECT_T_MALL_SKU>();
				
		// redis缓存检索
		String key = "class_2_" + flbh2;
		list_sku = MyCacheUtil.getList(key, OBJECT_T_MALL_SKU.class);
		
		//缓存未命中；mysql检索
		if(list_sku==null || list_sku.size()==0) {
			list_sku =listService.get_list_by_flbh2(flbh2);
			//将检索结果同步到Redis
			MyCacheUtil.setKey(key, list_sku);
		}
		
		map.put("list_attr", list_attr);
		map.put("list_sku", list_sku);
		map.put("flbh2", flbh2);
		return "list";
	}
	
	@RequestMapping("keywords")
	public String keywords(String keywords,ModelMap map) {
		//调用关键字查询接口
		String doGet = "";
		String property = MyPropertyUtil.getProperty("ws.properties", "keywords_url");
		try {
			doGet = MyHttpGetUtil.doGet(property+"?keywords="+keywords);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<KEYWORDS_T_MALL_SKU> list_sku = MyJsonUtil.json_to_list(doGet, KEYWORDS_T_MALL_SKU.class);
		
		map.put("keywords",keywords);
		map.put("list_sku",list_sku);
		return "search";
	}

}
