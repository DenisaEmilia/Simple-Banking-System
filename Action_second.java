package banking;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static banking.Action.DATABASE_NAME;


public class Action_second {
    Scanner scanner = new Scanner(System.in);
    Connection connection;
    String cardNumber;

    public String command = " ";

    String pin;

    Action_second(Connection connection, String cardNumber, String pin) {

        this.connection = connection;
        this.cardNumber = cardNumber;
        this.pin = pin;

        Choose_Action();
    }

    void print_options() {
        System.out.println("1. Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log out\n0. Exit");

    }

    void Choose_Action() {


      do {
            print_options();
            command = scanner.next();

            switch (command) {

                case "1": {
                    int balance = DataBase.getCardBalance(connection, cardNumber, pin);
                    System.out.println("Balance: " + balance);
                    break;
                }


                case "2": {
                    try {
                        System.out.println("Enter income:");
                        int income = scanner.nextInt();
                        DataBase.addIncome(connection, cardNumber, pin, income);
                        System.out.println("Income was added!");
                    } catch (SQLException e) {
                        System.out.println("neeee");
                        throw new RuntimeException(e);
                    }

                    break;
                }
                case "3": {
                    System.out.println("Transfer\n" +
                            "Enter card number:");
                    String card_transfer = scanner.next();
                    if(check_valid_card(card_transfer) == true) {
                        if (DataBase.chckCard_Transfer(connection, card_transfer) == false) {
                            System.out.println("Such a card does not exist.");
                            break;
                        } else {

                            System.out.println("Enter how much money you want to transfer: ");
                            int transfer_money = scanner.nextInt();

                            int balance = DataBase.getCardBalance(connection, cardNumber, pin);
                            if (balance < transfer_money)
                                System.out.println("Not enough money!");
                            else if (card_transfer.equals(cardNumber) == true)
                                System.out.println("You can't transfer money to the same account!");
                            else
                                DataBase.transfer_money(connection, card_transfer, transfer_money, cardNumber, pin);
                        }
                    }
                    else
                        System.out.println("Probably you made a mistake in the card number. Please try again!\n");

                    break;
                }
                case "4": {
                    DataBase.close_account(connection,cardNumber);
                    System.out.println("The account has been closed!");
                }
                case "5": {
                    System.out.println("You have successfully logged out!");
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    command = "0";
                    Action back = new Action(DATABASE_NAME);
                    break;
                }

                case "0": {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Bye");
                }


            }


        }while (command.equals("0") == false);
    }

    public boolean check_valid_card(String number) {
        int sum = 0;
        for (int i = 0; i < number.length() - 1; i++) {


            int nr = Integer.parseInt(String.valueOf(number.charAt(i)));

            if (i % 2 == 0)
                nr = nr * 2;

            if (nr > 9)
                nr -= 9;

            sum += nr;

        }

        int lenght = number.length() - 1;
        int checksum = Integer.parseInt(String.valueOf(number.charAt(lenght)));

        if ((sum + checksum) % 10 == 0)
            return true;
        else {
            return false;
        }

    }
}
