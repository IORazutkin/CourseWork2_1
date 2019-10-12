package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.StageStyle;
import sample.Classes.Admin;
import sample.Classes.Functions;
import sample.Classes.User;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthorizationController implements Initializable {
    @FXML
    private TextField Login;
    @FXML
    private PasswordField Password;

    double x, y;

    /**
     * добавляем слушателей к необходимым полям
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Login.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.matches("[a-zA-Z0-9_]+$"))
                return;
            if (Login.getLength() > 0) Login.setText(oldValue);
        }));

        Password.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.matches("[a-zA-Z0-9]+$"))
                return;
            if (Password.getLength() > 0) Password.setText(oldValue);
        }));
    }

    /**
     * пользователь перемещает курсор
     */

    @FXML
    void formDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    /**
     * пользователь нажал курсором на форму
     */

    @FXML
    void formPressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    /**
     * пользователь нажал на кнопку "Х" - закрыть окно
     */

    @FXML
    public void closeAction(ActionEvent actionEvent) {
        ((Stage)Login.getScene().getWindow()).close();
    }

    /**
     * пользователь нажал на кнопку "Регистрация"
     */

    @FXML
    public void registrationAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../Models/Registration.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 400, 331));
        stage.setResizable(false);
        stage.setTitle("Регистрация");
        stage.show();
        stage.getIcons().add(new Image("sample/Images/icon.png"));
        closeAction(actionEvent);
    }

    /**
     * пользователь нажал на кнопку "Вход"
     *
     * В этом методе идет проверка корректности введенных данных (логина и пароля)
     * В зависимости от того, кто выполнил вход (USER или ADMIN), открывается соответствующее окно
     */

    public void enterAction(ActionEvent actionEvent) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/sample/Files/AllUsers")));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] atributes = line.split(" ");
            String  login = atributes[1],
                    password = atributes[2];
            if (Login.getText().equals(login) && Password.getText().equals(password)) {
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setResizable(false);
                if (atributes[0].equals("USER")) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../Models/UserStage.fxml"));
                    UserStage userStage = new UserStage();
                    userStage.user = new User(line);
                    loader.setController(userStage);
                    Parent root = loader.load();
                    stage.setScene(new Scene(root, 400, 435));
                    stage.setTitle("Информация о бюджете");
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../Models/AdminStage.fxml"));
                    AdminStage adminStage = new AdminStage();
                    adminStage.admin = new Admin(line);
                    loader.setController(adminStage);
                    Parent root = loader.load();
                    stage.setScene(new Scene(root, 600, 400));
                    stage.setTitle("Окно администратора");
                }
                stage.getIcons().add(new Image("sample/Images/icon.png"));
                stage.show();
                closeAction(actionEvent);
                return;
            }
        }
        bufferedReader.close();
        Functions.showAlert(Alert.AlertType.ERROR, "Ошибка", "Неверный логин/пароль", "Попробуйте повторить попытку");
    }
}