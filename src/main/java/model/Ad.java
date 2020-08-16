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

        ResultSet resultSet = Database.getInstance().selectComplex(new HashMap<String, ArrayList<String>>(){{
                                            put("ad", new ArrayList<String>(){{
                                                add("id");
                                                add("employer_id");
                                                add("title");
                                                add("description");
                                            }});
                                            put("details", new ArrayList<String>(){{
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
        ResultSet resultSet = Database.getInstance().selectComplex(new HashMap<String, ArrayList<String>>(){{
            put("ad", new ArrayList<String>(){{
                add("id");
                add("employer_id");
                add("title");
                add("description");
            }});
            put("details", new ArrayList<String>(){{
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
        ResultSet resultSet = Database.getInstance().selectComplex(new HashMap<String, ArrayList<String>>(){{
            put("status", new ArrayList<String>(){{
                add("value");
            }});
            put("ad__employee", new ArrayList<String>(){{
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

    public static void create(final int employerId, final String title, final String description) {
        Database.getInstance().insert("ad", new HashMap<String, Object>(){{
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
        Database.getInstance().update("ad", new HashMap<String, Object>(){{ put("is_active", 0); }})
                            .where("id", Database.Condition.EQUAL, id)
                            .andWhere("employer_id", Database.Condition.EQUAL, Auth.getAuthenticatedUser().getId())
                            .printQueryBuilder().execute();
    }

    public static void apply(final int adId) {
        Database.getInstance().insert("ad__employee", new HashMap<String, Object>(){{
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

    public static int getCountAll() {
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
    }

    public static int getCountLike(String likeKey) {
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
    }

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

//    ---------------------------------

    /*public static int getCountAll() throws SQLException {
        int adCount = 0;
        ResultSet resultSet = Database.getInstance().count("ad")
                                                    .where("is_active", Database.Condition.EQUAL, 1)
                                                    .printQueryBuilder().fetch();
        while (resultSet.next()) {
            adCount = resultSet.getInt("entry_count");
        }
        return adCount;
    }*/

    /*public static int getCountLike(String column, String likeValue) throws SQLException {
        int productCount = 0;
        ResultSet resultSet = Database.getInstance().count("ad")
                                                    .where("is_active", Database.Condition.EQUAL, 1)
                                                    .andWhere()
                                                    .printQuery().fetch();
        while (resultSet.next()) {
            productCount = resultSet.getInt("entry_count");
        }
        return productCount;
    }*/

}
