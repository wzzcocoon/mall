package cn.wzz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wzz.bean.OBJECT_T_MALL_DETAIL_SKU;
import cn.wzz.bean.T_MALL_SKU;
import cn.wzz.service.ListService;

@Controller
public class IndexController {
	
	
	@Autowired
	private ListService listService;
	
	@RequestMapping("index")
	public String index(HttpServletRequest request,ModelMap map) {
		
		//不使用这种方法，这个方法只有在首先看到用户的特殊信息---可以直接去页面上使用js获取Cookie的信息
		/*String yh_mch = ""; 
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length != 0) {
			for (int i = 0; i < cookies.length; i++) {
				String name = cookies[i].getName();
				if("yh_mch".equals(name)) {
					yh_mch = cookies[i].getValue();
				}
			}
		}
		map.put("yh_mch", yh_mch);*/
		
		return "index";
	}
	
	@RequestMapping("goto_login")
	public String LoginPage() {
		return "login";
	}

	@RequestMapping("goto_login_checkOrder")
	public String LoginPage2() {
		return "loginOrder";
	}

	@RequestMapping("goto_sku_detail")	
	public String goto_sku_detail(int sku_id,int spu_id,ModelMap map) {
		// 调用详情的业务	--spu信息，sku信息，图片集合，属性规格
		OBJECT_T_MALL_DETAIL_SKU obj_sku = listService.get_sku_detail(sku_id,spu_id);
		
		//调用商品sku集合的业务	--同spu下库存sku的集合
		List<T_MALL_SKU> list_sku = listService.get_list_sku_by_spu_id(spu_id);
		
		map.put("obj_sku", obj_sku);
		map.put("list_sku", list_sku);
		
		return "skuDetail";
	}

	@RequestMapping("orderErr")
	public String orderErr() {
		return "orderErr";
	}
}
