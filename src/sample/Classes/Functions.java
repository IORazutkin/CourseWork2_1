package sample.Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.TableFilters.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public final class Functions {

    /**
     * Проверяет корректность введенных в текстовое поле данных
     */

    public static boolean checkData(TextField tf) {
        if (tf.getText().isEmpty()) return false;

        if (tf.getId().equals("FIO"))
            return tf.getText().split(" ").length == 3;
        else if (tf.getId().equals("BirthDay")) {
            try {
                String date = tf.getText();
                int day = Integer.parseInt(date.substring(0, 2)),
                        month = Integer.parseInt(date.substring(3, 5)),
                        year = Integer.parseInt(date.substring(6, 10));
                LocalDate.of(year, month, day);
                return true;
            } catch (Throwable t) {
                return false;
            }
        }
        else if (tf.getId().equals("Telephone"))
            return tf.getLength() == 12;
        else if (tf.getId().equals("Mail"))
            return tf.getText().matches("^([-a-zA-Z_.0-9]+)@([a-z]+)\\.(ru|com)$");
        return true;
    }

    /**
     * Отображает всплывающее окно
     */

    public static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert errorAlert = new Alert(alertType);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }

    /**
     * конвертирует дату в текст разного формата
     */

    public static String dateToString(LocalDate date, boolean flag) {
        if (flag) return String.format("%02d.%02d.%d", date.getDayOfMonth(), date.getMonthValue(), date.getYear());
        return String.format("%d-%02d-%02d", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    /**
     * заполняет таблицу необходимыми данными
     *
     * В этом методе происходит чтение данных из файла
     * и на их основе происходит заполнение таблицы
     */

    public static void printNetProfit(TableView tableView) {
        Map<String, Double> map = new HashMap<>();
        ObservableList<NetProfit> tableData = FXCollections.observableArrayList();

        TableColumn surname = new TableColumn("Фамилия");
        surname.setSortable(false);
        TableColumn name = new TableColumn("Имя");
        name.setSortable(false);
        TableColumn patronymic = new TableColumn("Отчество");
        patronymic.setSortable(false);
        TableColumn net = new TableColumn("Чистый доход");
        net.setSortable(false);

        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        patronymic.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        net.setCellValueFactory(new PropertyValueFactory<>("net"));

        tableView.getColumns().addAll(surname, name, patronymic, net);

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/sample/Files/RecordHistory")))) {
            bufferedReader.lines().forEach(line -> {
                String[] atributes = line.split(" ");
                double currentNet = Double.parseDouble(atributes[2]) - Double.parseDouble(atributes[3]) - Double.parseDouble(atributes[4]);
                if (map.containsKey(atributes[0])) map.put(atributes[0], map.get(atributes[0]) + currentNet);
                else map.put(atributes[0], currentNet);
            });
        } catch (IOException e) {}

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/sample/Files/AllUsers")))) {
            bufferedReader.lines().forEach(line -> {
                String[] atributes = line.split(" ");
                if (atributes[0].equals("USER")) {
                    NetProfit netProfit = new NetProfit(
                            atributes[3],
                            atributes[4],
                            atributes[5],
                            map.containsKey(atributes[1]) ? map.get(atributes[1]) : 0
                    );
                    tableData.add(netProfit);
                }
            });
        } catch (IOException e) {}
        tableData.sort((o1, o2) -> o1.getSurname().compareTo(o2.getSurname()));
        tableView.setItems(tableData);
    }

    /**
     * заполняет таблицу необходимыми данными
     *
     * В этом методе происходит чтение данных из файлов
     * и на их основе происходит заполнение таблицы
     */

    public static void printProfitInRoom(TableView tableView) {
        Map<String, Double> map = new HashMap<>();
        ObservableList<ProfitRoom> tableData = FXCollections.observableArrayList();

        TableColumn room = new TableColumn("Квартира");
        room.setSortable(false);
        TableColumn users = new TableColumn("Число жильцов");
        users.setSortable(false);
        TableColumn net = new TableColumn("Чистый доход");
        net.setSortable(false);
        TableColumn average = new TableColumn("Средний доход");
        average.setSortable(false);

        room.setCellValueFactory(new PropertyValueFactory<>("room"));
        users.setCellValueFactory(new PropertyValueFactory<>("users"));
        net.setCellValueFactory(new PropertyValueFactory<>("net"));
        average.setCellValueFactory(new PropertyValueFactory<>("average"));

        tableView.getColumns().addAll(room, users, net, average);

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/sample/Files/RecordHistory")))) {
            bufferedReader.lines().forEach(line -> {
                String[] atributes = line.split(" ");
                double currentNet = Double.parseDouble(atributes[2]) - Double.parseDouble(atributes[3]) - Double.parseDouble(atributes[4]);
                if (map.containsKey(atributes[0])) map.put(atributes[0], map.get(atributes[0]) + currentNet);
                else map.put(atributes[0], currentNet);
            });
        } catch (IOException e) {}

        Map<Integer, ProfitRoom> roomMap = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/sample/Files/AllUsers")))) {
            bufferedReader.lines().forEach(line -> {
                String[] atributes = line.split(" ");
                if (map.containsKey(atributes[1])) {
                    int currentRoom = Integer.parseInt(atributes[7]);
                    if (roomMap.containsKey(currentRoom)) roomMap.get(currentRoom).addUser(map.get(atributes[1]));
                    else roomMap.put(currentRoom, new ProfitRoom(currentRoom, map.get(atributes[1])));
                }
            });
        } catch (IOException e) {}

        for (ProfitRoom profitRoom : roomMap.values()) tableData.add(profitRoom);
        tableData.sort((o1, o2) -> o1.getRoom() - o2.getRoom());
        tableView.setItems(tableData);
    }

    /**
     * заполняет таблицу необходимыми данными
     *
     * В этом методе происходит чтение данных из файлов
     * и на их основе происходит заполнение таблицы
     */

    public static void printAllUsers(TableView tableView) {
        ObservableList<AllUsers> tableData = FXCollections.observableArrayList();

        TableColumn surname = new TableColumn<>("Фамилия");
        surname.setSortable(false);
        TableColumn name = new TableColumn("Имя");
        name.setSortable(false);
        TableColumn patronymic = new TableColumn("Отчество");
        patronymic.setSortable(false);
        TableColumn birthDay = new TableColumn("Дата рождения");
        birthDay.setSortable(false);
        TableColumn room = new TableColumn("Квартира");
        room.setSortable(false);

        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        patronymic.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        birthDay.setCellValueFactory(new PropertyValueFactory<>("birthDay"));
        room.setCellValueFactory(new PropertyValueFactory<>("room"));

        tableView.getColumns().addAll(surname, name, patronymic, birthDay, room);

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("src/sample/Files/AllUsers")))) {
            bufferedReader.lines().forEach(line -> {
                if (line.startsWith("USER")) {
                    String[] atributes = line.split(" ");
                    AllUsers allUsers = new AllUsers(
                            atributes[3],
                            atributes[4],
                            atributes[5],
                            AllUsers.StringToBirthDay(atributes[6]),
                            AllUsers.StringToRoom(atributes[7])
                    );
                    tableData.add(allUsers);
                }
            });
            tableData.sort((o1, o2) -> o1.getSurname().compareTo(o2.getSurname()));
            tableView.setItems(tableData);
        } catch (IOException e) {}
    }
}
