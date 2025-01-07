package com.zygne.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiProps {

    public static String readApi(){

        String propValue = "";
        try {

            Properties prop = new Properties();
            InputStream input = null;

            input = new FileInputStream("api.properties");

            // load a properties file
            prop.load(input);

            propValue = prop.getProperty("api");
            // get the property value and print it out
            System.out.println(propValue);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return propValue;
    }
}
