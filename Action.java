package banking;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Action {

    Scanner scanner = new Scanner(System.in);

    public static String CARD_NUMBER;
    public static String CARD_PIN;

    public static String DATABASE_NAME;
   public Connection connection;
    Action(String DATABASE_NAME) {

        this.DATABASE_NAME = DATABASE_NAME;

        connection = DataBase.getConnection(DATABASE_NAME);

        DataBase.createNewTable(connection);

        Choose_Action();
    }

    void print_options() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    void Choose_Action() {



        String command = " ";
        print_options();




        do {

            command = scanner.next();

            switch (command) {

                case "1": {
                    New_Card card = new New_Card();
                    System.out.println("Your card has been created");
                    System.out.println("Your card number:");
                    CARD_NUMBER = card.getNUMBER_CARD();
                    System.out.println(CARD_NUMBER);
                    System.out.println("Your card PIN:");
                    CARD_PIN = card.getPIN();
                    System.out.println(CARD_PIN);
                    DataBase.insertData(connection, card.getNUMBER_CARD(), card.getPIN(), card.getBALANCE());

                    print_options();

                    break;
                }

                case "2": {

                    System.out.println("Enter your card number:");
                    String Number_check = scanner.next();
                    System.out.println("Enter your PIN:");
                    String Pin_check = scanner.next();

                    if (DataBase.checkCard(connection, Number_check,Pin_check) == true) {
                        System.out.println("You have successfully logged in!");
                        Action_second obj = new Action_second(connection,Number_check, Pin_check);
                        command = "0";
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        System.out.println("Wrong card number or PIN!");
                        print_options();
                    }

                      break;
                }
                case "0": {
                    System.out.println("Bye");
                    command = "0";
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }



            }


        }while(command.equals("0") == false);
    }



}
