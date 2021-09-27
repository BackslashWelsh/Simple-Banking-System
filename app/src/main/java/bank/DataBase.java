package bank;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

class DataBase {
    private SQLiteDataSource database;

    DataBase(String dbName) {
        initDataBase(dbName);
    }

    private void initDataBase(String dbName) {
        String url = "jdbc:sqlite:" + dbName;
        database = new SQLiteDataSource();
        database.setUrl(url);

        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER PRIMARY KEY," +
                    "number TEXT NOT NULL," +
                    "pin  TEXT NOT NULL," +
                    "balance INTEGER DEFAULT 0)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createAccount(Account account) {
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("INSERT INTO card" +
                    "(id, number, pin)" +
                    "VALUES" +
                    account.toDataBase());

            ResultSet rs = stmt.executeQuery("SELECT number, pin FROM card " +
                    "WHERE id =" + account.getClientID());

            System.out.println("Your card has been created");
            System.out.printf("Your card number:%n%s%n" +
                            "Your card PIN:%n%s%n",
                    rs.getString("number"),
                    rs.getString("pin"));
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String addBalance(int clientID, int balance) {
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("UPDATE card " +
                    "SET balance = balance + " + balance +
                    " WHERE id = " + clientID);

        } catch (SQLException e) {
            e.printStackTrace();
            return "Sorry something went wrong try again.";
        }
        return "Income was added!";
    }

    int getBalance(int clientID) {
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();

             ResultSet rs = stmt.executeQuery(
                     "SELECT balance " +
                             "FROM card " +
                             "WHERE id =" + clientID)) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    void transfer(int clientID, int transferID, int amount) {
        String transferFrom = "UPDATE card SET balance = balance - ? " +
                "WHERE id = ?";
        String transferTo = "UPDATE card SET balance = balance + ? " +
                "WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmtFrom = conn.prepareStatement(transferFrom);
             PreparedStatement stmtTo = conn.prepareStatement(transferTo)) {

            conn.setAutoCommit(false);
            stmtFrom.setInt(1, amount);
            stmtFrom.setInt(2, clientID);
            stmtFrom.executeUpdate();

            stmtTo.setInt(1, amount);
            stmtTo.setInt(2, transferID);
            stmtTo.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String deleteAccount(int clientID) {
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM card " +
                    " WHERE id = " + clientID);

        } catch (SQLException e) {
            e.printStackTrace();
            return "Sorry something went wrong try again.";
        }
        return "Income was added!";
    }

    int getCardID(String cardN) {
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();

             ResultSet rs = stmt.executeQuery(
                     "SELECT id " +
                             "FROM card " +
                             "WHERE number = '" + cardN + "'")) {

            return rs.next() ? rs.getInt(1) : -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    boolean isIdToPIN(int id, String cardPIN) {
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();

             ResultSet rs = stmt.executeQuery(
                     "SELECT id " +
                             "FROM card " +
                             "WHERE id = '" + id +
                             "' AND pin = '" + cardPIN + "'")) {

           return  rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    void showAllClients() {
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM card")) {

            while ((rs.next())) {
                System.out.printf("id-%s N%s PIN%s\n",
                        rs.getString("id"),
                        rs.getString("number"),
                        rs.getString("pin"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int getID() {
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT max(id) FROM card")) {

            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
