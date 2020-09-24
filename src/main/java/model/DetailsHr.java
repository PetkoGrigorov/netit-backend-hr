package model;

import framework.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailsHr {

    private int id;
    private int userId;
    private String fullName;

    public DetailsHr(int id, int userId, String fullName) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
    }


    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public static void create(final int userId, final String fullName) {
        Database.getInstance().insert("details", new HashMap<String, Object>() {{
            put("user_id", userId);
            put("full_name", fullName);
            put("is_active", 1);
        }}).printQueryBuilder().execute();
    }

    public static ArrayList<DetailsHr> fetchHRCollection(String queryAllHRSQL) {
        ArrayList<DetailsHr> detailsCollection = new ArrayList<>();
        ResultSet resultSet = Database.getInstance().sqlQuery(queryAllHRSQL).printQueryBuilder().fetch();
        while (true) {
            try {
                if (!resultSet.next()) break;
                DetailsHr details = new DetailsHr(resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("full_name"));
                detailsCollection.add(details);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return detailsCollection;
    }

    public static  DetailsHr fetchDetails(int userId) {
        DetailsHr details = null;
        ResultSet resultSet = Database.getInstance().selectSome(new ArrayList<String>(){{
            add("id");
            add("user_id");
            add("full_name");
        }}, "details")
                .where("user_id", Database.Condition.EQUAL, userId)
                .andWhere("is_active", Database.Condition.EQUAL, 1)
                .printQueryBuilder().fetch();
        while (true){
            try {
                if (!resultSet.next()) break;
                details = new DetailsHr(resultSet.getInt("id"),
                                        resultSet.getInt("user_id"),
                                        resultSet.getString("full_name"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return details;
    }

    public static void update(final int hrId, final String updateName) {
        String queryUpdateDetailsHRSQL = "UPDATE details, users SET details.full_name='" + updateName + "'" +
                " WHERE details.is_active=1 AND users.id=details.user_id AND users.role=2" +
                " AND details.user_id=" + hrId;
        Database.getInstance().sqlQuery(queryUpdateDetailsHRSQL).printQueryBuilder().execute();
    }

    public static void deleteSoft(int hrId) {
        String queryUpdateDetailsAndUsers = "UPDATE details, users SET details.is_active=0, users.is_active=0" +
                " WHERE details.is_active=1 AND users.is_active=1 AND users.id=details.user_id AND users.role=2" +
                " AND details.user_id=" + hrId;
        Database.getInstance().sqlQuery(queryUpdateDetailsAndUsers).printQueryBuilder().execute();
    }

}
