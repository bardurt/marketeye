package com.zygne.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProjectProps {
    public static String readProperty(String propName){

        String propValue = "";
        try {

            Properties prop = new Properties();
            InputStream input = null;

            input = new FileInputStream("gradle.properties");

            prop.load(input);

            propValue = prop.getProperty(propName);

            System.out.println(propValue);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return propValue;
    }
}
