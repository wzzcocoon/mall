package cn.wzz.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.wzz.bean.T_MALL_SHOPPINGCAR;
import cn.wzz.bean.T_MALL_USER_ACCOUNT;
import cn.wzz.server.LoginServer;
import cn.wzz.server.TestServer;
import cn.wzz.service.CartService;
import cn.wzz.util.MyJsonUtil;

@Controller
public class LoginController {
	
	@Autowired
	private LoginServer loginServer;
	@Autowired
	private TestServer testServer;
	@Autowired
	private CartService cartService;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private ActiveMQQueue queueDestination;
	
	/**登录功能( + 日志功能)
	 * 	登录成功或者失败都应该重定向
		用转发的话，用户在首页上刷新页面，会进行重复登录的操作
		原则：一般情况下，在一个系统中，增删改除了查询之外的操作都应该重定向；登录查询比较特殊，也应该重定向 */
	@RequestMapping("login")
	public String login(@CookieValue(value="list_cart_cookie",required=false)String list_cart_cookie,
			T_MALL_USER_ACCOUNT user,HttpSession session,HttpServletResponse response,
			@RequestParam(value="redirect",required=false)String redirect,String dataSource_type) throws UnsupportedEncodingException {
		//登录远程接口
		//T_MALL_USER_ACCOUNT select_user = loginMapper.select_user(user);
		T_MALL_USER_ACCOUNT select_user = new T_MALL_USER_ACCOUNT();
		
		//直接调用webservice
		//String url = MyPropertyUtil.getProperty("ws.properties", "login_url");
		//LoginServer loginServer = MyWsFactoryBean.getMyWs(url,LoginServer.class);
		
		//调用webservice已经整合到Spring中（Spring属性自动注入）
		String userJson = "";
		//判断选择的数据源
		if("1".equals(dataSource_type)) {
			userJson = loginServer.login(user); 
			
			//有安全协议的请求
			testServer.ping("hello");
		}else if("2".equals(dataSource_type)) {
			userJson = loginServer.login2(user); 
		}else {		//不加这个判断，当用户没有选择数据源时。会重定向次数过多而报错
			userJson = loginServer.login(user); 
		}
		select_user = MyJsonUtil.json_to_object(userJson, T_MALL_USER_ACCOUNT.class);
		
		if(select_user == null) {
			return "redirect:/login.do";
		}else {
			
			final String message = select_user.getYh_mch() + "|登录";
			//登录成功后直接使用MQ记录日志
			// 发送mq消息（调用三方服务，一定要抓取异常）
			try {
				jmsTemplate.send(queueDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(message);
					}
				});
			} catch (Exception e) {
			}
			
			
			session.setAttribute("user", select_user);
			
			//在客户端(Cookie中)保存用户个性化信息，方便用户下次访问网站时使用
			// Cookie可能会出现中文乱码的问题，所以要对保存的信息进行转码
			Cookie cookie = new Cookie("yh_mch",URLEncoder.encode(select_user.getYh_mch(),"utf-8"));
			//设置Cookie放置的位置，读取的时候也从这里读取
			cookie.setPath("/");
			//设置Cookie保存的时间，不设置的话马上消失..
			cookie.setMaxAge(60 * 60 * 24);
			response.addCookie(cookie);
			
			//合并购物车（用户id、cookie购物车、response、session）
			List<T_MALL_SHOPPINGCAR> list_cart_db = cartService.get_list_cart_by_user(select_user);
			merge_cart(list_cart_cookie, response, session, list_cart_db);
			
			//判断是否是 从订单页登录
			if(StringUtils.isBlank(redirect)) {
				return "redirect:/index.do";
			}else {
				return "redirect:/" + redirect;
			}
		}
	}
	
	/**注销的功能*/
	@RequestMapping("goto_out")
	public String goto_out(HttpSession session) {
		// 注销session
		session.invalidate();
		return "redirect:/goto_login.do";
	}
	
	/**合并购物车*/
	private void merge_cart(String list_cart_cookie, HttpServletResponse response, 
			HttpSession session,List<T_MALL_SHOPPINGCAR> list_cart_db) {
		
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");

		if (list_cart_db == null) {
			// 判断cookie
			if (StringUtils.isBlank(list_cart_cookie)) {
				// 结束
			} else {
				// 循环将cookie中的数据，插入db
				list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
				for (int i = 0; i < list_cart.size(); i++) {
					list_cart.get(i).setYh_id(user.getId());
					cartService.add_cart(list_cart.get(i));
				}
			}
		} else {
			// 判断cookie
			if (StringUtils.isBlank(list_cart_cookie)) {
				// 结束
			}else {
				list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
				// 循环cookie和db的购物车集合，判断是插入还是更新
				for (int i = 0; i < list_cart.size(); i++) {
					boolean b = if_new_cart(list_cart_db, list_cart.get(i));
					if (b) {
						list_cart.get(i).setYh_id(user.getId());
						cartService.add_cart(list_cart.get(i));
					} else {
						for (int j = 0; j < list_cart_db.size(); j++) {
							if (list_cart.get(i).getSku_id() == list_cart_db.get(j).getSku_id()) {
								list_cart_db.get(j).setTjshl(list_cart_db.get(j).getTjshl() + list_cart.get(i).getTjshl());
								list_cart_db.get(j).setHj(list_cart_db.get(j).getSku_jg() * list_cart_db.get(j).getTjshl());
								cartService.update_cart(list_cart_db.get(j));
							}
						}
					}
				}
			}
		}

		// 同步session，查询用户的购物车集合
		session.setAttribute("list_cart_session", cartService.get_list_cart_by_user(user));
		// 删除cookie
		response.addCookie(new Cookie("list_cart_cookie", ""));
	}

	private boolean if_new_cart(List<T_MALL_SHOPPINGCAR> list_cart_db, T_MALL_SHOPPINGCAR cart) {
		boolean b = true;
		for (int i = 0; i < list_cart_db.size(); i++) {
			if(list_cart_db.get(i).getSku_id() == cart.getSku_id()) {
				b = false;
			}
		}
		return b;
	}

}
