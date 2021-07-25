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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import java.io.*;
import java.net.URL;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
        systemMessageArea.setText("List cleared.");
        return currList;
    }

    /**
     * Save List to External File
     * @param actionEvent
     */
    public void saveTXTButtonPressed(ActionEvent actionEvent) {
        //get file picked by the user
        File selectedFile = openFileChooser();
        //if the file is not null
        if(selectedFile != null){
            //call the formatListForTXT method to format list
            String message = formatListForTXT(mainList);
            //call the printInformationToTXT method to print the message to the file
            printInformationToTXT(selectedFile, message);
        }else{
            //if file is null indicate to user that the file is invalid
            systemMessageArea.setText("Invalid file.");
        }
    }

    public String formatListForTXT(ArrayList<Item> currList){
        //initialize return string
        String message = "";
        //iterate through the list of items
        for (Item item: currList){
            //add to message the item's value, serial number, and name
            message += String.format("%s\t %s\t %s\n", item.getValue(), item.getSerialNumber(), item.getName());
        }
        //return message
        return message;
    }

    public void printInformationToTXT(File outText, String message){
        try {
            //initialize FileWriter
            FileWriter myWriter = new FileWriter(outText);
            //write the message to the file
            myWriter.write(message);
            //close FileWriter
            myWriter.close();
            //indicate to user that the list was successfully written to the file
            systemMessageArea.setText("List successfully written to file.");
        } catch (IOException e) {
            //otherwise indicate to user that an error had occurred.
            systemMessageArea.setText("An error occurred.");
            e.printStackTrace();
        }
    }
    public void saveHTMLButtonPressed(ActionEvent actionEvent) {
        //get file picked by the user
        File selectedFile = openFileChooser();
        //if the file is not null
        if(selectedFile != null){
            //call the formatListForHTML method to format list to be ready to be written
            String message = formatListForHTML(mainList);
            //call the printInformationToHTML method to print message to file
            printInformationToHTML(selectedFile, message);
        }else{
            //if the file is null indicate to user that the file was invalid
            systemMessageArea.setText("Invalid file.");
        }
    }

    public String formatListForHTML(ArrayList<Item> currList){
        //initialize return string with the beginning headers of html doc
        String message = "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n" + "<meta charset=\"UTF-8\">\n" + "<title>Inventory</title>\n" +"</head>\n<body>\n<table border = '1' style = \"width: 100%\">\n";
        //iterate through items in currList
        for(Item item:currList){
            //add table row opener
            message += "<tr>\n";
            //add table data (item's value, serial number, and name)
            message += String.format("<td>%s</td>\n<td>%s</td>\n<td>%s</td>\n", item.getValue(), item.getSerialNumber(), item.getName());
            //add table row closer
            message += "</tr>\n";
        }
        //close table, body, and html doc
        message += "</table>\n</body>\n</html>\n";
        //return String
        return message;
    }

    public void printInformationToHTML(File outText, String message){
        try {
            //initialize BufferedWriter to write to outText
            BufferedWriter bw = new BufferedWriter(new FileWriter(outText));
            //write message to file
            bw.write(message);
            //close BufferedWriter
            bw.close();
            //indicate to user that the list was successfully written to the file
            systemMessageArea.setText("List successfully written to file.");
        } catch (IOException e) {
            //otherwise indicate to user that an error had occurred.
            systemMessageArea.setText("An error occurred.");
            e.printStackTrace();
        }
    }

    public void saveJSONButtonPressed(ActionEvent actionEvent) {
        //get file picked by user
        File selectedFile = openFileChooser();
        // if the file is not null
        if(selectedFile != null){
            //format list to be written to JSON file by calling formatListForJSON
            JSONArray message = formatListForJSON(mainList);
            //call printInformationToJSON method to write message to file
            printInformationToJSON(selectedFile, message);
        }else{
            //if the file is null indicate to user that the file was invalid
            systemMessageArea.setText("Invalid file.");
        }
    }

    public JSONArray formatListForJSON(ArrayList<Item> currList){
        //initialize the main JSONArray
        JSONArray main = new JSONArray();
        //iterate through all items in currList
        for(Item item: currList){
            //initialize JSONObject
            JSONObject object = new JSONObject();
            //put the item's value, serial number, and name into the object
            object.put("Value", item.getValue());
            object.put("Serial Number", item.getSerialNumber());
            object.put("Name", item.getName());
            //add the object to the array
            main.add(object);
        }
        //return the JSONArray
        return main;
    }

    public void printInformationToJSON(File outText, JSONArray list){
        try {
            //initialize the FileWriter to the outText
            FileWriter fw = new FileWriter(outText);
            //write list to file
            fw.write(list.toJSONString());
            //close FileWriter
            fw.flush();
            //indicate to user that the list was successfully written to the file
            systemMessageArea.setText("List successfully written to file.");
        } catch (IOException e) {
            //otherwise indicate to user that an error had occurred.
            systemMessageArea.setText("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Insert List from an External File
     * @param actionEvent
     */

    public void openTXTButtonPressed(ActionEvent actionEvent) {
    }

    public void openHTMLButtonPressed(ActionEvent actionEvent) {
    }

    public void openJSONButtonPressed(ActionEvent actionEvent) {
    }

    /**
     * Open Help Menu
     * @param actionEvent
     */
    public void helpMenuButtonPressed(ActionEvent actionEvent) {
    }

    /**
     * Delete Item from List
     * @param actionEvent
     */
    public void deleteItemMenuButtonPressed(ActionEvent actionEvent) {
        //get selected tasks
        ObservableList<Item> selectedItems = itemTable.getSelectionModel().getSelectedItems();
        //remove the selected tasks from the mainList
        mainList = removeItems(mainList, selectedItems);
        //update the table
        updateTable(mainList);
    }

    public ArrayList<Item> removeItems(ArrayList<Item> currList, ObservableList<Item> selectedItems){
        //iterate through all of the selected tasks
        for(Item item: selectedItems){
            //find the index of the selected task
            int index = searchBySN(currList, item.getSerialNumber());
            //remove it from the ArrayList
            currList.remove(index);
        }
        systemMessageArea.setText("Items successfully deleted.");
        //return the updated ArrayList
        return currList;
    }

    /**
     * Edit Items in List
     * @param editCell
     */
    public void valueEditCommitted(TableColumn.CellEditEvent editCell) {
        //get item that was selected
        Item itemSelected = itemTable.getSelectionModel().getSelectedItem();
        //get the new value entered by user
        String newValue = editCell.getNewValue().toString();
        //call updateValue method
        mainList = updateValue(mainList, itemSelected.getSerialNumber(), newValue);
        //updateTable
        updateTable(mainList);
    }

    public ArrayList<Item> updateValue(ArrayList<Item> currList, String serialNumber, String newValue){
        //find the index of the item that will be updated
        int index = searchBySN(currList, serialNumber);
        //In the case that the user enters in a value with a $
        char[] charArray = newValue.toCharArray();
        String valueWODollarSign = newValue;
        //if the value has a $
        if (charArray[0] == '$'){
            //call the removeDollarSign method
            valueWODollarSign = removeDollarSign(charArray);
        }
        //if the value is added correctly
        if (mainList.get(index).setValue(valueWODollarSign)){
            //indicate to user that the value was updated successfully
            systemMessageArea.setText("Value updated.");
        }else{
            //indicate to user that the value entered was not valid resulting in an unsuccessful add
            systemMessageArea.setText("Invalid value.");
        }
        //return the currList with the new updated value
        return currList;
    }

     public void serialNumberEditCommitted(TableColumn.CellEditEvent editCell) {
        //get item selected in table
        Item itemSelected = itemTable.getSelectionModel().getSelectedItem();
        //get the new serial number entered by user
        String newSerialNumber = editCell.getNewValue().toString();
        //call the updateSerialNumber method
        mainList = updateSerialNumber(mainList, itemSelected.getSerialNumber(), newSerialNumber);
        //update table
        updateTable(mainList);
    }

    public ArrayList<Item> updateSerialNumber(ArrayList<Item> currList,String originalSerialNumber, String newSerialNumber){
        //find the index of the item that will be updated
        int index = searchBySN(currList, originalSerialNumber);
        //search if there are any items with the same serial number
        boolean duplicates = findSNDuplicates(currList, newSerialNumber);
        //if there are no duplicates and the serial number is valid
        if (!duplicates && currList.get(index).setSerialNumber(newSerialNumber)){
            //indicate to user that the serial number was updated successfully
            systemMessageArea.setText("Serial Number updated.");
        }else{
            //indicate to user that the serial number entered was invalid resulting in an unsuccessful add
            systemMessageArea.setText("Invalid serial number.");
        }
        //return the currList with the updated serial number
        return currList;
    }

    public void nameEditCommitted(TableColumn.CellEditEvent editCell) {
        //get the item that was selected in the table
        Item itemSelected = itemTable.getSelectionModel().getSelectedItem();
        //get the new name entered by the user
        String newName = editCell.getNewValue().toString();
        //call the updateName method
        mainList = updateName(mainList, itemSelected.getName(), newName);
        //update table
        updateTable(mainList);
    }

    public ArrayList<Item> updateName(ArrayList<Item> currList, String originalName, String newName){
        //find the index of the item that will be updated
        int index = searchByName(currList, originalName);
        //search if there are any items with the same name
        boolean duplicates = findNameDuplicates(currList, newName);
        //if there are no duplicates and the name is valid
        if (!duplicates && currList.get(index).setName(newName)){
            //indicate to the user that the name was successfully updated
            systemMessageArea.setText("Name updated.");
        }else{
            //indicate to the user that the name entered was invalid resulting in an unsuccessful add
            systemMessageArea.setText("Invalid name.");
        }
        //return currList with updated name
        return currList;
    }

    /**
     * Add Item to List
     * @param actionEvent
     */
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

        //In the case that the user accidentally enters in a $ sign with the value
        char[] charArray = information[0].toCharArray();
        if (charArray[0] == '$'){
            information[0] = removeDollarSign(charArray);
        }

        //determine whether the information entered is correct
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
        if(findSNDuplicates(currList, information[1])){
            message += "Serial Number already exists in list. Please enter a new one.";
            correct = false;
        }
        if(findNameDuplicates(currList, information[2])){
            message += "Name already exists in list. Please enter a new one.";
            correct = false;
        }

        //if everything is correct
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

    /**
     * Search for an Item in List
     * @param actionEvent
     */

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

    /**
     * Universally Used Methods
     */
    public File openFileChooser(){
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        return selectedFile;
    }

    public int searchBySN(ArrayList<Item> currlist, String query){
        String queryCAP = query.toUpperCase(Locale.ROOT);
        //iterate through ArrayList of Items
        for(int i = 0; i < currlist.size(); i++){
            //get the element from the currList
            String element = currlist.get(i).getSerialNumber().toUpperCase(Locale.ROOT);
            //if an item's serial number matches the query
            if (element.equals(queryCAP)){
                //return the index
                return i;
            }
        }
        //if the query was not found return a -1
        return -1;
    }

    public int searchByName(ArrayList<Item> currlist, String query){
        String queryCAP = query.toUpperCase(Locale.ROOT);
        //iterate through ArrayList of Items
        for(int i = 0; i < currlist.size(); i++){
            //get the element in the currList
            String element = currlist.get(i).getName().toUpperCase(Locale.ROOT);
            //if an item's name matches the query
            if (element.equals(queryCAP)){
                //return the index
                return i;
            }
        }
        //if the query was not found return a -1
        return -1;
    }

    public boolean findSNDuplicates(ArrayList<Item>currList, String query){
        //call the searchBySN method
        int found = searchBySN(currList, query);
        //if the item was not found (no duplicate)
        if(found == -1){
            //return false (there are no duplicates)
            return false;
        }else{
            //return true (there are duplicates)
            return true;
        }
    }

    public boolean findNameDuplicates(ArrayList<Item>currList, String query){
        //call the searchByName method
        int found = searchByName(currList, query);
        //if the item was not found (no duplicate)
        if(found == -1){
            //return false (there are no duplicates)
            return false;
        }else{
            //return true (there are duplicates)
            return true;
        }
    }

    public String removeDollarSign(char[] charArray){
        char[] newArray = new char[charArray.length - 1];
        int counter = 0;
        for (char element: charArray){
            if (element != '$'){
                newArray[counter] = element;
                counter++;
            }
        }
        String newValue = String.valueOf(newArray);
        return newValue;
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
