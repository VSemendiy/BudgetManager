package budget;

import java.util.Scanner;

public class Main {

    private final static Scanner sc = new Scanner(System.in);
    static Budget budget = new Budget();

    public static void main(String[] args) {
        String command = "";

        do {
            printMenu();
            command = sc.nextLine();
            System.out.println();
            action(command);
        } while (!command.equals("0"));
    }

    static void printMenu() {
        System.out.println("Choose your action:\n" +
                "1) Add income\n" +
                "2) Add purchase\n" +
                "3) Show list of purchases\n" +
                "4) Balance\n" +
                "5) Save\n" +
                "6) Load\n" +
                "7) Analyze (Sort)\n" +
                "0) Exit");
    }

    static void action(String command) {

        switch (command) {
            case "1":
                budget.addIncome();
                break;
            case "2":
                budget.addPurchase();
                break;
            case "3":
                budget.showListOfPurchases();
                break;
            case "4":
                budget.showBalance();
                break;
            case "5":
                budget.saveToFile();
                break;
            case "6":
                budget.loadFromFile();
                break;
            case "7":
                budget.analyzePurchases();
                break;
            case "0":
                System.out.println("Bye!");
                break;

        }
    }

}
