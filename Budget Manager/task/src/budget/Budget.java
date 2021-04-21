package budget;

import java.io.*;
import java.util.*;

public class Budget implements Serializable {

    private final transient Scanner sc = new Scanner(System.in);
    private double balance = 0;
    private Map<Integer, List<Purchase>> purchases = new HashMap<>();
    private final transient String[] pTypes = {"Food", "Clothes", "Entertainment", "Other", "All"};

    void showBalance() {
        System.out.printf("Balance: $%.2f%n%n", balance);
    }

    void addIncome() {
        System.out.println("Enter income:");
        balance += Double.parseDouble(sc.nextLine());
        System.out.println("Income was added!\n");
    }

    void addPurchase() {
        Integer command;

        do {
            System.out.println("Choose the type of purchase\n" +
                    "1) Food\n" +
                    "2) Clothes\n" +
                    "3) Entertainment\n" +
                    "4) Other\n" +
                    "5) Back");

            command = Integer.parseInt(sc.nextLine()) - 1;
            System.out.println();

            switch (command) {
                case 0:
                case 1:
                case 2:
                case 3:
                    System.out.println("Enter purchase name:");
                    String name = sc.nextLine();
                    System.out.println("Enter its price:");
                    double price = Double.parseDouble(sc.nextLine());
                    balance -= price;
                    if (!purchases.containsKey(command)) purchases.put(command, new ArrayList<>());
                    purchases.get(command).add(new Purchase(name, price));
                    System.out.println("Purchase was added!\n");
                    break;
                default:
                    break;
            }
        } while (command != 4);
    }

    void showListOfPurchases() {
        int command;
        do {
            System.out.println("Choose the type of purchase\n" +
                    "1) Food\n" +
                    "2) Clothes\n" +
                    "3) Entertainment\n" +
                    "4) Other\n" +
                    "5) All\n" +
                    "6) Back");

            double sum = 0d;
            command = Integer.parseInt(sc.nextLine()) - 1;
            System.out.println();

            switch (command) {
                case 0:
                case 1:
                case 2:
                case 3:
                    System.out.println(pTypes[command] + ":");
                    sum = printArrOfPurchase(purchases.get(command));
                    break;
                case 4:
                    System.out.println(pTypes[command] + ":");
                    sum = printArrOfPurchase(purchasesToList());
                    break;
                case 5:
                    break;
            }
            if (command == 5) break;
            if (sum > 0) System.out.printf("Total sum: $%.2f%n%n", sum);
            else System.out.println("The purchase list is empty\n");
        } while (true);
    }

    void saveToFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("purchases.txt"))) {
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Purchases were saved!\n");
    }

    void loadFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("purchases.txt"))) {
            Budget temp = (Budget) objectInputStream.readObject();
            this.purchases = temp.purchases;
            this.balance = temp.balance;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Purchases were loaded!\n");
    }

    void analyzePurchases() {
        int command;
        do {
            System.out.println("How do you want to sort?\n" +
                    "1) Sort all purchases\n" +
                    "2) Sort by type\n" +
                    "3) Sort certain type\n" +
                    "4) Back");

            command = Integer.parseInt(sc.nextLine()) - 1;
            System.out.println();
            double sum = 0;
            List<Purchase> tempList;
            switch (command) {
                case 0:
                    System.out.println(pTypes[4] + ":");
                    tempList = purchasesToList();
                    Collections.sort(tempList);
                    Collections.reverse(tempList);
                    sum = printArrOfPurchase(tempList);
                    if (sum > 0) System.out.printf("Total: $%.2f%n%n", sum);
                    else System.out.println("The purchase list is empty\n");
                    break;


                case 1:
                    System.out.println("Types:");
                    tempList = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        List<Purchase> entry = purchases.get(i);
                        sum = 0;
                        if (entry != null) for (Purchase p : entry) sum += p.getPrice();
                        tempList.add(new Purchase(pTypes[i] + " -", sum));
                    }
                    Collections.sort(tempList);
                    Collections.reverse(tempList);
                    sum = printArrOfPurchase(tempList);
                    if (sum > 0) System.out.printf("Total sum: $%.2f%n%n", sum);
                    else System.out.println("The purchase list is empty\n");
                    break;

                case 2:
                    System.out.println("Choose the type of purchase\n" +
                            "1) Food\n" +
                            "2) Clothes\n" +
                            "3) Entertainment\n" +
                            "4) Other");
                    command = Integer.parseInt(sc.nextLine()) - 1;
                    System.out.println();
                    System.out.println(pTypes[command] + ":");
                    tempList = purchases.get(command);
                    if (tempList != null) {
                        Collections.sort(tempList);
                        Collections.reverse(tempList);
                        sum = printArrOfPurchase(tempList);
                    }
                    if (sum > 0 || command == 1) System.out.printf("Total sum: $%.2f%n%n", sum);
                    else System.out.println("The purchase list is empty\n");
                    command = 0;
                    break;
                default:
                    break;
            }
            if (command == 3) break;
        } while (true);
    }

    List<Purchase> purchasesToList() {
        List<Purchase> tempList = new ArrayList<>();

        for (Map.Entry<Integer, List<Purchase>> entry : purchases.entrySet()) {
            tempList.addAll(entry.getValue());
        }
        return tempList;
    }

    double printArrOfPurchase(List<Purchase> purchases) {
        double sum = 0;
        if (purchases != null)
            for (Purchase p : purchases) {
                sum += p.getPrice();
                System.out.printf("%s $%.2f%n", p.getName(), p.getPrice());
            }
        return sum;
    }
}

class Purchase implements Serializable, Comparable<Purchase> {
    private final String name;
    private final Double price;

    public Purchase(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public int compareTo(Purchase purchase) {
        if (this.price <= purchase.price) return -1;
        else if (this.price > purchase.price) return 1;
        return 0;
    }
}