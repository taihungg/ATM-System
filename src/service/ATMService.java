package service;

import model.Account;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMService {
    private final List<Account> accounts;
    private Account currentAccount = null;

    private Map<Integer, Integer> cashDispenser;
    private static final int[] DENOMINATIONS = {500000, 200000, 100000, 50000};

    public ATMService(List<Account> accounts, Map<Integer, Integer> cashDispenser) {
        this.accounts = accounts;

        System.out.println("\n[BENCHMARK] Bat dau sap xep " + accounts.size() + " tai khoan theo so the...");
        long startTime = System.nanoTime();
        
        Collections.sort(this.accounts);
        
        long endTime = System.nanoTime();
        printTime("Sap xep (TimSort)", startTime, endTime);

        if (cashDispenser == null || cashDispenser.isEmpty()) {
            this.cashDispenser = new HashMap<>();
            this.cashDispenser.put(500000, 0);
            this.cashDispenser.put(200000, 0);
            this.cashDispenser.put(100000, 0);
            this.cashDispenser.put(50000, 0);
        } else {
            this.cashDispenser = cashDispenser;
        }
    }

    public Account findAccountByCardNumber(String cardNumber) {
        Account searchKey = new Account(cardNumber, "", 0, "");
        
        long startTime = System.nanoTime();
        
        int index = Collections.binarySearch(accounts, searchKey);
        
        long endTime = System.nanoTime();
        
        if (index >= 0) {
            printTime("Tim kiem (Binary Search) so the " + cardNumber, startTime, endTime);
            return accounts.get(index);
        }
        return null;
    }

    public boolean login(String cardNumber, String pin) {
        Account acc = findAccountByCardNumber(cardNumber);
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

    public Map<Integer, Integer> getCashDispenser() {
        return cashDispenser;
    }

    public boolean withdraw(long amount) {
        if (currentAccount == null || amount > currentAccount.getBalance()) {
            System.out.println("Tai khoan khong du tien!");
            return false;
        }
        if (amount % 50000 != 0) {
            System.out.println("So tien phai la boi so cua 50.000!");
            return false;
        }

        Map<Integer, Integer> plan = new HashMap<>();

        boolean success = backtrackSolve(0, amount, plan);

        if (!success) {
            System.out.println("Loi: May khong du menh gia phu hop de tra so tien nay.");
            return false;
        }

        StringBuilder log = new StringBuilder();
        for (Integer note : plan.keySet()) {
            int count = plan.get(note);
            if (count > 0) {
                cashDispenser.put(note, cashDispenser.get(note) - count);
                log.append(note).append("x").append(count).append("; ");
            }
        }

        currentAccount.setBalance(currentAccount.getBalance() - amount);
        System.out.println("Rut tien thanh cong! (Thuat toan Quay lui)");
        System.out.println("Chi tiet: " + log.toString());
        return true;
    }

    private boolean backtrackSolve(int index, long remainingAmount, Map<Integer, Integer> plan) {
        if (remainingAmount == 0) return true;

        if (index >= DENOMINATIONS.length) return false;

        int note = DENOMINATIONS[index];
        int availableInStock = cashDispenser.getOrDefault(note, 0);
        int maxCount = (int) Math.min(remainingAmount / note, availableInStock);

        for (int count = maxCount; count >= 0; count--) {
            plan.put(note, count);

            long newRemaining = remainingAmount - ((long) note * count);
            
            if (backtrackSolve(index + 1, newRemaining, plan)) {
                return true;
            }
        }

        plan.remove(note);
        return false;
    }

    private void printTime(String algoName, long start, long end) {
        long durationNano = end - start;
        double durationMs = durationNano / 1_000_000.0;
        System.out.printf(">> [THOI GIAN] %s: %d ns (%.4f ms)\n", algoName, durationNano, durationMs);
    }

    public boolean transfer(String targetcardNumber, long amount) {
        if (currentAccount == null) return false;
        Account target = findAccountByCardNumber(targetcardNumber);

        if (target == null) {
            System.out.println("Tai khoan dich khong ton tai!");
            return false;
        }
        if (amount > currentAccount.getBalance()) return false;

        currentAccount.setBalance(currentAccount.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);
        return true;
    }

    public boolean refillCash(int denomination, int count) {
        if (count <= 0) {
            System.out.println("So luong to tien phai lon hon 0!");
            return false;
        }
        
        if (denomination != 500000 && denomination != 200000 && 
            denomination != 100000 && denomination != 50000) {
            System.out.println("Menh gia khong hop le! Chi chap nhan 500k, 200k, 100k, 50k.");
            return false;
        }

        int currentCount = cashDispenser.getOrDefault(denomination, 0);
        cashDispenser.put(denomination, currentCount + count);
        
        System.out.println("Da nap them " + count + " to menh gia " + denomination);
        System.out.println("Tong so to hien tai: " + cashDispenser.get(denomination));
        return true;
    }

    public void showATMStatus() {
        long totalMoney = 0;
        System.out.println("--- TRANG THAI KHO TIEN ATM ---");
        for (Integer denom : cashDispenser.keySet()) {
            int count = cashDispenser.get(denom);
            long value = (long) denom * count;
            totalMoney += value;
            System.out.println(denom + " VND: " + count + " to");
        }
        System.out.println("=> TONG TIEN TRONG MAY: " + totalMoney);
    }
}