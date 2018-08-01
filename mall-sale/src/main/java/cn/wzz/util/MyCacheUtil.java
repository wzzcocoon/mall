package cn.wzz.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;

/**Redis数据缓存工具类*/
public class MyCacheUtil {
	
	/**根据key查询Redis缓存(放的是JSON数据)，返回一个存对象的集合*/
	public static <T> List<T> getList(String key, Class<T> t) {
		
		List<T> list = new ArrayList<T>();
	
		Jedis jedis = null;
		
		// 第三方数据库调用（可能Redis服务器没有开启。注意异常处理）
		try {
			jedis = JedisPoolUtils.getJedis();
		} catch (Exception e) {
			return null;
		}
		
		//	从zset中获取值
		Set<String> zrange = jedis.zrange(key, 0, -1);
		Iterator<String> iterator = zrange.iterator();
		while(iterator.hasNext()) {
			String sku_url_str = iterator.next();
			T sku = MyJsonUtil.json_to_object(sku_url_str, t);
			list.add(sku);
		}
		
		return list;
	}
	
	
	/**根据多个值,生成一个存放数据(多个属性中公共的商品信息)的key的名称*/
	public static String interKeys(String... keys) {
		
		Jedis jedis = null;
		
		//第三方数据库调用
		try {
			jedis = JedisPoolUtils.getJedis();
		} catch (Exception e) {
			return null;
		}
		
		// 生成动态的key
		String k0 = "combine";
		for (int i = 0; i < keys.length; i++) {
			k0 = k0 + "_" + keys[i];
		}
		if (!jedis.exists(k0)) {
			jedis.zinterstore(k0, keys);
		}
		
		return k0;
	}
	
	/**【查询数据库后】同步Redis数据的方法*/
	public static <T> void setKey(String key, List<T> list) {

		// 第三方数据调用
		Jedis jedis = null;
		try {
			jedis = JedisPoolUtils.getJedis();
			// 先清理、再同步
			//(因为向Redis中zset数据结构中插入的是JSON数据(只是价格或者其他updata了),整个JSON不同了,不删除的话旧数据依然存在)
			jedis.del(key);
			// 同步reids
			for (int i = 0; i < list.size(); i++) {
				jedis.zadd(key, i, MyJsonUtil.object_to_json(list.get(i)));
			}
		} catch (Exception e) {
			// 记日志
		}
		
	}

	/**判断Redis中是否存在这个key*/
	public static boolean if_key(String key) {
		// 第三方数据调用
		Jedis jedis = null;
		Boolean exists = false;
		try {
			jedis = JedisPoolUtils.getJedis();
			exists = jedis.exists(key);
		} catch (Exception e) {
			// 记日志
		}

		return exists;
	}

}
