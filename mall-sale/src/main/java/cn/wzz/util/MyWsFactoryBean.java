package cn.wzz.util;

import java.util.HashMap;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.FactoryBean;

/** 使用Spring框架整合，让Spring框架自动注入这个类
 * 		1.实现Spring的FactoryBean接口
 * 		2.继承一抽象方法		3.实现回调函数
 * */
public class MyWsFactoryBean<T> implements FactoryBean<T>{

	private String url;
	private Class<T> t;
	
	public static <T>T getMyWs(String url, Class<T> t) {
		JaxWsProxyFactoryBean jwpf = new JaxWsProxyFactoryBean(); 
		jwpf.setAddress(url);
		jwpf.setServiceClass(t);
		
		//加入安全协议	--仅当是TestServerInf时加入
		if (t.getSimpleName().equals("TestServer")) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
			hashMap.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
			hashMap.put("user", "username");
			hashMap.put(WSHandlerConstants.PW_CALLBACK_CLASS, MyCallback.class.getName());
			WSS4JOutInterceptor wss4jOutInterceptor = new WSS4JOutInterceptor(hashMap);
			jwpf.getOutInterceptors().add(wss4jOutInterceptor);
		}
		
		T creat = (T) jwpf.create();
		
		return creat;
	}

	@Override
	public T getObject() throws Exception {
		return getMyWs(url,this.t);
	}

	@Override
	public Class<?> getObjectType() {
		return this.t;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Class<T> getT() {
		return t;
	}
	public void setT(Class<T> t) {
		this.t = t;
	}

}