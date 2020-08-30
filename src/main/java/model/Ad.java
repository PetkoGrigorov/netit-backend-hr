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

    public String getEmployerName() {
        return employerName;
    }

    public static ArrayList<Ad> fetchAll(int limit, int offset) {
        ArrayList<Ad> adCollection = new ArrayList<>();

        ResultSet resultSet = Database.getInstance().selectComplex(new HashMap<String, ArrayList<String>>() {{
            put("ad", new ArrayList<String>() {{
                add("id");
                add("employer_id");
                add("title");
                add("description");
            }});
            put("details", new ArrayList<String>() {{
                add("company_name");
            }});
        }})
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

    public static ArrayList<Ad> fetchAllLike(String column, String likeKey, int limit, int offset) {
        ArrayList<Ad> adCollection = new ArrayList<>();
        ResultSet resultSet = Database.getInstance().selectComplex(new HashMap<String, ArrayList<String>>() {{
            put("ad", new ArrayList<String>() {{
                add("id");
                add("employer_id");
                add("title");
                add("description");
            }});
            put("details", new ArrayList<String>() {{
                add("company_name");
            }});
        }})
                .where("ad.is_active", Database.Condition.EQUAL, 1)
                .andWhereColumns("ad.employer_id", Database.Condition.EQUAL, "details.user_id")
                .andWhere(column, Database.Condition.LIKE, likeKey)
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

        ResultSet resultSet = Database.getInstance().selectComplex(new HashMap<String, ArrayList<String>>() {{
            put("ad", new ArrayList<String>() {{
                add("id");
                add("employer_id");
                add("title");
                add("description");
            }});
            put("details", new ArrayList<String>() {{
                add("company_name");
            }});
        }})
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

    public static boolean isAdBelongsToAuthEmployer(int id) {
        ResultSet resultSet = Database.getInstance().selectAll("ad").where("id", Database.Condition.EQUAL, id)
                .andWhere("employer_id", Database.Condition.EQUAL, Auth.getAuthenticatedUser().getId())
                .printQueryBuilder().fetch();
        try {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Ad fetchAd(int id) {

        ResultSet resultSet = Database.getInstance().selectComplex(new HashMap<String, ArrayList<String>>() {{
            put("ad", new ArrayList<String>() {{
                add("id");
                add("employer_id");
                add("title");
                add("description");
            }});
            put("details", new ArrayList<String>() {{
                add("company_name");
            }});
        }})
                .where("ad.is_active", Database.Condition.EQUAL, 1)
                .andWhereColumns("ad.employer_id", Database.Condition.EQUAL, "details.user_id")
                .andWhere("ad.id", Database.Condition.EQUAL, id)
                .printQueryBuilder().fetch();
        while (true) {
            try {
                if (!resultSet.next()) break;
                return Ad.newAdFromDB(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String fetchStatus(int adId, int userId) {
        ResultSet resultSet = Database.getInstance().selectComplex(new HashMap<String, ArrayList<String>>() {{
            put("status", new ArrayList<String>() {{
                add("value");
            }});
            put("ad__employee", new ArrayList<String>() {{
                add("id");
            }});
        }})
                .where("ad__employee.ad_id", Database.Condition.EQUAL, adId)
                .andWhere("ad__employee.user_id", Database.Condition.EQUAL, userId)
                .andWhereColumns("ad__employee.status", Database.Condition.EQUAL, "status.id")
                .andWhere("ad__employee.is_active", Database.Condition.EQUAL, 1)
                .printQueryBuilder().fetch();
        while (true) {
            try {
                if (!resultSet.next()) break;
                return resultSet.getString("value");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void updateStatus(int adId, int userId, int status) {
        String query = "UPDATE  ad__employee SET status=" + status +
                    " WHERE ad__employee.is_active=1 " +
                    " AND ad__employee.ad_id=" + adId +
                    " AND ad__employee.user_id=" + userId;
        Database.getInstance().sqlQuery(query).printQueryBuilder().execute();
    }

    public static void create(final int employerId, final String title, final String description) {
        Database.getInstance().insert("ad", new HashMap<String, Object>() {{
            put("employer_id", employerId);
            put("title", title);
            put("description", description);
        }}).printQueryBuilder().execute();
    }

    public static void update(int id, HashMap<String, Object> columnValueCollection) {
        Database.getInstance().update("ad", columnValueCollection)
                .where("id", Database.Condition.EQUAL, id)
                .printQueryBuilder().execute();

    }

    public static void deleteById(int id) {
        Database.getInstance().update("ad", new HashMap<String, Object>() {{
            put("is_active", 0);
        }})
                .where("id", Database.Condition.EQUAL, id)
                .andWhere("employer_id", Database.Condition.EQUAL, Auth.getAuthenticatedUser().getId())
                .printQueryBuilder().execute();
    }

    public static void apply(final int adId) {
        Database.getInstance().insert("ad__employee", new HashMap<String, Object>() {{
            put("ad_id", adId);
            put("user_id", Auth.getAuthenticatedUser().getId());
        }}).printQueryBuilder().execute();
    }

    private static Ad newAdFromDB(ResultSet resultSet) throws SQLException {
        Ad ad = new Ad(resultSet.getInt("id"),
                resultSet.getInt("employer_id"),
                resultSet.getString("company_name"),
                resultSet.getString("title"),
                resultSet.getString("description"));
        return ad;
    }

    /*public static int getCountAll() {
        ResultSet resultSet = Database.getInstance().selectCountFromString("ad, details")
                .whereString("ad.is_active=1")
                .andWhereString("ad.employer_id=details.user_id")
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
    }*/

    /*public static int getCountLike(String likeKey) {
        ResultSet resultSet = Database.getInstance().selectCountFromString("ad, details")
                .whereString("ad.is_active=1")
                .andWhereString("ad.employer_id=details.user_id")
                .andWhereString("company_name LIKE '%" + likeKey + "%'")
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
    }*/

//    --------------------------------

    private static Database countResultSet() {
        return Database.getInstance().selectCountFromString("ad, details")
                .whereString("ad.is_active=1")
                .andWhereString("ad.employer_id=details.user_id");
    }

    public static int getCount() {
        ResultSet resultSet = countResultSet().printQueryBuilder().fetch();
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

    public static int getCount(String likeKey) {
        ResultSet resultSet = countResultSet().andWhereString("company_name LIKE '%" + likeKey + "%'").printQueryBuilder().fetch();
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

    private static Database countByAuthEmployerResultSet() {
        return Database.getInstance().selectCountFromString("ad, details")
                .whereString("ad.is_active=1")
                .andWhereString("ad.employer_id=details.user_id")
                .andWhereString("employer_id=" + Auth.getAuthenticatedUser().getId());
    }

    public static int getCountByAuthEmployer() {
        ResultSet resultSet = countByAuthEmployerResultSet().printQueryBuilder().fetch();
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

    private static Database countAppliedEmployeeResultSet() {
        return Database.getInstance().selectCountFromString("ad, details")
                .whereString("ad.is_active=1")
                .andWhereString("ad.employer_id=details.user_id")
                .andWhereString("employer_id=" + Auth.getAuthenticatedUser().getId());
    }

    public static int getCountAppliedEmployee() {
        ResultSet resultSet = countByAuthEmployerResultSet().printQueryBuilder().fetch();
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

    public static ResultSet fetchAppliedEmployee() {
        return null;
    }

    public static ResultSet fetchAppliedAd() {
        return null;
    }


    public static int getCountBase(String fromString, String whereString) {
        ResultSet resultSet = Database.getInstance().selectCountFromString(fromString, whereString)
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

   /* public static ArrayList<Ad> fetchAd(String selectString, String fromString, String whereString) {
        ResultSet resultSet = Database.getInstance().selectBase(selectString, fromString, whereString)
                                                    .printQueryBuilder().fetch();
        ArrayList<Ad> adCollection = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                adCollection.add(newAdFromDB(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return adCollection;
    }*/

    public static ArrayList<Ad> fetchAdSQL(String adCollectionSQL) {
        ResultSet resultSet = Database.getInstance().sqlQuery(adCollectionSQL)
                .printQueryBuilder().fetch();
        ArrayList<Ad> adCollection = new ArrayList<>();
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

    /*public static ArrayList<Ad> fetchAd(String query) {
        ResultSet resultSet = Database.getInstance().sqlSelect(query)
                .printQueryBuilder().fetch();
        ArrayList<Ad> adCollection = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                adCollection.add(newAdFromDB(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return adCollection;
    }*/

}
