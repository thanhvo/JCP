package jcip.bank;

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
}
