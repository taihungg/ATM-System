package util;

import model.Account;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHandler {
    private static final String FILE_PATH = "data/accounts.txt";
    private static final String CASH_FILE = "data/cash.txt";

    public List<Account> loadAccounts() {
        List<Account> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    list.add(new Account(parts[0], parts[1], Long.parseLong(parts[2]), parts[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void saveAccounts(List<Account> accounts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Account acc : accounts) {
                bw.write(acc.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Integer> loadATMCash() {
        Map<Integer, Integer> cashMap = new HashMap<>();
        File file = new File(CASH_FILE);

        if (!file.exists()) {
            System.out.println("File kho tien khong ton tai. Khoi tao kho tien moi...");
            cashMap.put(500000, 0);
            cashMap.put(200000, 0);
            cashMap.put(100000, 0);
            cashMap.put(50000, 0);
            return cashMap;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format trong file: 500000:10
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    int denomination = Integer.parseInt(parts[0].trim());
                    int count = Integer.parseInt(parts[1].trim());
                    cashMap.put(denomination, count);
                }
            }
        } catch (IOException e) {
            System.out.println("Loi khi doc file kho tien: " + e.getMessage());
        }
        return cashMap;
    }
    
    public void saveATMCash(Map<Integer, Integer> cashDispenser) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CASH_FILE))) {
            for (Map.Entry<Integer, Integer> entry : cashDispenser.entrySet()) {
                bw.write(entry.getKey() + ":" + entry.getValue());
                bw.newLine();
            }
            System.out.println("Da luu trang thai kho tien vao " + CASH_FILE);
        } catch (IOException e) {
            System.out.println("Loi khi luu kho tien: " + e.getMessage());
        }
    }
}