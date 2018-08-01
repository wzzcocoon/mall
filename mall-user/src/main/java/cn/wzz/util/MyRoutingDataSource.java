package cn.wzz.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MyRoutingDataSource extends AbstractRoutingDataSource {

	/**上面的Key是多线程共享对象，并发情况下 会出现修改问题*/
	//private static String key = "1";
	private static ThreadLocal<String> key = new ThreadLocal<String>();

	@Override
	protected Object determineCurrentLookupKey() {
		//return this.key;
		return this.key.get();
	}

	public static String getKey() {
		//return key;
		return key.get();
	}

	public static void setKey(String key_in) {
		//MyRoutingDataSource.key = key_in;
		key.set(key_in);
	}

}
