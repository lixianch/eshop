package com.lixianch.eshop.cache.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.lixianch.eshop.cache.kafka.KafkaConsumer;
import com.lixianch.eshop.cache.rebuild.RebuildCacheThread;
import com.lixianch.eshop.cache.spring.SpringContext;
import com.lixianch.eshop.cache.zk.ZooKeeperSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 系统初始化的监听器
 * @author Administrator
 *
 */
public class InitListener implements ServletContextListener {
	
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
		SpringContext.setApplicationContext(context);
		
		new Thread(new KafkaConsumer("cache-message")).start();
		new Thread(new RebuildCacheThread()).start();
		
		ZooKeeperSession.init();
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
