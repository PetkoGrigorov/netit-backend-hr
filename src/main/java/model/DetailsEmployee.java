package model;

import framework.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailsEmployee {

    private int id;
    private int userId;
    private String fullName;
    private int age;
    private String town;
    private String education;

    public DetailsEmployee(int id, int userId, String fullName, int age, String town, String education) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.age = age;
        this.town = town;
        this.education = education;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public String getTown() {
        return town;
    }

    public String getEducation() {
        return education;
    }

    public static void create(final int userId, final String fullName, final int age, final String town, final String education) {

        Database.getInstance().insert("details", new HashMap<String, Object>() {{
            put("user_id", userId);
            put("full_name", fullName);
            put("age", age);
            put("town", town);
            put("education", education);
            put("is_active", 1);
        }}).printQueryBuilder().execute();
    }

    public static  DetailsEmployee fetchDetails(int userId) {
        DetailsEmployee details = null;
        ResultSet resultSet = Database.getInstance().selectSome(new ArrayList<String>(){{
            add("id");
            add("user_id");
            add("full_name");
            add("age");
            add("town");
            add("education");
        }}, "details")
                .where("user_id", Database.Condition.EQUAL, userId)
                .andWhere("is_active", Database.Condition.EQUAL, 1)
                .printQueryBuilder().fetch();
        while (true){
            try {
                if (!resultSet.next()) break;
                details = new DetailsEmployee(resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("full_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("town"),
                        resultSet.getString("education"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return details;
    }

    public static int getCountSQL(String countSQL) {
        ResultSet resultSet = Database.getInstance().sqlQuery(countSQL)
                .printQueryBuilder().fetch();
        while (true) {
            try {
                if (!resultSet.next()) break;
                return resultSet.getInt("entry_count");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static DetailsEmployee newEmployeeDetailsFromDB(ResultSet resultSet) throws SQLException {
        DetailsEmployee details = new DetailsEmployee(resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("full_name"),
                resultSet.getInt("age"),
                resultSet.getString("town"),
                resultSet.getString("education"));
        return details;
    }

    public static ArrayList<DetailsEmployee> fetchEmployeeDetailsSQL(String employeeCollectionSQL) {
        ResultSet resultSet = Database.getInstance().sqlQuery(employeeCollectionSQL)
                .printQueryBuilder().fetch();
        ArrayList<DetailsEmployee> adCollection = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                adCollection.add(newEmployeeDetailsFromDB(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return adCollection;
    }

}
