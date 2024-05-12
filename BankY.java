//package Task;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String accountNumber;
    private String holderName;
    private double balance;

    public Account(String accountNumber, String holderName) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = 0.0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println(amount + " deposited successfully into account " + accountNumber);
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println(amount + " withdrawn successfully from account " + accountNumber);
        } else {
            System.out.println("Insufficient funds in account " + accountNumber);
        }
    }

    public void transfer(Account receiver, double amount) {
        if (amount <= balance) {
            withdraw(amount);
            receiver.deposit(amount);
            System.out.println(amount + " transferred successfully from account " + accountNumber + " to account " + receiver.getAccountNumber());
        } else {
            System.out.println("Insufficient funds in account " + accountNumber + " for transfer");
        }
    }
}

public class BankY {
    private Map<String, Account> accounts;
    private static final String DATA_FILE = "banky_data.ser";

    public BankY() {
        this.accounts = loadAccounts();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Account> loadAccounts() {
        Map<String, Account> loadedAccounts = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            loadedAccounts = (Map<String, Account>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found. Creating a new one.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loadedAccounts;
    }

    private void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createAccount(String accountNumber, String holderName) {
        Account account = new Account(accountNumber, holderName);
        accounts.put(accountNumber, account);
        saveAccounts();
        System.out.println("Account created successfully for " + holderName + " with account number " + accountNumber);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankY bank = new BankY();

        while (true) {
            System.out.println("\nWelcome to BankY");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accNum = scanner.nextLine();
                    System.out.print("Enter holder name: ");
                    String holderName = scanner.nextLine();
                    bank.createAccount(accNum, holderName);
                    break;
                case 2:
                    System.out.print("Enter account number: ");
                    accNum = scanner.nextLine();
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    bank.getAccount(accNum).deposit(depositAmount);
                    break;
                case 3:
                    System.out.print("Enter account number: ");
                    accNum = scanner.nextLine();
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    bank.getAccount(accNum).withdraw(withdrawAmount);
                    break;
                case 4:
                    System.out.print("Enter sender account number: ");
                    String senderAccNum = scanner.nextLine();
                    System.out.print("Enter receiver account number: ");
                    String receiverAccNum = scanner.nextLine();
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    Account senderAccount = bank.getAccount(senderAccNum);
                    Account receiverAccount = bank.getAccount(receiverAccNum);
                    if (senderAccount != null && receiverAccount != null) {
                        senderAccount.transfer(receiverAccount, transferAmount);
                    } else {
                        System.out.println("One or both of the accounts do not exist.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting BankY. Thank you!");
                    bank.saveAccounts();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
