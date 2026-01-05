package model;

public class Account {
    private String id;
    private String pin;
    private long balance;
    private String name;

    public Account(String id, String pin, long balance, String name) {
        this.id = id;
        this.pin = pin;
        this.balance = balance;
        this.name = name;
    }

    public String getId() { return id; }
    public String getPin() { return pin; }
    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return id + "," + pin + "," + balance + "," + name;
    }
}