package framework.config;

import java.util.HashMap;

public class ConfigDB {

    private static final String envName = "development";
    private static final HashMap<String, HashMap<String, String>> envList = new HashMap<String, HashMap<String, String>>() {{
        put("development", new HashMap<String, String>() {{
            put("url", "jdbc:mysql://localhost:3306/hr");
            put("user", "root");
            put("password", "");
        }});
        put("production", new HashMap<String, String>() {{
            put("url", "jdbc:mysql://192.168.0.25:3306/hr");
            put("user", "PKent");
            put("password", "pK0025lPo%4#mS");
        }});
        put("stage", new HashMap<String, String>() {{
            put("url", "jdbc:mysql://152.162.0.3:3306/hr");
            put("user", "service");
            put("password", "0552S");
        }});
    }};
//    private static HashMap<String, String> env = envList.get(envName);

    private static String getDbConfigValue(String key) {
        return envList.get(envName).get(key);
    }

    public static String getDbUrl() {
        return getDbConfigValue("url");
    }

    public static String getDbUser() {
        return getDbConfigValue("user");
    }

    public static String getDbPassword() {
        return getDbConfigValue("password");
    }

}
