package cn.wzz.controller;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wzz.bean.KEYWORDS_T_MALL_SKU;
import cn.wzz.util.MyPropertyUtil;

@Controller
public class KeywordsController {

	@ResponseBody
	@RequestMapping("keywords")
	public List<KEYWORDS_T_MALL_SKU> keywords(String keywords) {
		List<KEYWORDS_T_MALL_SKU> beans = null;
		
		HttpSolrServer solr = new HttpSolrServer(MyPropertyUtil.getProperty("solr.properties", "solrUrl"));
		solr.setParser(new XMLResponseParser());
		
		SolrQuery solrQuery = new SolrQuery();
		//solrQuery.setQuery("sku_mch:" + keywords);
		//设置全文搜索的配置了
		solrQuery.setQuery("combine_item:" + keywords);
		solrQuery.setRows(50);
		
		QueryResponse query  = null;
		try {
			query = solr.query(solrQuery);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		beans = query.getBeans(KEYWORDS_T_MALL_SKU.class);
		
		return beans;
	}
}
