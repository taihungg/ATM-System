package ui;

import service.ATMService;
import java.util.Scanner;

public class ConsoleUI {
    private final ATMService service;
    private final Scanner scanner;

    private static final String ADMIN_CARD = "9999";
    private static final String ADMIN_PIN = "8888";

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
        System.out.print("Nhap so the (hoac '0' de thoat): ");
        String cardNumber = scanner.nextLine();
        
        if (cardNumber.equals("0")) System.exit(0);

        System.out.print("Nhap PIN: ");
        String pin = scanner.nextLine();

        if (cardNumber.equals(ADMIN_CARD) && pin.equals(ADMIN_PIN)) {
            System.out.println(">> Xin chao Quan Tri Vien (Admin)!");
            showAdminMenu();
            return;
        }

        if (service.login(cardNumber, pin)) {
            System.out.println("Dang nhap thanh cong! Xin chao " + service.getCurrentAccount().getName());
        } else {
            System.out.println("Sai so the hoac PIN!");
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- MENU KHACH HANG ---");
        System.out.println("1. Xem so du");
        System.out.println("2. Rut tien");
        System.out.println("3. Chuyen khoan");
        System.out.println("4. Dang xuat");
        System.out.println("0. Thoat chuong trinh");
        System.out.print("Chon chuc nang: ");

        try {
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
                    System.out.println("Da dang xuat!");
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Vui long nhap so!");
        }
    }

    private void showAdminMenu() {
        boolean isAdmin = true;
        while (isAdmin) {
            System.out.println("\n--- MENU QUAN TRI (ADMIN) ---");
            System.out.println("1. Xem trang thai kho tien (So luong to)");
            System.out.println("2. Nap them tien vao may");
            System.out.println("3. Dang xuat Admin");
            System.out.println("0. Tat nguon he thong");
            System.out.print("Chon chuc nang: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        service.showATMStatus();
                        break;
                    case 2:
                        handleRefillCash();
                        break;
                    case 3:
                        System.out.println("Da dang xuat Admin.");
                        isAdmin = false;
                        break;
                    case 0:
                        System.out.println("Admin yeu cau tat nguon...");
                        System.exit(0);
                    default:
                        System.out.println("Lua chon khong hop le!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so!");
            }
        }
    }

    private void handleRefillCash() {
        System.out.println("\n-- NAP TIEN VAO KHO --");
        System.out.print("Nhap menh gia (500000, 200000, 100000, 50000): ");
        try {
            int denomination = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Nhap so luong to muon them: ");
            int count = Integer.parseInt(scanner.nextLine());

            if (service.refillCash(denomination, count)) {
                System.out.println("=> Nap tien thanh cong!");
            } else {
                System.out.println("=> Nap tien that bai (Kiem tra lai menh gia hoac so luong).");
            }
        } catch (NumberFormatException e) {
            System.out.println("Loi: Vui long nhap so nguyen hop le!");
        }
    }
}