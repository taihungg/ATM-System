package ui;

import service.ATMService;
import java.util.Scanner;

public class ConsoleUI {
    private final ATMService service;
    private final Scanner scanner;

    public ConsoleUI(ATMService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            if (service.getCurrentAccount() == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n=== CHAO MUNG DEN VOI ATM ===");
        System.out.print("Nhap so the: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Nhap PIN: ");
        String pin = scanner.nextLine();

        if (service.login(cardNumber, pin)) {
            System.out.println("Dang nhap thanh cong! Xin chao " + service.getCurrentAccount().getName());
        } else {
            System.out.println("Sai so the hoac PIN!");
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- MENU CHINH ---");
        System.out.println("1. Xem so du");
        System.out.println("2. Rut tien");
        System.out.println("3. Chuyen khoan");
        System.out.println("4. Dang xuat");
        System.out.println("0. Thoat chuong trinh");
        System.out.print("Chon chuc nang: ");

        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                System.out.println("So du hien tai: " + service.getCurrentAccount().getBalance());
                break;
            case 2:
                System.out.print("Nhap so tien can rut: ");
                long amount = Long.parseLong(scanner.nextLine());
                if (service.withdraw(amount)) {
                    System.out.println("Rut tien thanh cong. So du moi: " + service.getCurrentAccount().getBalance());
                }
                break;
            case 3:
                System.out.print("Nhap so the nguoi nhan: ");
                String targetCardNumber = scanner.nextLine();
                System.out.print("Nhap so tien: ");
                long transferAmount = Long.parseLong(scanner.nextLine());
                if (service.transfer(targetCardNumber, transferAmount)) {
                    System.out.println("Chuyen khoan thanh cong!");
                } else {
                    System.out.println("Giao dich that bai.");
                }
                break;
            case 4:
                service.logout();
                break;
            case 0:
                System.exit(0);
            default:
                System.out.println("Lua chon khong hop le!");
        }
    }
}