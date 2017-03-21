package org.my.service.search.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.my.service.search.SearchService;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

	public void testDubbo() {
		try {
			String hostName = InetAddress.getLocalHost().getHostName();
			System.out.println("testServer" + hostName);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
