package client.awt;

import javax.imageio.ImageIO;
import java.awt.*;
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

        java.net.URL imgURL = getResourcePath(name);
        if (imgURL != null) {
            try {
                Image image = ImageIO.read(getClass().getResource("/" + name));
                return image;
            } catch (Exception e) {
                System.err.println("Couldn't find file: " + imgURL);
            }
        }

        return null;
    }

}
