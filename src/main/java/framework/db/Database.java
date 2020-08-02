package framework.db;

import framework.config.ConfigDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Database {

    private static Database instance = null;
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String URL = ConfigDB.getDbUrl();
    private final String USER = ConfigDB.getDbUser();
    private final String PASS = ConfigDB.getDbPassword();
    private Connection dbConnection;
    private Statement dbStatement;
    private String queryBuilder;

    private Database() {
        try {
            Class.forName(this.DRIVER);
            this.dbConnection = DriverManager.getConnection(this.URL, this.USER, this.PASS);
            this.dbStatement = this.dbConnection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (instance == null){
            instance = new Database();
        }
        return instance;
    }

    public Database selectAll(String table) {
        String query = "SELECT * FROM " + table;
        this.queryBuilder = query;
        return this;
    }

    public Database selectSome(ArrayList<String> columnCollection, String table) {
        String query = "SELECT ";
        for (String element : columnCollection) {
            query += element + ", ";
        }
        this.queryBuilder = query.substring(0, (query.length() - 2)) + " FROM " + table;
        return this;
    }

    public Database where(String column, Condition condition, Object value) {
        this.queryBuilder += " WHERE " + column + " " + getCondition(condition) +  " " + valueForQueryBuilder(value, condition);
        return this;
    }

    public Database andWhere(String column, Condition condition, Object value) {
        this.queryBuilder += " AND " + column + " " +  getCondition(condition) + " " +  valueForQueryBuilder(value, condition);
        return this;
    }

    public Database orWhere(String column, Condition condition, Object value) {
        this.queryBuilder += " OR " + " " +  column + " " +  getCondition(condition) + valueForQueryBuilder(value, condition);
        return this;
    }

  /*  public Database like(String column, Object likeValue) {
        this.queryBuilder += " WHERE " + column + " LIKE " + "'%" + likeValue + "%'";
        return this;
    }*/

    public Database limit(int limit) {
        this.queryBuilder += " LIMIT " + limit;
        return this;
    }

    public Database limit(int limit, int offset) {
        this.queryBuilder += " LIMIT " + offset + ", " + limit;
        return this;
    }

    public Database countAll(String table) {
        this.queryBuilder = "SELECT COUNT(*) AS entry_count FROM " + table;
        return this;
    }

    public enum Condition {
        LOWER, EQUAL, GREATER, LIKE
    }

    private String getCondition(Condition condition) {
        switch (condition) {
            case LOWER : return "<";
            case EQUAL : return "=";
            case GREATER : return ">";
            case LIKE : return "LIKE";
            default : return null;
        }
    }

    private String valueForQueryBuilder(Object value, Condition condition) {
        if (condition.equals("LIKE")) {
            return "'%" + value + "%'";
        }
        if (value instanceof String) {
            return "'" + value + "'";
        }
        if (value instanceof Integer) {
            return value.toString();
        }
        return null;
    }

    private String valueForQueryBuilder(Object value) {
        if (value instanceof String) {
            return "'" + value + "'";
        }
        if (value instanceof Integer) {
            return value.toString();
        }
        return null;
    }

    public Database printQueryBuilder() {
        System.out.println(this.queryBuilder);
        return this;
    }

    public ResultSet fetch() {
        try {
            return this.dbStatement.executeQuery(this.queryBuilder);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Database insert(String table, HashMap<String, Object> columnValueCollection) {
        String query = "INSERT INTO " + table;
        String keys = "(";
        String values = "(";
        for (Map.Entry<String, Object> element: columnValueCollection.entrySet()){
            keys += element.getKey() + ", ";
            values += valueForQueryBuilder(element.getValue()) + ", ";
        }
        keys = keys.substring(0, (keys.length() - 2)) + ")";
        values = values.substring(0, (values.length() - 2)) + ")";
        this.queryBuilder = query + " " + keys + " VALUE " + values;
        return this;
    }

    public Database delete(String table) {
        this.queryBuilder = "DELETE FROM " + table;
        return this;
    }

    public void execute() {
        try {
            this.dbStatement.execute(this.queryBuilder, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getLastInsertedId() {
        try {
            ResultSet resultSet = this.dbStatement.getGeneratedKeys();
            while(resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Long.parseLong(null);
    }

}
