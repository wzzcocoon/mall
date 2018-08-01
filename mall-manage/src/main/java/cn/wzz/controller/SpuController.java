package cn.wzz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.wzz.bean.T_MALL_PRODUCT;
import cn.wzz.service.SpuService;
import cn.wzz.util.MyFileUpload;

@Controller
public class SpuController {
			
	@Autowired	
	private SpuService spuService;
	
	@RequestMapping("goto_spu_add")
	public String goto_spu_add(ModelMap map,T_MALL_PRODUCT spu) {
		map.put("spu", spu);
		return "spuAdd";
	}

	@RequestMapping("spu_add")
	public ModelAndView spu_add(@RequestParam("files")MultipartFile[] files,T_MALL_PRODUCT spu) {
		//上传图片,得到每个图片上传时的文件名
		List<String> fileNames = MyFileUpload.uploadFile(files);
		//保存商品信息
		spuService.saveSpu(fileNames,spu);
		
		ModelAndView mv = new ModelAndView("redirect:/goto_spu_add.do");
		mv.addObject("flbh1",spu.getFlbh1());
		mv.addObject("flbh2",spu.getFlbh2());
		mv.addObject("pp_id",spu.getPp_id());
		
		return mv;
	}
	
	/**根据分类2Id查找属性*/
	@RequestMapping("get_spu_list")
	@ResponseBody
	public List<T_MALL_PRODUCT> get_spu_list(ModelMap map,int pp_id,int flbh2) {
		List<T_MALL_PRODUCT> list_spu = spuService.get_spu_list(pp_id,flbh2);
		return list_spu;
	}
}
