package s2jh.biz.crawl.test;

import lab.s2jh.module.crawl.service.test.CrawlServiceTest;

import org.junit.Test;

public class MultiThreadCrawlServiceTest extends CrawlServiceTest {

    @Test
    public void startup() throws Exception {
        crawlService.startup(buildDefaultCrawlConfig(), "http://self-media.biyixia.com", "http://media.mediavalue.com.cn").get();
    }
}
