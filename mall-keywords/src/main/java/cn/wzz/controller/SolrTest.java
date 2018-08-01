package cn.wzz.controller;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

import cn.wzz.bean.KEYWORDS_T_MALL_SKU;
import cn.wzz.factory.MySqlSessionFactory;
import cn.wzz.mapper.TestMapper;
import cn.wzz.util.MyPropertyUtil;

/**不使用Spring时，单独使用Mybatis查询数据库*/
public class SolrTest {

	public static void main(String[] args) throws Exception {
		
		SqlSessionFactory myF = MySqlSessionFactory.getMyF();
		
		TestMapper mapper = myF.openSession().getMapper(TestMapper.class);
		
		List<KEYWORDS_T_MALL_SKU> list_sku = mapper.select_list_by_flbh2(28);
		
		System.out.println(list_sku);
		
		//向solr中导入sku数据
		HttpSolrServer solr = new HttpSolrServer(MyPropertyUtil.getProperty("solr.properties", "solrUrl"));
		// 设置解析器；用于java和solr之间的数据类型转换
		solr.setParser(new XMLResponseParser());
		solr.addBeans(list_sku);
		solr.commit();
		
		//从solr中查询数据
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("sku_mch:*");
		solrQuery.setRows(50);
		QueryResponse query = solr.query(solrQuery);
		List<KEYWORDS_T_MALL_SKU> beans = query.getBeans(KEYWORDS_T_MALL_SKU.class);
		System.out.println(beans);
	}	
}
