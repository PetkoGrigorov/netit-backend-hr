package model;

import framework.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailsEmployer {

    private int id;
    private int userId;
    private String companyName;
    private String branch;
    private String description;

    public String getCompanyName() {
        return companyName;
    }

    public DetailsEmployer(int id, int userId, String companyName, String branch, String description) {
        this.id = id;
        this.userId = userId;
        this.companyName = companyName;
        this.branch = branch;
        this.description = description;
    }

    public static void create(final int userId, final String companyName, final String branch, final String description) {

        Database.getInstance().insert("details", new HashMap<String, Object>() {{
            put("user_id", userId);
            put("company_name", companyName);
            put("branch", branch);
            put("description", description);
            put("is_active", 1);
        }}).printQueryBuilder().execute();

    }

    public static  DetailsEmployer fetchDetails(int userId) {
        DetailsEmployer details = null;
        ResultSet resultSet = Database.getInstance().selectSome(new ArrayList<String>(){{
            add("id");
            add("user_id");
            add("company_name");
            add("branch");
            add("description");
        }}, "details")
                .where("user_id", Database.Condition.EQUAL, userId)
                .andWhere("is_active", Database.Condition.EQUAL, 1)
                .printQueryBuilder().fetch();
        while (true){
            try {
                if (!resultSet.next()) break;
                details = new DetailsEmployer(resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("company_name"),
                        resultSet.getString("branch"),
                        resultSet.getString("description"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return details;
    }


}