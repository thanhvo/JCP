package jcip.bank;

import java.util.concurrent.locks.*;

public class Account {
	private final int id;
	private int balance;
	public ReentrantLock lock;
	
	public void deposit(int amount) {
		balance += amount;
	}
	
	public void take(int amount) {
		balance -= amount;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public Account(int id, int balance) {
		this.balance = balance;
		this.id = id;
		lock = new ReentrantLock();
	}
	
	public final int getId() {
		return id;
	}
}
