package jcip.bank;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.*;

import jcip.log.*;

public class Bank {
	private LogService logger;
	private boolean debug;
	
	public Bank(LogService logger) {
		this.logger = logger;
		debug = true;
	}
	
	public Bank() {}
	
	public void enable_log() {
		debug = true;
	}
	
	public void disable_log() {
		debug = false;
	}
	
	public void transferMoney(final Account fromAcct, final Account toAcct, final int amount)
		throws InsufficientFundsException{
		if (fromAcct == toAcct)
			return;
		try {
			if (debug)
				logger.log("Trying to transfer money from account " + fromAcct.getId() + " to account " + toAcct.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		class Helper {
			public void transfer() throws InsufficientFundsException {				
				if (fromAcct.getBalance() < amount) {
					try {
						if (debug)
						logger.log("WARNING: Can not take " + amount + " from account " 
								+ fromAcct.getId() + " of balance " + fromAcct.getBalance());
					} catch (Exception e) {}
					throw new InsufficientFundsException("Not enough funds.");
				} else {					
					try {
						if (debug)
						logger.log("Transfering " + amount + 
								" from account " + fromAcct.getId() + " of balance " + fromAcct.getBalance() +
								" to account " + toAcct.getId());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					fromAcct.take(amount);
					toAcct.deposit(amount);
				}
			}
		}
		
		int from = fromAcct.getId();
		int to = toAcct.getId();
		
		if (from < to) {
			try {
				if (debug)
				logger.log("Trying to acquire lock " + from);
				synchronized (fromAcct) {
					if (debug)
					logger.log("Trying to acquire lock " + to);
					synchronized (toAcct) {
						new Helper().transfer();
					}
				}
			} catch (Exception ex) {}
		} else if (from > to) {
			try {
				if (debug)
				logger.log("Trying to acquire lock " + to);
				synchronized (toAcct) {
					if (debug)
					logger.log("Trying to acquire lock " + from);
					synchronized (fromAcct) {
						new Helper().transfer();
					}
				}
			} catch (Exception ex) {}
		}	
	}
	
	private long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {		
		return unit.toNanos(timeout);
	}
	
	private long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
		return ThreadLocalRandom.current().nextLong(unit.toNanos(timeout));
	}
	
	public boolean transferMoney(Account fromAcct, Account toAcct, final int amount, long timeout, TimeUnit unit)
		throws InsufficientFundsException, InterruptedException {
		long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
		long randMod = getRandomDelayModulusNanos(timeout, unit);
		long stopTime = System.nanoTime() + unit.toNanos(timeout);
		
		while (true) {
			if (debug) logger.log("Trying to acquire lock " + fromAcct.getId());
			if (fromAcct.lock.tryLock()) {
				try {
					if (debug) logger.log("Trying to acquire lock " + toAcct.getId());
					if (toAcct.lock.tryLock()) {
						try {
							if (fromAcct.getBalance() < amount) {
								if (debug) logger.log("Not enough fund. Trying to transfer " + amount + " from account of balance " + fromAcct.getBalance());
								throw new InsufficientFundsException("Not enough fund");
							} else {
								if (debug) logger.log("Transfering from account " + fromAcct.getId() + " to " + toAcct.getId());
								fromAcct.take(amount);
								toAcct.deposit(amount);
								return true;
							}
						} finally {
							toAcct.lock.unlock();
						}
					}
				} finally {
					fromAcct.lock.unlock();
				}
			}
			if (System.nanoTime() > stopTime)
				return false;
			TimeUnit.NANOSECONDS.sleep(fixedDelay + ThreadLocalRandom.current().nextLong() % randMod);
		}
	}
}
