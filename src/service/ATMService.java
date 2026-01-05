package service;

import model.Account;
import java.util.List;

public class ATMService {
    private final List<Account> accounts;
    private Account currentAccount = null;

    public ATMService(List<Account> accounts) {
        this.accounts = accounts;
    }

    private Account findAccountById(String id) {
        for (Account acc : accounts) {
            if (acc.getId().equals(id)) {
                return acc;
            }
        }
        return null;
    }

    public boolean login(String id, String pin) {
        Account acc = findAccountById(id);
        if (acc != null && acc.getPin().equals(pin)) {
            this.currentAccount = acc;
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
        if (currentAccount == null) return false;
        if (amount > currentAccount.getBalance()) {
            System.out.println("So du khong du!");
            return false;
        }
        if (amount % 50000 != 0) {
            System.out.println("So tien phai la boi cua 50,000!");
            return false;
        }

        currentAccount.setBalance(currentAccount.getBalance() - amount);
        return true;
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