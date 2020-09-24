package model;

import framework.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetailsAdmin {

    private int id;
    private int userId;
    private String fullName;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public DetailsAdmin(int id, int userId, String fullName) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
    }

    public static ArrayList<DetailsAdmin> fetchAdminCollection(String queryAllAdminSQL) {
        ArrayList<DetailsAdmin> detailsCollection = new ArrayList<>();
//        String queryAllAdminSQL = "SELECT details.id, details.user_id, details.full_name FROM details, users " +
//                                "WHERE users.is_active=1 AND details.is_active=1 AND details.user_id=users.id AND users.role=1 " +
//                                "AND users.id<>24 AND users.id<>" + Auth.getAuthenticatedUser().getId();
        ResultSet resultSet = Database.getInstance().sqlQuery(queryAllAdminSQL).printQueryBuilder().fetch();
        while (true) {
            try {
                if (!resultSet.next()) break;
                DetailsAdmin details = new DetailsAdmin(resultSet.getInt("id"),
                                        resultSet.getInt("user_id"),
                                        resultSet.getString("full_name"));
                detailsCollection.add(details);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return detailsCollection;
    }

    public static void create(final int userId, final String fullName) {

        String queryInsertIntoDetailsAdminSQL = "INSERT INTO details (details.user_id, details.full_name, details.is_active) " +
                                "VALUE (" + userId + ", '" + fullName + "', 1)";

        Database.getInstance().sqlQuery(queryInsertIntoDetailsAdminSQL).printQueryBuilder().execute();
    }

    public static void update(final int adminId, final String updateName) {
        String queryUpdateDetailsAdminSQL = "UPDATE details, users SET details.full_name='" + updateName + "'" +
                        " WHERE details.is_active=1 AND users.id=details.user_id AND users.role=1" +
                        " AND details.user_id<>24 AND details.user_id=" + adminId;
        Database.getInstance().sqlQuery(queryUpdateDetailsAdminSQL).printQueryBuilder().execute();
    }

    public static void deleteSoft(final int adminId) {
//        String queryUpdateDetailsAdminSQL = "UPDATE details SET details.is_active=0" +
//                        " WHERE details.is_active=1 AND users.id=details.user_id AND users.role=1 AND details.user_id<>24 AND details.user_id=" + adminId;
//        String queryUpdateUserSQL = "UPDATE users SET users.is_active=0" +
//                        " WHERE users.is_active=1 AND users.role=1 AND users.id<>24 AND users.id=" + adminId;
//        Database.getInstance().sqlQuery(queryUpdateDetailsAdminSQL).printQueryBuilder().execute();
//        Database.getInstance().sqlQuery(queryUpdateUserSQL).printQueryBuilder().execute();

        String queryUpdateDetailsAndUsers = "UPDATE details, users SET details.is_active=0, users.is_active=0" +
                " WHERE details.is_active=1 AND users.is_active=1 AND users.id=details.user_id AND users.role=1" +
                " AND details.user_id<>24 AND details.user_id=" + adminId;
        Database.getInstance().sqlQuery(queryUpdateDetailsAndUsers).printQueryBuilder().execute();

    }

}
