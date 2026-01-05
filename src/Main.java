import model.Account;
import service.ATMService;
import ui.ConsoleUI;
import util.FileHandler;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();

        System.out.println("Dang khoi dong he thong...");
        List<Account> accounts = fileHandler.loadAccounts();

        ATMService atmService = new ATMService(accounts);
        ConsoleUI ui = new ConsoleUI(atmService);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nDang luu du lieu xuong o cung...");
            fileHandler.saveAccounts(accounts);
            System.out.println("Da luu thanh cong! Tam biet.");
        }));

        ui.start();
    }
}