package cn.wzz.server.impl;

import cn.wzz.server.TestServer;

public class TestServerImpl implements TestServer {

	@Override
	public String ping(String hello) {
		System.out.println("cxf接口调用:" + hello);
		return "pong";
	}

}
