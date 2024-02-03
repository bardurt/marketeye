package com.zygne.client.awt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {


    private URL getResourcePath(String path) {

        java.net.URL url = getClass().getResource("/" + path);
        if (url != null) {
            return url;
        } else {
            System.err.println("Couldn't load resource: " + path);
            return null;
        }


    }

    public Image loadImage(String name) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = null;

        try {
            in = classLoader.getResourceAsStream(name);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            in.transferTo(out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (in != null) {
            try {
                return ImageIO.read(getClass().getResource("/" + name));
            } catch (Exception e) {
                System.err.println("Couldn't find file: " + name);
            }
        } else {
            System.err.println("Input stream for " + name + " is null");
        }

        return null;
    }

}
