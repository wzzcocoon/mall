package cn.wzz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wzz.mapper.IndexMapper;
import cn.wzz.service.LogService;

@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private IndexMapper indexMapper;
	
	@Override
	public void log(String string) {
		indexMapper.log(string);
	}

}
