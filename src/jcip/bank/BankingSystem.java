package jcip.bank;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;


import jcip.log.LogService;

public class BankingSystem {
	private static final int NUM_THREADS = 20;
	private static final int NUM_ACCOUNTS = 5;
	private static final int NUM_ITERATIONS = 100000;
	private static final int BOUND = 1000;
	
	public static void main(String[] args) {
		final Random rnd = new Random();
		final Account[] accounts = new Account[NUM_ACCOUNTS];
		
		for (int i = 0; i < accounts.length; i++)
			accounts[i] = new Account(i, rnd.nextInt(100000));
		
		int sum1 = 0;
		for (int i = 0; i < accounts.length; i++) 
			sum1 += accounts[i].getBalance();
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter printer = null;
		LogService logger = null;
		try {						
			/* Create a logger */
			fw = new FileWriter("/home/thanh/workspace/JCP/logs/BankingSystem.log", true); 
			bw = new BufferedWriter(fw);
			printer = new PrintWriter(bw);
			logger = new LogService(printer);
			logger.start();			
		} catch (Exception e) {}
		
		final Bank bank = new Bank(logger);
		//bank.disable_log();
		class TransferThread extends Thread {
			private CountDownLatch latch;
			
			public TransferThread(CountDownLatch latch) {
				this.latch = latch;
			}
			
			public void run() {
				for (int i = 0; i < NUM_ITERATIONS; i++) {
					int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
					int toAcct = rnd.nextInt(NUM_ACCOUNTS);
					int amount = rnd.nextInt(BOUND);					
					try {
						bank.transferMoney(accounts[fromAcct], accounts[toAcct], amount);
						//bank.transferMoney(accounts[fromAcct], accounts[toAcct], amount, 1, TimeUnit.MILLISECONDS);
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
				latch.countDown();
			}
		}	
		
		CountDownLatch latch = new CountDownLatch(NUM_THREADS);
		for (int i = 0; i < NUM_THREADS; i++) {
			new TransferThread(latch).start();
		}
		
		try {
			latch.await();
		} catch (Exception e) {}
		
		logger.stop();
		
		/* Verify that the total sum of all accounts does not change*/
		int sum2 = 0;
		for (int i = 0; i < accounts.length; i++) {
			sum2 += accounts[i].getBalance();
		}
		
		if (sum1 != sum2) {
			System.out.println("The total sum changes !!!");
		}
	}

}
