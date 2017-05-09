package jcip;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class PrimeConsumer extends Thread{
	BlockingQueue<BigInteger> primes;
		
	public PrimeConsumer(BlockingQueue<BigInteger> primes) {
		this.primes = primes;		
	}
	
	public void run(){
		while(!Thread.currentThread().isInterrupted()) {
			try {
				System.out.println(primes.take());
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void cancel() { interrupt();}
}
