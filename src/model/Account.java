package model;

public class Account implements Comparable<Account>{
    private String cardNumber;
    private String pin;
    private long balance;
    private String name;

    public Account(String cardNumber, String pin, long balance, String name) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
        this.name = name;
    }

    public String getCardNumber() { return cardNumber; }
    public String getPin() { return pin; }
    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }
    public String getName() { return name; }

    @Override
    public int compareTo(Account other) {
        return this.cardNumber.compareTo(other.cardNumber);
    }

    @Override
    public String toString() {
        return cardNumber + "," + pin + "," + balance + "," + name;
    }
}