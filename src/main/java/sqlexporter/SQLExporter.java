package sqlexporter;

import model.GameHistory;
import model.SimpleGameState;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLExporter {

    private static final String USERNAME = "c1529854";
    private static final String PASSWORD = "gihoadyod4";
    private static final String CONNECTION = "jdbc:mysql://csmysql.cs.cf.ac.uk/c1529854";
    public static final String INSERT_STATEMENT = "INSERT INTO `Entries` (`HAND`, `CASTLE_FU`, `CASTLE_FD_SIZE`, `OP_HAND_SIZE`, `OP_CASTLE_FU`, `OP_CASTLE_FD_SIZE`, `TOP`, `DECK_EMPTY`, `WON`) VALUES ";

    private Connection connection;

    public SQLExporter() {
        try {
            connection = DriverManager.getConnection(
                    CONNECTION, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return;
        }
    }

    public void exportGames(List<GameHistory> gameHistories) throws SQLException {
        List<Entry> entries = new ArrayList<>();
        for (GameHistory gameHistory : gameHistories) {
            for (SimpleGameState gameState : gameHistory.getGameStates()) {
                entries.add(gameState.toEntry(gameHistory.hasWon()));
            }
        }
        loadEntries(entries);
    }

    public void updateSummary(Summary summary) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();

            if (stmt.execute("SELECT * FROM Summary")) {
                rs = stmt.getResultSet();
            }

            // Now do something with the ResultSet ....
            while (rs.next()) {
                int totalGames = rs.getInt("TOTAL_GAMES") + summary.getTotalWins();
                int aiWins = rs.getInt("AI_WINS") + summary.getAiWins();
                int lowestWins = rs.getInt("LOWEST_WINS") + summary.getLowestWins();

                update(totalGames, aiWins, lowestWins);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            return;
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { /* ignore */ }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { /* ignore */ }
            }
        }
    }

    private void loadEntries(List<Entry> entries) throws SQLException {
        Statement stmt;

        StringBuilder sqlStatement = new StringBuilder(INSERT_STATEMENT);

        for (Entry entry : entries) {
            sqlStatement.append(entry.getSqlStatement()).append(",");
        }
        sqlStatement.deleteCharAt(sqlStatement.length() - 1);

        stmt = connection.createStatement();
        stmt.execute(sqlStatement.toString());

        try {
            stmt.close();
        } catch (SQLException sqlEx) { /* ignore */ }

        System.out.println("Entries added to database.");
    }

    private void update(int totalGames, int aiWins, int lowestWins) {
        Statement updateStatement = null;
        try {
            updateStatement = connection.createStatement();

            updateStatement.execute("UPDATE Summary SET " +
                    "TOTAL_GAMES =" + totalGames
                    + ", AI_WINS =" + aiWins
                    + ", LOWEST_WINS =" + lowestWins);

            System.out.println("Summary table updated.");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        } finally {
            if (updateStatement != null) {
                try {
                    updateStatement.close();
                } catch (SQLException sqlEx) { /* ignore */ }
                updateStatement = null;
            }
        }
    }
}
