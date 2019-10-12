package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Classes.Admin;
import sample.Classes.Functions;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminStage implements Initializable {
    public Admin admin;

    @FXML
    private TableView tableView;

    @FXML
    private Label adminName;
    @FXML
    private ComboBox tableFilter;

    double x, y;

    /**
     * добавляем слушателя для ComboBox
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adminName.setText("Администратор: " + admin.getFio());
        tableView.setSelectionModel(null);

        tableFilter.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tableView.getColumns().clear();
                tableView.getItems().clear();
                if (newValue.equals("Список жильцов")) Functions.printAllUsers(tableView);
                else if (newValue.equals("Доход в квартире")) Functions.printProfitInRoom(tableView);
                else if (newValue.equals("Чистый доход")) Functions.printNetProfit(tableView);
            }
        }));
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
        ((Stage) adminName.getScene().getWindow()).close();
    }
}
