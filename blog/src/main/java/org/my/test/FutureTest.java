package org.my.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.mybatis.spring.SqlSessionTemplate;

public class FutureTest {

	/*public static class Task implements Runnable {
		public void run() {
			System.out.println(Thread.currentThread().getName() + ">>>>>" +"ah");
		}
	}
	
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < 100; i++) {
			es.submit(new Task());
		}
	}*/
	public static class Call implements Callable<String> {
		public String call() throws Exception {
			String name = Thread.currentThread().getName();
			System.out.println(name + ">>>>>ah");
			return name + ">>>>>something";
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
	}
}
