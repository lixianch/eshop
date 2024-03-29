package com.lixianch.eshop.cache.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.lixianch.eshop.cache.model.ProductInfo;
import com.lixianch.eshop.cache.spring.SpringContext;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import redis.clients.jedis.JedisCluster;

public class SaveProductInfo2ReidsCacheCommand extends HystrixCommand<Boolean> {
	
	private ProductInfo productInfo;
	
	public SaveProductInfo2ReidsCacheCommand(ProductInfo productInfo) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionTimeoutInMilliseconds(100)
						.withCircuitBreakerRequestVolumeThreshold(1000)
						.withCircuitBreakerErrorThresholdPercentage(70)
						.withCircuitBreakerSleepWindowInMilliseconds(60 * 1000))
				);  
		this.productInfo = productInfo;
	}
	
	@Override
	protected Boolean run() throws Exception {
		JedisCluster jedisCluster = (JedisCluster) SpringContext.getApplicationContext()
				.getBean("JedisClusterFactory"); 
		String key = "product_info_" + productInfo.getId();
		jedisCluster.set(key, JSONObject.toJSONString(productInfo));  
		return true;
	}  
	
	@Override
	protected Boolean getFallback() {
		return false;
	}
	
}
