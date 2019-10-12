package sample.Controllers;

import javafx.css.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Classes.Functions;
import sample.Classes.User;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserStage implements Initializable {
    public User user;

    @FXML
    private Label userInfo, profit, last;
    @FXML
    private TextField area;

    @FXML
    private TextField priceArea;

    @FXML
    private TextField consumption;

    @FXML
    private TextField priceCunsumption;

    @FXML
    private TextField priceServices;

    double x, y;
    double totalProfit;
    LocalDate lastDate = LocalDate.EPOCH;

    /**
     * считываем данные из файла и обновляем значение текстовых полей
     */

    public void updateDataFromFile() {
        totalProfit = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/sample/Files/RecordHistory")))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] atributes = line.split(" ");
                if (user.getLogin().equals(atributes[0])) {
                    totalProfit += Double.parseDouble(atributes[2]);
                    totalProfit -= Double.parseDouble(atributes[3]);
                    totalProfit -= Double.parseDouble(atributes[4]);
                    String[] elements = atributes[1].split("\\-");
                    LocalDate lastDay = LocalDate.of(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
                    if (lastDate.compareTo(lastDay) < 0)
                        lastDate = lastDay;
                }
            }
        } catch (IOException e) {}
        userInfo.setText(user.getUserInfo());
        profit.setText(String.valueOf(Math.round(totalProfit * 100) / 100.0));
        if (!lastDate.equals(LocalDate.EPOCH)) last.setText(Functions.dateToString(lastDate, true));
    }

    /**
     * добавляем слушателей на необходимые текстовые поля
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateDataFromFile();

        area.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.matches("[0-9\\.]+$"))
                if (newValue.contains(".") && newValue.substring(newValue.indexOf(".") + 1).length() > 2)
                    area.setText(oldValue);
                else return;
            if (area.getLength() > 0) area.setText(oldValue);
        }));

        area.addEventFilter(KeyEvent.ANY, keyEvent -> {
            switch (keyEvent.getCharacter()) {
                case ".":
                    if (area.getText().contains("."))
                        keyEvent.consume();
                    if (area.getLength() == 0)
                        keyEvent.consume();
            }
        });

        priceArea.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.matches("[0-9\\.]+$"))
                if (newValue.contains(".") && newValue.substring(newValue.indexOf(".") + 1).length() > 2)
                    priceArea.setText(oldValue);
                else return;
            if (priceArea.getLength() > 0) priceArea.setText(oldValue);
        }));

        priceArea.addEventFilter(KeyEvent.ANY, keyEvent -> {
            switch (keyEvent.getCharacter()) {
                case ".":
                    if (priceArea.getText().contains("."))
                        keyEvent.consume();
                    if (priceArea.getLength() == 0)
                        keyEvent.consume();
            }
        });

        consumption.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.matches("[0-9\\.]+$"))
                if (newValue.contains(".") && newValue.substring(newValue.indexOf(".") + 1).length() > 2)
                    consumption.setText(oldValue);
                else return;
            if (consumption.getLength() > 0) consumption.setText(oldValue);
        }));

        consumption.addEventFilter(KeyEvent.ANY, keyEvent -> {
            switch (keyEvent.getCharacter()) {
                case ".":
                    if (consumption.getText().contains("."))
                        keyEvent.consume();
                    if (consumption.getLength() == 0)
                        keyEvent.consume();
            }
        });
        priceCunsumption.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.matches("[0-9\\.]+$"))
                if (newValue.contains(".") && newValue.substring(newValue.indexOf(".") + 1).length() > 2)
                    priceCunsumption.setText(oldValue);
                else return;
            if (priceCunsumption.getLength() > 0) priceCunsumption.setText(oldValue);
        }));

        priceCunsumption.addEventFilter(KeyEvent.ANY, keyEvent -> {
            switch (keyEvent.getCharacter()) {
                case ".":
                    if (priceCunsumption.getText().contains("."))
                        keyEvent.consume();
                    if (priceCunsumption.getLength() == 0)
                        keyEvent.consume();
            }
        });
        priceServices.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.matches("[0-9\\.]+$"))
                if (newValue.contains(".") && newValue.substring(newValue.indexOf(".") + 1).length() > 2)
                    priceServices.setText(oldValue);
                else return;
            if (priceServices.getLength() > 0) priceServices.setText(oldValue);
        }));

        priceServices.addEventFilter(KeyEvent.ANY, keyEvent -> {
            switch (keyEvent.getCharacter()) {
                case ".":
                    if (priceServices.getText().contains("."))
                        keyEvent.consume();
                    if (priceServices.getLength() == 0)
                        keyEvent.consume();
            }
        });
    }

    /**
     * пользователь перемещает курсор
     */

    @FXML
    void formDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        ((Stage) userInfo.getScene().getWindow()).close();
    }

    /**
     * пользователь нажал на кнопку "Отмена"
     */

    @FXML
    public void cancelAction(ActionEvent actionEvent) {
        area.clear();
        priceArea.clear();
        consumption.clear();
        priceCunsumption.clear();
        priceServices.clear();
    }

    /**
     * пользователь нажал на кнопку "Применить"
     *
     * В этом методе происходит добавление/обновление данных в файле.
     * Если пользователь сегодня уже вводил свои данные, то они обновятся.
     * Иначе, в файл добавится новая запись.
     * После нажатия на кнопку происходит обновление текстовых полей.
     */

    @FXML
    public void applyAction(ActionEvent actionEvent) throws IOException {
        String sArea, sPriceArea, sConsumption, sPriceConsumption, sPriceServices;
        if ((sArea = area.getText()).isEmpty()) sArea = "0";
        if ((sPriceArea = priceArea.getText()).isEmpty()) sPriceArea = "0";
        if ((sConsumption = consumption.getText()).isEmpty()) sConsumption = "0";
        if ((sPriceConsumption = priceCunsumption.getText()).isEmpty()) sPriceConsumption = "0";
        if ((sPriceServices = priceServices.getText()).isEmpty()) sPriceServices = "0";
        double areaProfit = Math.round(Double.parseDouble(sArea) * Double.parseDouble(sPriceArea) * 100) / 100.0;
        double consumptionExpense = Math.round(Double.parseDouble(sConsumption) * Double.parseDouble(sPriceConsumption) * 100) / 100.0;
        Functions.showAlert(Alert.AlertType.INFORMATION, "Уведомление", String.format("Доход за аренду: %s\nПотребление энергии: %s\nСуммарный расход: %s", areaProfit, consumptionExpense, consumptionExpense + Double.parseDouble(priceServices.getText())), "Итого: " + (areaProfit - consumptionExpense - Double.parseDouble(priceServices.getText())));
        if (lastDate.equals(LocalDate.now())) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/sample/Files/RecordHistory")));
            List<String> list = new ArrayList<>();
            bufferedReader.lines().forEach(line -> list.add(line));
            bufferedReader.close();
            FileWriter fileWriter = new FileWriter("src/sample/Files/RecordHistory");
            for (String line : list) {
                String[] atributes = line.split(" ");
                LocalDate now = LocalDate.now();
                if (atributes[1].equals(Functions.dateToString(now, false)) && atributes[0].equals(user.getLogin()))
                    line = String.format("%s %s %s %s %s", atributes[0], atributes[1], areaProfit, consumptionExpense, sPriceServices);
                fileWriter.append(line + "\n");
            }
            fileWriter.close();
        } else {
            FileWriter fileWriter = new FileWriter("src/sample/Files/RecordHistory", true);
            fileWriter.append(String.format("%s %s %s %s %s\n", user.getLogin(), LocalDate.now().toString(), areaProfit, consumptionExpense, sPriceServices));
            fileWriter.close();
        }
        updateDataFromFile();
    }
}