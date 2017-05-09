package jcip;

import java.util.concurrent.*;
import java.math.*;

public class PrimeTest {
	public static void main(String[] args) {
		BlockingQueue<BigInteger> primes = new ArrayBlockingQueue<BigInteger>(10);
		PrimeProducer producer = new PrimeProducer(primes);
		PrimeConsumer consumer = new PrimeConsumer(primes);
		producer.start();
		consumer.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		}catch (InterruptedException ex) {
			ex.printStackTrace();
		}finally {
			producer.cancel();
			consumer.cancel();
		}
	}
}
