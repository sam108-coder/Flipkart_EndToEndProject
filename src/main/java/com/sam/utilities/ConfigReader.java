package com.sam.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties prop = new Properties();

    static {
        try {
            FileInputStream input = new FileInputStream(
                "src\\test\\java\\resources\\Properties\\Config.Property"
            );
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }
}
