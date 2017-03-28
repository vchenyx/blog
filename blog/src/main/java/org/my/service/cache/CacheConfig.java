package org.my.service.cache;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

import com.google.common.cache.CacheBuilder;

public class CacheConfig {

	public static final int DEFAULT_MAXSIZE = 50000;
	public static final int DEFAULT_TTL = 10;
	
	
	public enum Caches {
		messageContent(8640000),
		getSomeData,
		qiniuUpToken(1800, 1),
		
		getCommonAds(60),
		getAndAssembleAreaSpecificAds(60);
		
		private int maxSize=DEFAULT_MAXSIZE;    //最大数量  
        private int ttl=DEFAULT_TTL;        //过期时间（秒）
		
		Caches() {
			
		}
		
		Caches(int ttl) {
			this.ttl = ttl;
		}
		
		Caches(int ttl, int maxSize) {  
            this.ttl = ttl;  
            this.maxSize = maxSize;  
        }  
		
		public int getMaxSize() {
			return this.maxSize;
		}
		
		public void setMaxSize(int maxSize) {  
            this.maxSize = maxSize;  
        }  
        public int getTtl() {  
            return ttl;  
        }  
        public void setTtl(int ttl) {  
            this.ttl = ttl;  
        }  
	}
	
	@Bean
	public CacheManager guavaCacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		ArrayList<GuavaCache> caches = new ArrayList<GuavaCache>();
		for (Caches c : Caches.values()) {
			caches.add(new GuavaCache(c.name(), 
					CacheBuilder.newBuilder().recordStats()
					.expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
					.maximumSize(c.getMaxSize()).build()));
		}
		
		cacheManager.setCaches(caches);
		return cacheManager;
	}
}
