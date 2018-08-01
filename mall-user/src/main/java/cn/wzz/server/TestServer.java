package cn.wzz.server;

import javax.jws.WebService;

@WebService
public interface TestServer {
	public String ping(String hello);
}
