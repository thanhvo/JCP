package jcip.bank;

public class Account {
	private final int id;
	private int balance;
	
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
	}
	
	public final int getId() {
		return id;
	}
}
