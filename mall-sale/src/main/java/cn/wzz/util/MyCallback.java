package cn.wzz.util;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

/**客户端的回调函数*/
public class MyCallback implements CallbackHandler {

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		// 配置username/password的代码
		WSPasswordCallback wsc = (WSPasswordCallback) callbacks[0];
		wsc.setIdentifier("cxf");
		String password = "123";// 查询数据库或者配置文件
		wsc.setPassword(password);

	}

}
