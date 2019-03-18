package com.lixianch.eshop.cache.ha.hystrix.command;

import com.lixianch.eshop.cache.ha.degrade.IsDegrade;
import com.lixianch.eshop.cache.ha.model.ProductInfo;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;

public class GetProductInfoFacadeCommand extends HystrixCommand<ProductInfo> {
	
	private Long productId;
	
	public GetProductInfoFacadeCommand(Long productId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("GetProductInfoFacadeCommand"))
		        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
		        		.withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)
		        		.withExecutionIsolationSemaphoreMaxConcurrentRequests(15)));
		this.productId = productId;
	}
	
	@Override
	protected ProductInfo run() throws Exception {
		if(!IsDegrade.isDegrade()) {
			return new GetProductInfoCommand(productId).execute();
		} else {
			return new GetProductInfoFromMySQLCommand(productId).execute();
		}
	}
	
	@Override
	protected ProductInfo getFallback() {
		return new ProductInfo();
	}
	
}
