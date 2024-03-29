package util.sqlexporter;

import model.GameHistory;
import model.SimpleGameState;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLExporter {

    private static final String USERNAME = "c1529854";
    private static final String PASSWORD = "gihoadyod4";
    private static final String CONNECTION = "jdbc:mysql://csmysql.cs.cf.ac.uk/c1529854?autoReconnect=true";

    private static final String SUMMARY_TEST = "Summary_Test";
    private static final String SUMMARY = "Summary";
    private static final String SUMMARY_NEW = "Summary_New";

    private static final String INSERT_STATEMENT_TEST = "INSERT INTO `Entries_Test` (`HAND`, `CASTLE_FU`, `CASTLE_FD_SIZE`, `OP_HAND_SIZE`, `OP_CASTLE_FU`, `OP_CASTLE_FD_SIZE`, `TOP`, `DECK_EMPTY`, `WON`) VALUES ";
    private static final String INSERT_STATEMENT = "INSERT INTO `Entries` (`HAND`, `CASTLE_FU`, `CASTLE_FD_SIZE`, `OP_HAND_SIZE`, `OP_CASTLE_FU`, `OP_CASTLE_FD_SIZE`, `TOP`, `DECK_EMPTY`, `WON`) VALUES ";
    private static final String INSERT_STATEMENT_NEW = "INSERT INTO `Entries_New` (`HAND`, `CASTLE_FU`, `CASTLE_FD_SIZE`, `OP_HAND_SIZE`, `OP_CASTLE_FU`, `OP_CASTLE_FD_SIZE`, `TOP`, `DECK_EMPTY`, `WON`) VALUES ";

    private Connection connection;

    public SQLExporter() {
        try {
            connection = DriverManager.getConnection(
                    CONNECTION, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.exit(0);
        }
    }

    public void updateDatabase(List<GameHistory> gameHistories) throws SQLException {
        List<Entry> entries = new ArrayList<>();
        for (GameHistory gameHistory : gameHistories) {
            for (SimpleGameState gameState : gameHistory.getGameStates()) {
                entries.add(gameState.toEntry(gameHistory.hasWon()));
            }
        }
        int aiWins = countAiWins(gameHistories);
        int[] summaryValues = getCurrentSummaryValues();
        int[] summaryValuesToAdd = new int[]{gameHistories.size(), aiWins, gameHistories.size() - aiWins};
        int[] summaryValuesToUpdateInDB = new int[]{summaryValues[0] + summaryValuesToAdd[0],
                summaryValues[1] + summaryValuesToAdd[1],
                summaryValues[2] + summaryValuesToAdd[2]};
        loadEntries(entries, summaryValuesToUpdateInDB);
    }

    private int[] getCurrentSummaryValues() throws SQLException {
        Statement stmt;
        ResultSet rs = null;

        stmt = connection.createStatement();

        if (stmt.execute("SELECT * FROM " + SUMMARY_NEW)) {
            rs = stmt.getResultSet();
        }

        while (rs.next()) {
            int totalGames = rs.getInt("TOTAL_GAMES");
            int aiWins = rs.getInt("AI_WINS");
            int lowestWins = rs.getInt("LOWEST_WINS");

            return new int[]{totalGames, aiWins, lowestWins};
        }

        try {
            rs.close();
        } catch (SQLException sqlEx) { /* ignore */ }


        try {
            stmt.close();
        } catch (SQLException sqlEx) { /* ignore */ }

        return null;
    }

    private void loadEntries(List<Entry> entries, int[] summaryValues) throws SQLException {
        Statement stmt;

        StringBuilder insertStatement = new StringBuilder(INSERT_STATEMENT_NEW);
        for (Entry entry : entries) {
            insertStatement.append(entry.getSqlStatement()).append(",");
        }
        insertStatement.deleteCharAt(insertStatement.length() - 1);

        String updateStatement = "UPDATE " + SUMMARY_NEW + " SET " +
                "TOTAL_GAMES =" + summaryValues[0]
                + ", AI_WINS =" + summaryValues[1]
                + ", LOWEST_WINS =" + summaryValues[2] + ";";

        stmt = connection.createStatement();
        stmt.executeUpdate(insertStatement.toString());
        stmt.executeUpdate(updateStatement);

        try {
            stmt.close();
        } catch (SQLException sqlEx) { /* ignore */ }
    }

    private int countAiWins(List<GameHistory> gameHistories) {
        int aiWins = 0;
        for (GameHistory gameHistory : gameHistories) {
            if (gameHistory.hasWon()) {
                aiWins++;
            }
        }
        return aiWins;
    }
}
