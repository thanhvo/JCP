package jcip;

import java.util.*;
import java.util.concurrent.*;
import jcip.annotations.*;
import java.math.*;

public class PrimeGenerator implements Runnable{
	@GuardedBy("this")
	private final List<BigInteger> primes = new ArrayList<BigInteger>();
	private volatile boolean cancelled;
	
	public void run() {
		BigInteger p = BigInteger.ONE;
		while (!cancelled) {
			p = p.nextProbablePrime();
			synchronized (this) {
				primes.add(p);
			}
		}
	}
	
	public void cancel() { cancelled = true;}
	
	public synchronized List<BigInteger> get() {
		return new ArrayList<BigInteger>(primes);
	}
	
	public static void main(String[] args) throws InterruptedException {
		PrimeGenerator generator = new PrimeGenerator();
		new Thread(generator).start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} finally {
			generator.cancel();
		}
		for(BigInteger p : generator.get())
			System.out.println(p);
	}
}
