package cn.wzz.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wzz.bean.T_MALL_SHOPPINGCAR;
import cn.wzz.bean.T_MALL_USER_ACCOUNT;
import cn.wzz.service.CartService;
import cn.wzz.util.MyJsonUtil;

@Controller
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping("add_cart")
	public String add_cart(@CookieValue(value="list_cart_cookie",required=false)String list_cart_cookie,
						T_MALL_SHOPPINGCAR cart,HttpSession session,
						HttpServletRequest request,HttpServletResponse response) {
		
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
		
		//购物车添加业务
		if(user == null) {
			//用户未登录，操作cookie
			if(StringUtils.isBlank(list_cart_cookie)) {
				//新建cookie，放入客户端
				list_cart.add(cart);
			}else {
				list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
				//判断是否重复
				boolean b = if_new_cart(list_cart,cart);
				if(b) {
					//新车，添加
					list_cart.add(cart);
				}else {
					//老车，更新
					for (int i = 0; i < list_cart.size(); i++) {
						if(list_cart.get(i).getSku_id() == cart.getSku_id()) {
							//更新cart的添加数量和合计
							list_cart.get(i).setTjshl(list_cart.get(i).getTjshl() + cart.getTjshl());
							list_cart.get(i).setHj(list_cart.get(i).getTjshl() * list_cart.get(i).getSku_jg());
						}
					}
				}
			}
			Cookie cookie = new Cookie("list_cart_cookie",MyJsonUtil.list_to_json(list_cart));
			cookie.setPath("/");
			cookie.setMaxAge(60 * 60 * 24);
			response.addCookie(cookie);
		}
/*		else {
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");//数据库
			//用户已登录，操作db
			if(list_cart == null||list_cart.isEmpty()) {
				//db中没有数据
				cartService.add_cart(cart);
				//创建session
				list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
				list_cart.add(cart);
				session.setAttribute("list_cart_session", list_cart);
			}else {
				//判断是否重复
				boolean b = if_new_cart(list_cart,cart);
				if(b) {
					//新车，添加
					cartService.add_cart(cart);
					//同步session
					list_cart.add(cart);
				}else {
					//老车，更新
					cartService.update_cart(cart);
					//同步session
					for (int i = 0; i < list_cart.size(); i++) {
						if(list_cart.get(i).getSku_id() == cart.getSku_id()) {
							//更新cart的添加数量和合计
							list_cart.get(i).setTjshl(list_cart.get(i).getTjshl() + cart.getTjshl());
							list_cart.get(i).setHj(list_cart.get(i).getTjshl() * list_cart.get(i).getSku_jg());
							//老车，更新
							cartService.update_cart(list_cart.get(i));
						}
					}
				}
			}
		}*/

		//用户已登录，操作db
		else {
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");//数据库
			//直接判断数据库中是否存在此购物车
			boolean b = cartService.if_cart_exists(cart);
			if(!b) {
				cartService.add_cart(cart);
				if(list_cart == null||list_cart.isEmpty()) {
					//创建session
					list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
					list_cart.add(cart);
					session.setAttribute("list_cart_session", list_cart);
				}else {
					list_cart.add(cart);
				}
			}else {
				//同步session
				for (int i = 0; i < list_cart.size(); i++) {
					if(list_cart.get(i).getSku_id() == cart.getSku_id()) {
						//更新cart的添加数量和合计
						list_cart.get(i).setTjshl(list_cart.get(i).getTjshl() + cart.getTjshl());
						list_cart.get(i).setHj(list_cart.get(i).getTjshl() * list_cart.get(i).getSku_jg());
						//老车，更新
						cartService.update_cart(list_cart.get(i));
					}
				}
			}
		}
		return "redirect:/cart_success.do";
	}

	private Boolean if_new_cart(List<T_MALL_SHOPPINGCAR> list_cart, T_MALL_SHOPPINGCAR cart) {
		boolean b = true;
		for (int i = 0; i < list_cart.size(); i++) {
			if(list_cart.get(i).getSku_id() == cart.getSku_id()) {
				b = false;
			}
		}
		return b;
	}

	@RequestMapping("cart_success")
	public String cart_success() {
		return "cartSuccess";
	}

	@RequestMapping("miniCart")
	public String miniCart(@CookieValue(value="list_cart_cookie",required=false)String list_cart_cookie,
			T_MALL_SHOPPINGCAR cart,HttpSession session,ModelMap map) {

		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		
		//通过cookie或者session获取购物车数据			
		if(user == null) {
			list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
		}else {
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");
		}
		
		map.put("list_cart", list_cart);
		return "miniCartList";
	}
	
	@RequestMapping("goto_cart_list")
	public String goto_cart_list(@CookieValue(value="list_cart_cookie",required=false)String list_cart_cookie,
			T_MALL_SHOPPINGCAR cart,HttpSession session,ModelMap map) {
		
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		
		//通过cookie或者session获取购物车数据			
		if(user == null) {
			list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
		}else {
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");
		}
		
		map.put("sum", get_sum(list_cart));
		map.put("list_cart", list_cart);
		return "cartList";
	}

	@RequestMapping("change_shfxz")
	public String change_shfxz(@CookieValue(value="list_cart_cookie",required=false)String list_cart_cookie,
			T_MALL_SHOPPINGCAR cart,HttpSession session,HttpServletResponse response,ModelMap map) {
		
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		
/*		//购物车修改的业务
		if(user == null) {
			//修改cookie
			list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
			for (int i = 0; i < list_cart.size(); i++) {
				if(list_cart.get(i).getSku_id() == cart.getSku_id()) {
					list_cart.get(i).setShfxz(cart.getShfxz());
				}	
			}
			//覆盖cookie
			Cookie cookie = new Cookie("list_cart_cookie",MyJsonUtil.list_to_json(list_cart));
			cookie.setPath("/");
			cookie.setMaxAge(60 * 60 * 24);
			response.addCookie(cookie);
		}else {
			//修改session
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");
			for (int i = 0; i < list_cart.size(); i++) {
				if(list_cart.get(i).getSku_id() == cart.getSku_id()) {
					list_cart.get(i).setShfxz(cart.getShfxz());
					cartService.update_cart(list_cart.get(i));
				}	
			}
		}*/
		
		//购物车修改的业务	--优化
		if(user == null) {
			list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
		}else {
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");
		}
		for (int i = 0; i < list_cart.size(); i++) {
			if(list_cart.get(i).getSku_id() == cart.getSku_id()) {
				list_cart.get(i).setShfxz(cart.getShfxz());
				if(user == null) {
					//覆盖cookie
					Cookie cookie = new Cookie("list_cart_cookie",MyJsonUtil.list_to_json(list_cart));
					cookie.setPath("/");
					cookie.setMaxAge(60 * 60 * 24);
					response.addCookie(cookie);
				}else {
					cartService.update_cart(list_cart.get(i));
				}
			}	
		}
		
		map.put("sum", get_sum(list_cart));
		map.put("list_cart", list_cart);
		return "cartListInner";
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
	
}
