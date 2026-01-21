import model.Account;
import service.ATMService;
import ui.ConsoleUI;
import util.FileHandler;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();

        System.out.println("Dang khoi dong he thong...");
        List<Account> accounts = fileHandler.loadAccounts();
        Map<Integer, Integer> cashMap = fileHandler.loadATMCash();

        ATMService atmService = new ATMService(accounts, cashMap);
        ConsoleUI ui = new ConsoleUI(atmService);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nDang luu du lieu xuong o cung...");
            fileHandler.saveAccounts(accounts);
            fileHandler.saveATMCash(atmService.getCashDispenser());
            System.out.println("Da luu thanh cong! Tam biet.");
        }));

        ui.start();
    }
}