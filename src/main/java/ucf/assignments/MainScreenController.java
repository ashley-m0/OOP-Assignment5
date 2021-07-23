package ucf.assignments;

/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Ashley Mojica
 */


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class MainScreenController implements Initializable {

    private Map<String, Scene> scenes = new HashMap<>();
    private ArrayList<Item> mainList = new ArrayList<Item>();

    //Initialize the table and its elements
    @FXML private TableView<Item> itemTable;
    @FXML private TableColumn<Item, String> valueColumn;
    @FXML private TableColumn<Item, String> serialNumberColumn;
    @FXML private TableColumn<Item, String> nameColumn;

    public void updateTable(ArrayList<Item> currList){
        //initialize the cell values of the columns
        valueColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("value"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

        //set description column to be able to be typed in when user clicks on a cell
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //set the items in the table by calling the getAllTaskInfo function
        itemTable.setItems(getTableInfo(currList));
    }

    public ObservableList<Item> getTableInfo(ArrayList<Item> currList){
        //initialize an ObservableList that will contain the information that will be added into the table
        ObservableList<Item> listInfo = FXCollections.observableArrayList();
        //for loop that iterates from 0 to the size of the taskList
        for(int i = 0; i < mainList.size(); i++){
            //add Task to the ObservableList
            listInfo.add(mainList.get(i));
        }
        //return ObservableList
        return listInfo;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //call updateTable function
        updateTable(mainList);
        //set table to be editable and all for items to be selected
        itemTable.setEditable(true);
        itemTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }



    public void newListMenuButtonPressed(ActionEvent actionEvent) {
    }

    public void saveListButtonPressed(ActionEvent actionEvent) {
    }

    public void openFileMenuButtonPressed(ActionEvent actionEvent) {
    }

    public void addItemMenuButtonPressed(ActionEvent actionEvent) {
    }

    public void editItemMenuButtonPressed(ActionEvent actionEvent) {
    }

    public void deleteItemMenuButtonPressed(ActionEvent actionEvent) {
    }

    public void helpMenuButtonPressed(ActionEvent actionEvent) {
    }
}
