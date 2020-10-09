package com.zygne.stockalyze;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ResourceLoader {

    private static final String STYLES_FOLDER = "/styles/";
    private static final String STYLE_EXTENSION = ".css";
    private static final String LAYOUT_FOLDER = "/layout/";
    private static final String LAYOUT_EXTENSION = ".fxml";
    private static final String ID_PREFIX = "#";

    private Scene scene;

    public void setContent(String source) throws IOException {
        scene = new Scene(getView(source));
    }

    public void setStyle(String name) {
        String location = STYLES_FOLDER + formatResourceName(name, STYLE_EXTENSION);
        scene.getStylesheets().add(ResourceLoader.class.getResource(location).toExternalForm());
    }

    public Parent getView(String name) throws IOException {
        String location = LAYOUT_FOLDER + formatResourceName(name, LAYOUT_EXTENSION);
        return FXMLLoader.load(ResourceLoader.class.getResource(location));
    }

    public Node findView(String name) {
        String viewName = formatViewName(name);
        return scene.lookup(viewName);
    }


    public void inflate(Stage stage, String title) {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private String formatResourceName(String name, String extension) {
        if (name.contains(extension)) {
            return name;
        } else {
            return name + extension;
        }
    }

    private String formatViewName(String name) {
        if (name.startsWith(ID_PREFIX)) {
            return name;
        } else {
            return ID_PREFIX + name;
        }
    }
}
