package acp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import util.XML2File;

public class RunThreadTest {

	public static void main(String[] args) {

		try {
			Object lock = new Object();

			ThreadAcpControl a = new ThreadAcpControl(lock);
			a.start();

			Thread.sleep(50);

			while (true) {

				System.out.println("等待命令：");
				BufferedReader stream = new BufferedReader(new InputStreamReader(System.in));
				String s="";
				try {
					s =stream.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				synchronized (lock) {
					String stringRa = "21:30:49.17";
					String stringDec = "12�14'41.6\"";
					//					MyAcpCommandList.add(s);
					MyAcpCommandList.addParam("stringRa" , stringRa);
					MyAcpCommandList.addParam("stringDec", stringDec);
					MyAcpCommandList.addParam("planName", "BatPlan");
					lock.notify();
				}
				Thread.sleep(1000);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
