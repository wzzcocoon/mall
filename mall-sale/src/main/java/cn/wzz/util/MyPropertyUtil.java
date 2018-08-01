package cn.wzz.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**读取文件上传的配置文件*/
public class MyPropertyUtil {

	public static String getProperty(String pro, String key) {
		
		Properties properties = new Properties();
		//获取流
		InputStream inputStream = MyPropertyUtil.class.getClassLoader().getResourceAsStream(pro);
		//加载文件
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//获取值
		String property = properties.getProperty(key);
		return property;
	}

	
}
