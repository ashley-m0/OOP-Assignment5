package ucf.assignments;

/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Ashley Mojica
 */


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
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

    //Initialize Text Fields
    @FXML private TextField valueField;
    @FXML private TextField serialNumberField;
    @FXML private TextField nameField;
    @FXML private TextField queryField;

    //Initialize Choice Box
    @FXML private ChoiceBox queryType;

    //Initialize Text Area for system messages
    @FXML private TextArea systemMessageArea;


    /**
     * Clear List
     * @param actionEvent
     */
    public void newListMenuButtonPressed(ActionEvent actionEvent) {
        //Call clearList method
        ArrayList<Item> currList = clearList(mainList);
        //Update Table
        updateTable(currList);
    }

    public ArrayList<Item> clearList(ArrayList<Item> currList){
        currList.clear();
        return currList;
    }

    public void saveListButtonPressed(ActionEvent actionEvent) {
    }

    public void openFileMenuButtonPressed(ActionEvent actionEvent) {
    }
    
    public void helpMenuButtonPressed(ActionEvent actionEvent) {
    }

    public void setMainList(ArrayList<Item> currList){
        mainList = currList;
    }

    public void deleteItemMenuButtonPressed(ActionEvent actionEvent) {
    }

    public void addItemButtonPressed(ActionEvent actionEvent) {
        //call add item method
        mainList = addItem(mainList);

        //update table
        updateTable(mainList);
    }

    public ArrayList<Item> addItem(ArrayList<Item> currList){
        //get information from fields
        String[] information = getInfoFromFields();
        //initialize a boolean variable to keep track of the correct values
        boolean correct = true;
        //initialize the new item
        Item newItem = new Item();
        //initialize the message that will be displayed to the user
        String message = "";
        if(!newItem.setValue(information[0])){
            message += "Please enter a valid value.(Only values with two or less decimal places)\n";
            correct = false;
        }
        if(!newItem.setSerialNumber(information[1])){
            message += "Please enter a valid serial number. (10 characters long, no special character)\n";
            correct = false;
        }
        if(!newItem.setName(information[2])){
            message += "Please enter a valid name. (At least 2 characters long but smaller than 256 characters long)\n";
            correct = false;
        }
        if (correct){
            //add item to currList
            currList.add(newItem);
            //indicate to user that the item was successfully added
            message += "Item successfully added.";
            //clear the text fields
            resetFields();
        }
        //set text area to the message created
        systemMessageArea.setText(message);
        //return currList
        return currList;
    }

    public String[] getInfoFromFields(){
        //initialize String array that will hold all information of new Item
        String[] information = new String[3];
        //get information from fields and place them in the array
        information[0] = valueField.getText();
        information[1] = serialNumberField.getText();
        information[2] = nameField.getText();
        //return String array
        return information;
    }


    public void searchItemButtonPressed(ActionEvent actionEvent) {
        //call the searchItem method to find whether the item was found or not
        String message = searchItem(mainList, queryType.getValue().toString(), queryField.getText());
        //set the system message to equal the message received
        systemMessageArea.setText(message);
        //reset all fields
        resetFields();
    }

    public String searchItem(ArrayList<Item> currList, String type, String query){
        //initialize search index
        int index = -1;
        //initialize return message
        String message = "";
        //if the query is by the serial number
        if (type.equals("Serial Number")){
            //call the searchBySN method
            index = searchBySN(currList, query);
        }
        //if the query is by name
        if (type.equals("Name")){
            //call the searchByName method
            index = searchByName(currList, query);
        }
        //if the query was not found
        if (index == -1){
            //indicate in message that item was not found
            message += "Item not found.";
        }else{
            //indicate in message that the item was found and print the item's information
            message += String.format("Item found:\nValue: %10s | Serial Number: %12s | Name: %s", currList.get(index).getValue(), currList.get(index).getSerialNumber(), currList.get(index).getName());
        }
        //return message
        return message;
    }

    public int searchBySN(ArrayList<Item> currlist, String query){
        //iterate through ArrayList of Items
        for(int i = 0; i < currlist.size(); i++){
            //if an item's serial number matches the query
            if (currlist.get(i).getSerialNumber().equals(query)){
                //return the index
                return i;
            }
        }
        //if the query was not found return a -1
        return -1;
    }

    public int searchByName(ArrayList<Item> currlist, String query){
        //iterate through ArrayList of Items
        for(int i = 0; i < currlist.size(); i++){
            //if an item's name matches the query
            if (currlist.get(i).getName().equals(query)){
                //return the index
                return i;
            }
        }
        //if the query was not found return a -1
        return -1;
    }

    public void setUpChoiceBox(){
        //add Serial Number and Name to the ChoiceBox options
        queryType.getItems().add("Serial Number");
        queryType.getItems().add("Name");
        //set the default choice to be Serial Number
        queryType.setValue("Serial Number");
    }

    public void resetFields(){
        //clear all TextFields
        valueField.clear();
        serialNumberField.clear();
        nameField.clear();
        queryField.clear();
        //reset ChoiceBox back to default "Serial Number"
        queryType.setValue("Serial Number");
    }

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
        //set choice box options
        setUpChoiceBox();
        //set table to be editable and all for items to be selected
        itemTable.setEditable(true);
        itemTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}
