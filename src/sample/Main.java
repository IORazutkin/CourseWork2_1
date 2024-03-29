package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("Models/Authorization.fxml"));
        primaryStage.setScene(new Scene(root, 400, 222));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Авторизация");
        primaryStage.getIcons().add(new Image("sample/Images/icon.png"));
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}