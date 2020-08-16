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

    public Database selectComplex(HashMap<String, ArrayList<String>> collection) {
        String query = "SELECT ";
        String fromTablesString = " FROM ";
        Set<Map.Entry<String, ArrayList<String>>> entrySet = collection.entrySet();
        for (Map.Entry<String, ArrayList<String>> tableAndColumns : entrySet) {
            String table = tableAndColumns.getKey();
            fromTablesString += table + ", ";
            ArrayList<String> columnList = tableAndColumns.getValue();
            for (String column : columnList) {
                query += table + "." + column + ", ";
            }
        }
        this.queryBuilder = query.substring(0, (query.length() - 2)) + fromTablesString.substring(0, (fromTablesString.length() - 2));
        return this;
    }

    public Database where(String column, Condition condition, Object value) {
        this.queryBuilder += " WHERE " + column + " " + getCondition(condition) +  " " + valueForQueryBuilder(value, condition);
        return this;
    }

    public Database whereColumns(String column1, Condition condition, String column2) {
        this.queryBuilder += " WHERE " + column1 + " " + getCondition(condition) +  " " + column2;
        return this;
    }

    public Database andWhere(String column, Condition condition, Object value) {
        this.queryBuilder += " AND " + column + " " +  getCondition(condition) + " " +  valueForQueryBuilder(value, condition);
        return this;
    }

    public Database andWhereColumns(String column1, Condition condition, String column2) {
        this.queryBuilder += " AND " + column1 + " " +  getCondition(condition) + " " +  column2;
        return this;
    }

    public Database orWhere(String column, Condition condition, Object value) {
        this.queryBuilder += " OR " + " " +  column + " " +  getCondition(condition) + valueForQueryBuilder(value, condition);
        return this;
    }

    public Database orWhereColumns(String column1, Condition condition, String column2) {
        this.queryBuilder += " OR " + " " +  column1 + " " +  getCondition(condition) + column2;
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
        if (condition == Condition.LIKE) {
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

    public Database update(String table, HashMap<String, Object> columnValueCollection) {
        String query = "UPDATE " + table + " SET ";
        Set<Map.Entry<String, Object>> entrySet = columnValueCollection.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String column = entry.getKey();
            Object value = entry.getValue();
            query += column + " = " + valueForQueryBuilder(value) + ", ";
        }
        this.queryBuilder = query.substring(0, (query.length() - 2));
        return this;
    }

    public Database delete(String table) {
        this.queryBuilder = "DELETE FROM " + table;
        return this;
    }

    public Database count(String tableName) {
        this.queryBuilder = "SELECT COUNT(*) AS entry_count FROM " + tableName;
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

    public Database stringQuery(String query) {
        this.queryBuilder = query;
        return  this;
    }

    public Database selectCountFromString(String tablesString) {
        this.queryBuilder = "SELECT COUNT(*) AS entry_count FROM " + tablesString;
        return this;
    }

    public Database whereString(String conditionString) {
        this.queryBuilder += " WHERE " + conditionString;
        return this;
    }

    public Database andWhereString(String conditionString) {
        this.queryBuilder += " AND " + conditionString;
        return this;
    }

    public Database orWhereString(String conditionString) {
        this.queryBuilder += " OR " + conditionString;
        return this;
    }

}
