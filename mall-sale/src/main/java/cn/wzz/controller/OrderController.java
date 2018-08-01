package cn.wzz.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.wzz.bean.OBJECT_T_MALL_FLOW;
import cn.wzz.bean.OBJECT_T_MALL_ORDER;
import cn.wzz.bean.T_MALL_ADDRESS;
import cn.wzz.bean.T_MALL_ORDER_INFO;
import cn.wzz.bean.T_MALL_SHOPPINGCAR;
import cn.wzz.bean.T_MALL_USER_ACCOUNT;
import cn.wzz.exception.OverSaleException;
import cn.wzz.server.AddressServer;
import cn.wzz.service.CartService;
import cn.wzz.service.OrderService;

@Controller
@SessionAttributes("order")
public class OrderController {
	
	@Autowired
	private AddressServer addressServer;
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	
	@RequestMapping("goto_checkOrder")
	public String goto_order(BigDecimal sum, HttpSession session, 
			HttpServletRequest request, ModelMap map) {
	
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
		
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		if(user == null) {
			//进入登录界面
			return "redirect:/goto_login_checkOrder.do";
		}else {
			//session中的购物车列表
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");// 数据库

			OBJECT_T_MALL_ORDER order = new OBJECT_T_MALL_ORDER();	//订单对象
			order.setYh_id(user.getId());
			order.setJdh(1);
			order.setZje(get_sum(list_cart));
			
			//结算业务
			//根据购物车的选中状态，获取商品的库存地址信息
			//set中放入库存地址，获取多个唯一的地址信息
			HashSet<String> set_kcdz = new HashSet<String>();
			for(int i=0 ; i < list_cart.size(); i++) {
				T_MALL_SHOPPINGCAR shoppingcar = list_cart.get(i);
				if(shoppingcar.getShfxz().equals("1")) {
					//选中的商品，放入set中，去重
					set_kcdz.add(shoppingcar.getKcdz());
				}
			}
			//根据库存地址，封装送货清单
			List<OBJECT_T_MALL_FLOW> list_flow = new ArrayList<OBJECT_T_MALL_FLOW>();
			
			Iterator<String> iterator = set_kcdz.iterator();
			while(iterator.hasNext()) {
				//获取set中的库存地址
				String kcdz = iterator.next();
				//根据库存地址，生成送货清单(一个送货地址对应一个送货清单)
				OBJECT_T_MALL_FLOW flow = new OBJECT_T_MALL_FLOW();
				flow.setMqdd("商品未出库");
				flow.setPsfsh("xx快递");
				flow.setYh_id(user.getId());
				//一个送货清单对应一个送货清单信息
				List<T_MALL_ORDER_INFO> list_info = new ArrayList<T_MALL_ORDER_INFO>();
				
				// 循环购物车，将购物车对象转化成订单信息
				for (int i=0 ; i < list_cart.size(); i++) {
					//当商品是选中状态  并且 库存地址相同时，封装到一个送货清单中
					if(list_cart.get(i).getShfxz().equals("1") && list_cart.get(i).getKcdz().equals(kcdz)) {
						T_MALL_SHOPPINGCAR cart = list_cart.get(i);
						T_MALL_ORDER_INFO info = new T_MALL_ORDER_INFO();

						// 将购物车转为订单信息
						info.setGwch_id(cart.getId());
						info.setShp_tp(cart.getShp_tp());
						info.setSku_id(cart.getSku_id());
						info.setSku_jg(cart.getSku_jg());
						info.setSku_kcdz(kcdz);
						info.setSku_mch(cart.getSku_mch());
						info.setSku_shl(cart.getTjshl());
						list_info.add(info);
					}
				}
				flow.setList_info(list_info);
				// 将送货清单放入送货清单集合
				list_flow.add(flow);
			}
			
			//送货订单放入主订单中
			order.setList_flow(list_flow);//内存中的对象--游离态
			try {
				//调用第三方接口时，应当进行一定的处理
				List<T_MALL_ADDRESS> list_address = addressServer.get_addresses(user);
				map.put("list_address", list_address);
			} catch (Exception e) {
				e.printStackTrace();
				// 处理用户系统调用异常
				return "redirect:/orderErr.do";
			}
			
			map.put("order", order);//和 @SessionAttributes配合，使放入其中的值可以在这个类中使用
			
			return "checkOrder";
		}
	}
	
	private BigDecimal get_sum(List<T_MALL_SHOPPINGCAR> list_cart) {
		BigDecimal sum = new BigDecimal("0");
		for (int i = 0; i < list_cart.size(); i++) {
			if(list_cart.get(i).getShfxz().equals("1")) {
				sum = sum.add(new BigDecimal(list_cart.get(i).getHj() + ""));
			}
		}
		return sum;
	}
	
	/**提交订单的功能*/
	@RequestMapping("save_order")
	public String save_order(@ModelAttribute("order")OBJECT_T_MALL_ORDER order,
			T_MALL_ADDRESS address,HttpSession session) {
		//获取地址信息
		T_MALL_ADDRESS get_address = addressServer.get_address(address.getId());
		//订单保存业务
		orderService.save_order(order, get_address);
		//获取user
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		//重新同步session
		session.setAttribute("list_cart_session", cartService.get_list_cart_by_user(user));
		
		//重定向到支付服务，传入订单号和交易金额
		//return "redirect:/goto_pay.do";
		
		//真正的支付服务界面
		return "pay2";
	}
	
	@Deprecated
	@RequestMapping("goto_pay")
	public String goto_pay() {
		// 伪支付服务
		return "pay";
	}

	@RequestMapping("pay_success")
	public String pay_success(@ModelAttribute("order")OBJECT_T_MALL_ORDER order,
			T_MALL_ADDRESS address) {
		// 支付成功
		try {
			orderService.paySuccess(order);
		} catch (OverSaleException e) {
			String message = e.getMessage();
			if(message.equals("over sale")) {
				return "redirect:/over_sale.do";
			}else {
				return "系统交易失败！";
			}
		}
		return "redirect:/order_success.do";
	}
	@ResponseBody
	@RequestMapping("pay_success2")
	public String pay_success2(@ModelAttribute("order")OBJECT_T_MALL_ORDER order,
			T_MALL_ADDRESS address) {
		// 支付成功
		try {
			orderService.paySuccess(order);
		} catch (OverSaleException e) {
			e.printStackTrace();
			return "success";
		}
		return "success";
	}
	
	@RequestMapping("order_success")
	public String order_success() {
		return "order_success";
	}

	@RequestMapping("over_sale")
	public String over_sale() {
		return "over_sale";
	}
}
