package model;

import framework.db.Database;
import model.system.Auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Ad {

    private int id;
    private int employerId;
    private String employerName;
    private String title;
    private String description;

    public Ad(int id, int employerId, String employerName, String title, String description) {
        this.id = id;
        this.employerId = employerId;
        this.employerName = employerName;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static ArrayList<Ad> fetchAll(int limit, int offset) {
        ArrayList<Ad> adCollection = new ArrayList<>();
        ResultSet resultSet1 = Database.getInstance().selectAll("ad").where("is_active", Database.Condition.EQUAL, 1)
                .limit(limit, offset)
                .printQueryBuilder().fetch();

        ResultSet resultSet = Database.getInstance().selectComplex(new HashMap<String, ArrayList<String>>(){{
            put("ad", new ArrayList<String>(){{
                add("id");
                add("employer_id");
                add("title");
                add("description");
            }});
            put("details", new ArrayList<String>(){{
                add("company_name");
            }}); }})
                .where("ad.is_active", Database.Condition.EQUAL, 1)
                .andWhereColumns("ad.employer_id", Database.Condition.EQUAL, "details.user_id")
                .limit(limit, offset).printQueryBuilder().fetch();
        while (true) {
            try {
                if (!resultSet.next()) break;
                adCollection.add(newAdFromDB(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return adCollection;
    }

    public static ArrayList<Ad> fetchAllByAuthEmployer(int limit, int offset) {
        final ArrayList<Ad> adCollection = new ArrayList<>();


//        int authId = Auth.getAuthenticatedUser().getId();
//        String companyName = DetailsEmployer.fetchDetails(authId).getCompanyName();


//        ResultSet resultSet = Database.getInstance().selectAll("ad").where("is_active", Database.Condition.EQUAL, 1)
//                .andWhere("employer_id", Database.Condition.EQUAL, authId)
//                .limit(limit, offset)
//                .printQueryBuilder().fetch();



        ResultSet resultSet = Database.getInstance().selectComplex(new HashMap<String, ArrayList<String>>(){{
                                                                    put("ad", new ArrayList<String>(){{
                                                                        add("id");
                                                                        add("employer_id");
                                                                        add("title");
                                                                        add("description");
                                                                    }});
                                                                    put("details", new ArrayList<String>(){{
                                                                        add("company_name");
                                                                    }}); }})
                .where("ad.is_active", Database.Condition.EQUAL, 1)
                .andWhereColumns("ad.employer_id", Database.Condition.EQUAL, "details.user_id")
                .andWhere("ad.employer_id", Database.Condition.EQUAL, Auth.getAuthenticatedUser().getId())
                .limit(limit, offset).printQueryBuilder().fetch();


        while (true) {
            try {
                if (!resultSet.next()) break;
                adCollection.add(newAdFromDB(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return adCollection;
    }

    public static void deleteById(int id) {
        Database.getInstance().update("ad", new HashMap<String, Object>(){{ put("is_active", 0); }})
                            .where("id", Database.Condition.EQUAL, id)
                            .andWhere("employer_id", Database.Condition.EQUAL, Auth.getAuthenticatedUser().getId())
                            .printQueryBuilder().execute();
    }

    private static Ad newAdFromDB(ResultSet resultSet) throws SQLException {
        Ad ad = new Ad(resultSet.getInt("id"),
                resultSet.getInt("employer_id"),
                resultSet.getString("company_name"),
                resultSet.getString("title"),
                resultSet.getString("description"));
        return ad;
    }

    public static void fetchAd() {

    }

}
