package model;

import framework.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Message {

    int id;
    int adId;
    int employeeId;
    String value;

    public Message(int id, int adId, int employeeId, String value) {
        this.id = id;
        this.adId = adId;
        this.employeeId = employeeId;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getAdId() {
        return adId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getValue() {
        return value;
    }

    public static int getCountSQL(String countSQL) {
        return Ad.getCountSQL(countSQL);
    }

    public static ArrayList<Message> fetchMessageSQL(String messageCollectionSQL) {
        ResultSet resultSet = Database.getInstance().sqlQuery(messageCollectionSQL)
                .printQueryBuilder().fetch();
        ArrayList<Message> adCollection = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                adCollection.add(newMessageFromDB(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return adCollection;
    }

    private static Message newMessageFromDB(ResultSet resultSet) throws SQLException {
        Message message = new Message(resultSet.getInt("id"),
                resultSet.getInt("ad_id"),
                resultSet.getInt("employee_id"),
                resultSet.getString("value"));
        return message;
    }

    public static void createMessage(int adId, int employeeId, String message) {
        String query = "INSERT INTO messages (ad_id, employee_id, value) " +
                        "VALUE (" + adId + ", " + employeeId + ", '" + message + "')";
        Database.getInstance().sqlQuery(query).printQueryBuilder().execute();
    }

    public static void updateMessage(int messageId, String updatedMessage) {
        String query = "UPDATE messages SET value=" + updatedMessage +
                " WHERE is_active=1 " +
                " AND id=" + messageId ;
        Database.getInstance().sqlQuery(query).printQueryBuilder().execute();
    }

    public static void deleteMessage(int messageId) {
        String query = "UPDATE messages SET is_active=0" +
                " WHERE id=" + messageId ;
        Database.getInstance().sqlQuery(query).printQueryBuilder().execute();
    }

}
