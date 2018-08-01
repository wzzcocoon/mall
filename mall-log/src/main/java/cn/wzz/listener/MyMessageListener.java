package cn.wzz.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import cn.wzz.mapper.IndexMapper;

/**Mq消息监听容器 */
public class MyMessageListener implements MessageListener {
	
	@Autowired
	private IndexMapper indexMapper;
	
	@Override
	public void onMessage(Message message) {
		// 消息内容
		TextMessage textMessage = (TextMessage) message;
		
		try {
			String text = textMessage.getText();
			//根据消息执行内容
			indexMapper.log(text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}

