package sample.Controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sample.Classes.Functions;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    @FXML
    private StackPane PersonalData;
    @FXML
    private Button default_button, cancel_button;
    @FXML
    private TextField FIO, BirthDay, Room, Telephone, Mail, login;
    @FXML
    private PasswordField password1, password2;

    double x, y;

    /**
     * добавляем слушателей ко всем полям для ввода
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FIO.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("[а-яА-Я ]+$") && FIO.getText().split(" ").length < 4)
                return;
            if (FIO.getLength() > 0) FIO.setText(oldValue);
        });

        BirthDay.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("[0-9.]+$") && BirthDay.getLength() < 11) {
                if ((BirthDay.getLength() != 3 && BirthDay.getLength() != 6) && newValue.endsWith("."))
                    BirthDay.setText(oldValue);
                if ((BirthDay.getLength() == 2 || BirthDay.getLength() == 5) && newValue.compareTo(oldValue) > 0)
                    BirthDay.setText(newValue.concat("."));
                return;
            }
            if (BirthDay.getLength() > 0) BirthDay.setText(oldValue);
        });

        Room.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("[0-9]+$") && Room.getLength() < 5)
                return;
            if (Room.getLength() > 0) Room.setText(oldValue);
        });

        Telephone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("[0-9+]+$") && Telephone.getLength() < 13) {
                if (!newValue.contains("+7"))
                    Telephone.setText("+7".concat(newValue));
                if (newValue.endsWith("+"))
                    Telephone.setText(oldValue);
                return;
            }
            if (Telephone.getLength() > 0) Telephone.setText(oldValue);
        });

        Telephone.addEventFilter(KeyEvent.ANY, keyEvenet -> {
            switch (keyEvenet.getCode()) {
                case BACK_SPACE:
                    if (Telephone.getText().equals("+7"))
                        keyEvenet.consume();
            }
        });

        Telephone.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (Telephone.getText().isEmpty())
                    Telephone.setText("+7");
                return;
            }
            if (Telephone.getText().equals("+7"))
                Telephone.setText("");
        });

        Mail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("[-a-zA-Z0-9._@]+$"))
                return;
            if (Mail.getLength() > 0) Mail.setText(oldValue);
        });

        login.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.matches("[a-zA-Z0-9_]+$"))
                return;
            if (login.getLength() > 0) login.setText(oldValue);
        }));

        password1.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.matches("[a-zA-Z0-9]+$"))
                return;
            if (password1.getLength() > 0) password1.setText(oldValue);
        }));

        password2.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.matches("[a-zA-Z0-9]+$"))
                return;
            if (password2.getLength() > 0) password2.setText(oldValue);
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
     *
     * После закрытия окна регистрации открывается окно авторизации
     */

    @FXML
    public void closeAction(ActionEvent actionEvent) throws IOException {
        ((Stage) FIO.getScene().getWindow()).close();
    }

    /**
     * пользователь нажал на кнопку "Далее" или "Регистрация"
     *
     * В этом методе происходит переход на следующий этап регистрации и сама регистрация.
     * Перед переходом на следующий этап происходит проверка корректности входных данных.
     * Перед регистрацией происходит проверка на соответствие паролей и на уникиальность логина.
     */

    @FXML
    public void nextAction(ActionEvent actionEvent) throws IOException {
        TextField[] texts = {FIO, BirthDay, Room, Telephone, Mail};
        StringBuilder error = new StringBuilder();
        if (PersonalData.getTranslateX() == 0) {
            boolean bool_next = true;
            for (TextField tf: texts) {
                if (!Functions.checkData(tf)) {
                    bool_next = false;
                    switch (tf.getId()) {
                        case "FIO":
                            error.append("ФИО: должно содержать ваше полное имя разделенное пробелами;\n");
                            break;
                        case "BirthDay":
                            error.append("День рождения: формат ДД.ММ.ГГГГ;\n");
                            break;
                        case "Room":
                            error.append("Квартира: номер квартиры;\n");
                            break;
                        case "Telephone":
                            error.append("Телефон: содержит 10 цифр, не считая +7;\n");
                            break;
                        case "Mail":
                            error.append("E-mail: (пример: igor.razutkin@list.ru);\n");
                            break;
                    }
                    tf.getStyleClass().clear();
                    tf.getStyleClass().add("log_pas-err");
                }
            }
            if (bool_next) {
                TranslateTransition move = new TranslateTransition(Duration.seconds(0.3), PersonalData);
                move.setFromX(0);
                move.setToX(-400);
                move.play();
                cancel_button.setText("НАЗАД");
                default_button.setText("РЕГИСТРАЦИЯ");
                login.requestFocus();
                for (TextField tf : texts) tf.setFocusTraversable(false);
                login.setFocusTraversable(true);
                password1.setFocusTraversable(true);
                password2.setFocusTraversable(true);
                String temp = "";
                for (String word : FIO.getText().split(" ")) {
                    word = Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
                    temp += word + " ";
                }
                FIO.setText(temp.trim());
            }
            else {
                Functions.showAlert(Alert.AlertType.ERROR, "Ошибка", error.toString(), "Все поля являются обязательными");
            }
        } else {
            if (!password1.getText().equals(password2.getText())) {
                password2.getStyleClass().clear();
                password2.getStyleClass().add("log_pas-err");
                Functions.showAlert(Alert.AlertType.ERROR, "Ошибка", "Пароли не совпадают", "Попробуйте ввести пароли снова");
                password2.requestFocus();
            } else {
                boolean isEmpty = false;
                if (password1.getText().isEmpty()) {
                    password1.getStyleClass().clear();
                    password1.getStyleClass().add("log_pas-err");
                    password2.getStyleClass().clear();
                    password2.getStyleClass().add("log_pas-err");
                    password1.requestFocus();
                    isEmpty = true;
                }
                if (login.getText().isEmpty()) {
                    login.getStyleClass().clear();
                    login.getStyleClass().add("log_pas-err");
                    login.requestFocus();
                    isEmpty = true;
                }
                if (isEmpty) {
                    Functions.showAlert(Alert.AlertType.ERROR, "Ошибка", "Все поля обязательны для заполнения", null);
                    return;
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/sample/Files/AllUsers")));
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    if (login.getText().equals(line.split(" ")[1])) {
                        login.getStyleClass().clear();
                        login.getStyleClass().add("log_pas-err");
                        Functions.showAlert(Alert.AlertType.ERROR,
                                "Ошибка", "Данный логин уже используется",
                                "Попробуйте придумать новый логин");
                        login.requestFocus();
                        return;
                    }
                bufferedReader.close();
                String[] fio = FIO.getText().split(" "),
                        birthDay = BirthDay.getText().split("\\.");
                FileWriter fileWriter = new FileWriter("src/sample/Files/AllUsers", true);
                fileWriter.append(String.format("USER %s %s %s %s %s %s %s %s %s\n", login.getText(), password1.getText(),
                        fio[0], fio[1], fio[2], birthDay[2]+"-"+birthDay[1]+"-"+birthDay[0], Room.getText(), Telephone.getText(), Mail.getText()));
                fileWriter.close();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                Parent root = FXMLLoader.load(getClass().getResource("../Models/Authorization.fxml"));
                stage.setScene(new Scene(root, 400, 222));
                stage.setResizable(false);
                stage.setTitle("Авторизация");
                stage.getIcons().add(new Image("sample/Images/icon.png"));
                stage.show();
                closeAction(null);
            }
        }
    }

    /**
     * пользователь нажал на кнопку "Отмена"
     */

    @FXML
    public void cancelAction(ActionEvent actionEvent) throws IOException {
        if (PersonalData.getTranslateX() == -400) {
            TranslateTransition move = new TranslateTransition(Duration.seconds(0.3), PersonalData);
            move.setFromX(-400);
            move.setToX(0);
            move.play();
            cancel_button.setText("ОТМЕНА");
            default_button.setText("ДАЛЕЕ");
            FIO.setFocusTraversable(true);
            BirthDay.setFocusTraversable(true);
            Room.setFocusTraversable(true);
            Telephone.setFocusTraversable(true);
            Mail.setFocusTraversable(true);
            login.setFocusTraversable(false);
            password1.setFocusTraversable(false);
            password2.setFocusTraversable(false);
        }
        else {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../Models/Authorization.fxml"));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 400, 222));
            stage.setResizable(false);
            stage.setTitle("Авторизация");
            stage.getIcons().add(new Image("sample/Images/icon.png"));
            stage.show();
            closeAction(actionEvent);
        }
    }

    /**
     * пользователь нажал на текстовое поле
     */
    @FXML
    public void textPressed(KeyEvent keyEvent) {
        if (!keyEvent.getText().isEmpty()) {
            TextField tf = (TextField) keyEvent.getSource();
            tf.getStyleClass().clear();
        }
    }
}
