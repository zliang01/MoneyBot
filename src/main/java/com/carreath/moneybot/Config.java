package main.java.com.carreath.moneybot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

public class Config {
    private static JSONObject config = null;

    // Open config file
    public static boolean openConfig(Main caller) {
        try {
            JSONParser parser = new JSONParser();

            ClassLoader classLoader = caller.getClass().getClassLoader();
            File file = new File(classLoader.getResource("config.json").getFile());

            config = (JSONObject) parser.parse(new FileReader(file));

            return true;
        } catch (Exception e) {
            System.out.println("Config.json is missing or unreadable");
        }
        return false;
    }

    // Get bot access token
    public static String getToken() {
        try {
            return (String) config.get("token");
        } catch (Exception e) {
            System.out.println("token cannot be read");
        }
        return "";
    }

    // Get enabled channels from config
    public static HashSet<Long> getEnabledChannels() {
        try {
            HashSet<Long> channels = new HashSet<>();
            for(Object o : (JSONArray)config.get("enabled-channels")) {
                channels.add((Long)o);
            }
            return (HashSet<Long>) channels;
        } catch (Exception e) {
            System.out.println("enabled-channels cannot be read...");
        }
        return null;
    }
}
