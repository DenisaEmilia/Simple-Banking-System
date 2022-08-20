package banking;

import java.sql.*;

public class DataBase {


    public static Connection getConnection(String databaseName) {

        String url = "jdbc:sqlite:" + databaseName;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;

    }


    public static void createNewTable(Connection connection) {

        //String url = "jdbc:sqlite:C://sqlite/" + databaseName;

        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + " id INTEGER PRIMARY KEY,\n"
                + " number TEXT,\n"
                + " pin TEXT,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void insertData(Connection connection, String accountNumber, String pin, int balance) {
        String sql = "INSERT INTO card(number,pin, balance) VALUES(?,?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, pin);
            pstmt.setInt(3, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static int getCardBalance(Connection connection, String accountNumber, String pin) {

        String sql = "SELECT * FROM card WHERE number = " + accountNumber + " AND pin = " + pin;

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int balance = rs.getInt("balance");

            return balance;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
      return 0;
    }

    public static void addIncome(Connection connection, String accountNumber, String pin, int income) throws SQLException {

        String update_balance = "UPDATE card SET balance = ? WHERE number = " + accountNumber + " AND pin = " + pin;

        try (PreparedStatement preparedStatement = connection.prepareStatement(update_balance)) {

                int balance = DataBase.getCardBalance(connection, accountNumber, pin);

                preparedStatement.setInt(1, balance + income);
                preparedStatement.executeUpdate();
               // System.out.println("Balance: " + balance);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }



}
   public static boolean chckCard_Transfer(Connection connection, String accountNumber)  {
       try (Statement st = connection.createStatement()) {
           return st.executeQuery("SELECT EXISTS (SELECT * FROM card WHERE number = " + accountNumber +");").getBoolean(1);
       } catch (Exception e) {
           e.printStackTrace();}
       return false;
   }

    public static boolean checkCard(Connection connection, String accountNumber, String pin) {
        try (Statement st = connection.createStatement()) {
            return st.executeQuery("SELECT EXISTS (SELECT * FROM card WHERE number = " + accountNumber + " AND pin = " + pin + ");").getBoolean(1);
        } catch (Exception e) {
            e.printStackTrace();}
        return false;
    }

    public static void transfer_money(Connection connection, String accountNumber, int income, String proprietar, String pin) {

        boolean ok = false;
        try (Statement st = connection.createStatement()) {
            ok = st.executeQuery("SELECT EXISTS (SELECT * FROM card WHERE number = " + accountNumber +");").getBoolean(1);
        } catch (Exception e) {
            e.printStackTrace();}

        if(ok == true) {

            String update_balance_trasfer = "UPDATE card SET balance = ? WHERE number = " + accountNumber;

            try (PreparedStatement preparedStatement = connection.prepareStatement(update_balance_trasfer)) {

                    int balance = DataBase.getCardBalance(connection, accountNumber, pin);

                    preparedStatement.setInt(1, balance + income);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }




            String update_balance_after_transfer = "UPDATE card SET balance = ? WHERE number = " + proprietar;

            try (PreparedStatement preparedStatement = connection.prepareStatement(update_balance_after_transfer)) {

                try {
                    int balance = DataBase.getCardBalance(connection, proprietar, pin);

                    preparedStatement.setInt(1, balance - income);
                    preparedStatement.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }





    }

    public static void close_account(Connection connection, String card_number){
        String delete_card = "DELETE FROM card WHERE number = " + card_number;
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(delete_card);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    }
