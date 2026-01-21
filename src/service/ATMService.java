package service;

import model.Account;

import java.util.Collections;
import java.util.List;

public class ATMService {
    private final List<Account> accounts;
    private Account currentAccount = null;

    public ATMService(List<Account> accounts) {
        this.accounts = accounts;

        System.out.println("\n[BENCHMARK] Bat dau sap xep " + accounts.size() + " tai khoan theo ID...");
        long startTime = System.nanoTime();
        
        Collections.sort(this.accounts);
        
        long endTime = System.nanoTime();
        printTime("Sap xep (TimSort)", startTime, endTime);
    }

    public Account findAccountById(String id) {
        Account searchKey = new Account(id, "", 0, "");
        
        long startTime = System.nanoTime();
        
        int index = Collections.binarySearch(accounts, searchKey);
        
        long endTime = System.nanoTime();
        
        if (index >= 0) {
            printTime("Tim kiem (Binary Search) ID " + id, startTime, endTime);
            return accounts.get(index);
        }
        return null;
    }

    public boolean login(String id, String pin) {
        Account acc = findAccountById(id);
        if (acc != null && acc.getPin().equals(pin)) {
            currentAccount = acc;
            return true;
        }
        return false;
    }

    public void logout() {
        this.currentAccount = null;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public boolean withdraw(long amount) {
        if (currentAccount == null || amount > currentAccount.getBalance()) return false;
        
        long startTime = System.nanoTime();
        
        int[] denominations = {500000, 200000, 100000, 50000};
        if (amount % 50000 != 0) return false;

        long tempAmount = amount;
        StringBuilder log = new StringBuilder();
        
        for (int note : denominations) {
            if (tempAmount >= note) {
                long count = tempAmount / note;
                tempAmount = tempAmount % note;
                log.append(note).append("x").append(count).append("; ");
            }
        }
        
        long endTime = System.nanoTime();
        printTime("Thuat toan Tham lam (Greedy)", startTime, endTime);
        
        System.out.println("Chi tiet tien nhan duoc: " + log.toString());
        currentAccount.setBalance(currentAccount.getBalance() - amount);
        return true;
    }

    private void printTime(String algoName, long start, long end) {
        long durationNano = end - start;
        double durationMs = durationNano / 1_000_000.0;
        System.out.printf(">> [THOI GIAN] %-30s: %d ns (%.4f ms)\n", algoName, durationNano, durationMs);
    }

    public boolean transfer(String targetId, long amount) {
        if (currentAccount == null) return false;
        Account target = findAccountById(targetId);

        if (target == null) {
            System.out.println("Tai khoan dich khong ton tai!");
            return false;
        }
        if (amount > currentAccount.getBalance()) return false;

        currentAccount.setBalance(currentAccount.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);
        return true;
    }
}