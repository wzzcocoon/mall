package cn.wzz.testJsonFile;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.gson.Gson;

import cn.wzz.bean.T_MALL_CLASS_1;
import cn.wzz.mapper.T_MALL_CLASS_1_mapper;

/** 查询表中数据，生成JSON的静态文件 (单纯的使用Mybatis)*/
public class Test {

	public static void main(String[] args) throws Exception {

		// 1.创建SqlSessionFactory对象
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		// 2.获取SqlSession对象，相当于JDBC中的 Connection
		// openSession()方法中可以传入一个boolean类型的值，设置自动提交还是手动提交commit
		SqlSession session = sqlSessionFactory.openSession();
		
		// 3.获取Mapper
		T_MALL_CLASS_1_mapper mapper = session.getMapper(T_MALL_CLASS_1_mapper.class);
		
		// 4.操作获取数据
		List<T_MALL_CLASS_1> list = mapper.getList();
		System.out.println(list.size());
		
		// 5.创建Gson对象，生成json
		Gson gson = new Gson();
		String jsonStr = gson.toJson(list);
		
		// 6.生成JSON文件，使用IO流
		FileOutputStream out = new FileOutputStream("H:/git/class_1.js"); 
		out.write(jsonStr.getBytes());
		
		// 7.关闭
		out.close();
		session.close();
	}

}
