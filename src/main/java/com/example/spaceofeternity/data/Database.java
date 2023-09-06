package com.example.spaceofeternity.data;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    static final String DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/ScoresSpaceOfEternity";
    static final String USER = "postgres";
    static final String PASS = "2031";
    static final int limitRows = 10;

    public Database() {
        Statement statement = null;
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();

            String sql1 = "CREATE TABLE IF NOT EXISTS TOP_10_SCORES " +
                    "(ID            INT     PRIMARY KEY," +
                    " NICKNAME      TEXT     NOT NULL, " +
                    " SCORE         INT     NOT NULL )";
            statement.executeUpdate(sql1);


            String sql2 = "CREATE TABLE IF NOT EXISTS RECENT_SCORE " +
                    "(ID            INT    PRIMARY KEY," +
                    " NICKNAME      TEXT    NOT NULL, " +
                    " SCORE         INT     NOT NULL )";
            statement.executeUpdate(sql2);

            String sql3 = "CREATE TABLE IF NOT EXISTS TOP_SCORE " +
                    "(ID            INT    PRIMARY KEY," +
                    " NICKNAME      TEXT    NOT NULL, " +
                    " SCORE         INT     NOT NULL )";
            statement.executeUpdate(sql3);

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveGlobalScores(String nickName, int score) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);

            int id = getLastID() + 1;
            stmt = c.createStatement();
            stmt.setMaxRows(limitRows);
            int newId = getIdWithMinScore();
            if (getRowCount() < stmt.getMaxRows()) { addNewUserToTop10(id, nickName, score); }
            else if (score > getMinScore()) { removeUserAndAddNew(newId, nickName, score); }

            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLocalScores(String nickName, int score) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);

            int id = getLastID() + 1;
            stmt = c.createStatement();
            stmt.setMaxRows(limitRows);

            refreshRecentUser(nickName, score);
            refreshTopScore(nickName, score);

            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewUserToTop10(int id, String nickName, int score) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            String sql1 = "INSERT INTO TOP_10_SCORES (ID, NICKNAME, SCORE) VALUES(" + id + ", '" + nickName + "', " + score + ")";
            stmt.executeUpdate(sql1);
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshRecentUser(String nickName, int score) {
        int id = 0;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            String delete = "DELETE from RECENT_SCORE";
            stmt.executeUpdate(delete);
            stmt.setMaxRows(limitRows);
            String sql2 = "INSERT INTO RECENT_SCORE (ID, NICKNAME, SCORE) VALUES(" + id + ", '" + nickName + "', " + score + ")";
            stmt.executeUpdate(sql2);
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshTopScore(String nickName, int score) {
        int id = 0;
        int maxScore = 0;
        ResultSet rs = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            String sql3 = "SELECT ID, NICKNAME, SCORE FROM TOP_SCORE ORDER BY SCORE";
            rs = stmt.executeQuery(sql3);
            while (rs.next()) {
                if (maxScore < rs.getInt("SCORE")) {
                    maxScore = rs.getInt("SCORE");
                }
            }

            if (maxScore < score) {
                String delete = "DELETE from TOP_SCORE";
                stmt.executeUpdate(delete);
                stmt.setMaxRows(limitRows);

                sql3 = "INSERT INTO TOP_SCORE (ID, NICKNAME, SCORE) VALUES(" + id + ", '" + nickName + "', " + score + ")";
                stmt.executeUpdate(sql3);
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getMinScore() {
        int minScore = 100000;
        ResultSet rs = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            String sql1 = "SELECT ID, NICKNAME, SCORE FROM TOP_10_SCORES ORDER BY SCORE";
            rs = stmt.executeQuery(sql1);
            while (rs.next()) {
                if (minScore > rs.getInt("SCORE")) {
                    minScore = rs.getInt("SCORE");
                }
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minScore;
    }

    private int getRowCount() {
        int rowCount = 0;
        ResultSet rs = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            String sql1 = "SELECT COUNT(*) AS COUNT FROM TOP_10_SCORES";
            rs = stmt.executeQuery(sql1);
            while (rs.next()) {
                rowCount = rs.getInt("COUNT");
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowCount;
    }

    private void removeUserAndAddNew(int id, String nickName, int score) {
        int minScore = 100000;
        ResultSet rs = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            String sql1 = "DELETE FROM TOP_10_SCORES WHERE ID = " + id;
            stmt.executeUpdate(sql1);

            sql1 = "INSERT INTO TOP_10_SCORES (ID, NICKNAME, SCORE) VALUES(" + id + ", '" + nickName + "', " + score + ")";
            stmt.executeUpdate(sql1);

            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getIdWithMinScore() {
        int minScore = 100000;
        int id = 0;
        ResultSet rs = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            String sql1 = "SELECT ID, NICKNAME, SCORE FROM TOP_10_SCORES ORDER BY SCORE, ID";
            rs = stmt.executeQuery(sql1);
            while (rs.next()) {
                if (minScore > rs.getInt("SCORE")) {
                    minScore = rs.getInt("SCORE");
                    id = rs.getInt("ID");
                }
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private int getLastID() {
        int id = 0;
        ResultSet rs = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            String sql1 = "SELECT ID, NICKNAME, SCORE FROM TOP_10_SCORES ORDER BY ID";
            rs = stmt.executeQuery(sql1);
            while (rs.next()) {
                id = rs.getInt("ID");
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public ArrayList<String> getDataBaseTop10Scores() {
        ArrayList<String> statistics = new ArrayList<>();
        ArrayList<String> statisticsNickName = new ArrayList<>();
        ArrayList<String> statisticsScore = new ArrayList<>();
        ResultSet rs = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            String sql = "SELECT * FROM TOP_10_SCORES ORDER BY SCORE";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                statisticsNickName.add(rs.getString("NICKNAME"));
                statisticsScore.add(Integer.toString(rs.getInt("SCORE")));
            }
            for (int i = statisticsNickName.size() - 1; i >= 1; i--) {
                for (int j = 0; j < i; j++) {
                    if (Integer.parseInt(statisticsScore.get(j)) < Integer.parseInt(statisticsScore.get(j + 1))) {
                        String tempScore = statisticsScore.get(j);
                        statisticsScore.set(j, statisticsScore.get(j + 1));
                        statisticsScore.set(j + 1, tempScore);
                        String tempNickName = statisticsNickName.get(j);
                        statisticsNickName.set(j, statisticsNickName.get(j + 1));
                        statisticsNickName.set(j + 1, tempNickName);
                    }
                }
            }
            for (int i = 0; i < statisticsNickName.size(); i++) {
                statistics.add(statisticsNickName.get(i));
                statistics.add(statisticsScore.get(i));
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statistics;
    }

    public ArrayList<String> getDataBaseRecentScore() {
        ArrayList<String> statistics = new ArrayList<>();
        ResultSet rs = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            String sql = "SELECT * FROM RECENT_SCORE ORDER BY SCORE";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                statistics.add(rs.getString("NICKNAME"));
                statistics.add(Integer.toString(rs.getInt("SCORE")));
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statistics;
    }

    public ArrayList<String> getDataBaseTopScore() {
        ArrayList<String> statistics = new ArrayList<>();
        ResultSet rs = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            String sql = "SELECT * FROM TOP_SCORE ORDER BY SCORE";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                statistics.add(rs.getString("NICKNAME"));
                statistics.add(Integer.toString(rs.getInt("SCORE")));
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statistics;
    }
}
