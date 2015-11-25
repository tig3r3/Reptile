package com.syntun.test;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueTest {
	
	public static ArrayBlockingQueue<String> list = new ArrayBlockingQueue<String>(10);

	public static void main(String[] args) {
		new Thread(new InsertData()).start();
		new Thread(new InsertData()).start();
		new Thread(new InsertData()).start();
		new Thread(new GetData()).start();
		new Thread(new GetData()).start();
		new Thread(new GetData()).start();
	}
	
}
class InsertData implements Runnable {
	@Override
	public void run() {
		while (true) {
			try {
				QueueTest.list.add(""+UUID.randomUUID());
			} catch (IllegalStateException e) {
				System.out.println(e.getMessage());
			}
			System.out.println(QueueTest.list);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class GetData implements Runnable {
	@Override
	public void run() {
		while (true) {
			System.out.println(Thread.currentThread().getName()+":"+QueueTest.list.poll());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}