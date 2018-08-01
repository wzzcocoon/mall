package cn.wzz.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.wzz.bean.MODEL_T_MALL_ATTR;
import cn.wzz.bean.OBJECT_T_MALL_ATTR;
import cn.wzz.service.AttrService;

@Controller
public class AttrController {

	@Autowired	
	private AttrService attrService;
	
	@RequestMapping("goto_attr_add")
	public String gotp_attr_add(int flbh2,ModelMap map) {
		map.put("flbh2", flbh2);
		return "attrAdd";
	}
	
	@RequestMapping("attr_add")
	public ModelAndView attr_add(int flbh2,MODEL_T_MALL_ATTR list_attr) {
		
		//保存属性参数
		attrService.save_attr(flbh2,list_attr.getList_attr());
		
		ModelAndView mv = new ModelAndView("redirect:/index.do");
		mv.addObject("flbh2",flbh2);
		
		return mv;
	}
	
	/**返回查询出来的内嵌页（返回的是页面，内嵌页）*/
	@Deprecated
	@RequestMapping("get_attr_list")
	public String get_attr_list(int flbh2,ModelMap map) {
		
		List<OBJECT_T_MALL_ATTR> list_attr = new ArrayList<OBJECT_T_MALL_ATTR>();
		list_attr = attrService.get_attr_list(flbh2);

		map.put("list_attr", list_attr);
		map.put("flbh2", flbh2);
		
		return "attrListInner";
	}
	/**返回查询出来数据 --json*/
	@ResponseBody
	@RequestMapping("get_attr_list_json")
	public List<OBJECT_T_MALL_ATTR> get_attr_list_json(int flbh2) {
		List<OBJECT_T_MALL_ATTR> list_attr = new ArrayList<OBJECT_T_MALL_ATTR>();
		list_attr = attrService.get_attr_list(flbh2);
		return list_attr;
	}
	
	
}
