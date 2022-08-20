package banking;

import java.util.Random;

public class New_Card {
    Random random = new Random();
    private static final String BIN = "400000";
    private String ACCOUNT_NUMBER;
    private String CHECKSUM;
    private String PIN;

    private String NUMBER_CARD;

    private int BALANCE;


    New_Card() {

        this.account_random();
        this.random_CHECKSUM();
        this.random_PIN();
        this.number_Card();
    }


    private void account_random() {


        ACCOUNT_NUMBER = Integer.toString(random.nextInt(10));

        for (int i = 0; i < 8; i++) {
            ACCOUNT_NUMBER = ACCOUNT_NUMBER + Integer.toString(random.nextInt(10));
        }


    }

    private void random_CHECKSUM() {


        String PARTIAL_CARD_NUMBER = BIN + ACCOUNT_NUMBER;
        int sum = 0;
        int nr = 0;


        for (int i = 0; i < PARTIAL_CARD_NUMBER.length(); i++) {


            nr = Integer.parseInt(String.valueOf(PARTIAL_CARD_NUMBER.charAt(i)));

            if (i % 2 == 0)
                nr = nr * 2;

            if (nr > 9)
                nr -= 9;

            sum += nr;

        }


        if (sum % 10 != 0)
            CHECKSUM = Integer.toString(10 - sum % 10);
        else
            CHECKSUM = "0";


    }

    private void random_PIN() {

        PIN = Integer.toString(random.nextInt(10));

        for (int i = 0; i < 3; i++) {
            PIN = PIN + Integer.toString(random.nextInt(10));
        }

    }

    private void number_Card() {

        NUMBER_CARD = BIN + ACCOUNT_NUMBER + CHECKSUM;


    }

    public String getPIN() {
        return PIN;
    }

    public String getNUMBER_CARD() {
        return NUMBER_CARD;
    }

    public int getBALANCE() {
        return BALANCE;
    }
}
