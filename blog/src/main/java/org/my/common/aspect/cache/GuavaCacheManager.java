package org.my.common.aspect.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.Cache;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;

public class GuavaCacheManager extends AbstractTransactionSupportingCacheManager {

	private final ConcurrentHashMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(16);
	
	private boolean dynamic = true;
	
	private CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
			.concurrencyLevel(1000)//最大并发数
			.initialCapacity(100)//初始容量
			.maximumSize(10000);//最大容量
	
	private CacheLoader<Object, Object> cacheLoader;
	
	//是否允许null值
	private boolean allowNullValues = true;
	
	//是否允许自动回滚
	private boolean transactionAware = false;
	
	public boolean isTransactionAware() {
		return transactionAware;
	}

	public void setTransactionAware(boolean transactionAware) {
		this.transactionAware = transactionAware;
	}

	public GuavaCacheManager() {
		
	}
	public GuavaCacheManager(String... cacheNames) {
		setCacheNames(Arrays.asList(cacheNames));
	}
	
	private void setCacheNames(Collection<String> cacheNames) {
		if (cacheNames != null) {
			for (String name : cacheNames) {
				this.cacheMap.put(name, createGuavaCache(name));
			}
			this.dynamic = false;
		} else {
			this.dynamic = true;
		}
	}
	
	public void setCacheBuilder(CacheBuilder<Object, Object> cacheBuilder) {
		Assert.notNull(cacheBuilder, "CacheBuilder must be not null");
		doSetCacheBuilder(cacheBuilder);
	}
	
	public void setCacheBuilderSpec(CacheBuilderSpec cacheBuilderSpec) {
		doSetCacheBuilder(CacheBuilder.from(cacheBuilderSpec));
	}
	
	public void setCacheSpecification(String cacheSpecification) {
		doSetCacheBuilder(CacheBuilder.from(cacheSpecification));
	}
	
	public void setCacheLoader(CacheLoader<Object, Object> cacheLoader) {
		if (!ObjectUtils.nullSafeEquals(this.cacheLoader, cacheLoader)) {
			this.cacheLoader = cacheLoader;
			refreshKnownCaches();
		}
	}
	
	public void setAllowNullValues(boolean allowNullValues) {
		if (this.allowNullValues != allowNullValues) {
			this.allowNullValues = allowNullValues;
			refreshKnownCaches();
		}
	}
	
	public boolean isAllowNullValues() {
		return this.allowNullValues;
	}
	
	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheMap.keySet());
	}
	
	
	
	private void doSetCacheBuilder(CacheBuilder<Object, Object> cacheBuilder) {
		if (!ObjectUtils.nullSafeEquals(this.cacheBuilder, cacheBuilder)) {
			this.cacheBuilder = cacheBuilder;
			refreshKnownCaches();
		}
	}

	private void refreshKnownCaches() {
		for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
			entry.setValue(createGuavaCache(entry.getKey()));
		}
	}

	@Override
	protected Collection<? extends Cache> loadCaches() {
		Collection<Cache> values = cacheMap.values();
		return values;
	}
	
	protected Cache decorateCache(Cache cache) {
		return (isTransactionAware() ? new TransactionAwareCacheDecorator(cache) : cache);
	}
	
	@Override
	public Cache getCache(String name) {
		Cache cache = this.cacheMap.get(name);
		if (cache == null && this.dynamic) {
			cache = createGuavaCache(name);
            this.cacheMap.put(name, cache);
		}
		return super.getCache(name);
	}

	private Cache createGuavaCache(String name) {
		Cache cache = new GuavaCache(name, createNativeGuavaCache(name), isAllowNullValues());
		if (cache != null) {
			cache = decorateCache(cache);
		}
		return cache;
	}
	
	protected com.google.common.cache.Cache<Object, Object> createNativeGuavaCache(String name) {
		if (this.cacheLoader != null) {
			return this.cacheBuilder.build(this.cacheLoader);
		} else {
			return this.cacheBuilder.build();
		}
	}
	
	
}
