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
import java.lang.Integer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainScreenController implements Initializable {

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
        systemMessageArea.setText("List cleared.");
    }

    public ArrayList<Item> clearList(ArrayList<Item> currList){
        currList.clear();
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
        String message = String.format("%s\t %s\t %s\n", "Value", "Serial Number", "Name");
        //iterate through the list of items
        for (Item item: currList){
            //add to message the item's value, serial number, and name
            message += String.format("%s\t %s\t %s\n", item.getValue(), item.getSerialNumber(), item.getName());
        }
        //return message
        return message;
    }

    private void printInformationToTXT(File outText, String message){
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
        String message = "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n" + "<meta charset=\"UTF-8\">\n" + "<title>Inventory</title>\n" +"</head>\n<body>\n<table>\n";
        message += String.format("<td>%s</td>\n<td>%s</td>\n<td>%s</td>\n", "Value", "Serial Number", "Name");
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

    private void printInformationToHTML(File outText, String message){
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

    private void printInformationToJSON(File outText, JSONArray list){
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
        //get file picked by the user
        File selectedFile = openFileChooser();
        //if the file is not null
        if(selectedFile != null){
            //call the getInfoFromTXT method to get information from file
            mainList = getInfoFromTXT(selectedFile, mainList);
            //update the table with the newly entered data
            updateTable(mainList);
            //indicate to user that the items have been successfully added to the list
            systemMessageArea.setText("All items added to list");
        }else{
            //if file is null indicate to user that the file is invalid
            systemMessageArea.setText("Invalid file.");
        }
    }

    public ArrayList<Item> getInfoFromTXT(File inText, ArrayList<Item> currList){
        try {
            //initialize scanner for file
            Scanner input = new Scanner(inText);
            //scan in the first line of headers
            String garbage = input.nextLine();
            //iterate while there is a next line
            while (input.hasNextLine()){
                //initialize the array of information
                String[] information  = new String[3];
                //scan in information into array
                information[0] = input.next();
                information[1] = input.next();
                information[2] = input.nextLine();
                //add the item to the list
                currList = addItem(currList, information);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return currList;
    }

    public void openHTMLButtonPressed(ActionEvent actionEvent) {
        //get file picked by the user
        File selectedFile = openFileChooser();
        //if the file is not null
        if(selectedFile != null){
            //call the getInfoFromHTML method to get information from file
            mainList = getInfoFromHTML(selectedFile, mainList);
            //update the table with the newly entered data
            updateTable(mainList);
            //indicate to user that the items were successfully added to the list
            systemMessageArea.setText("All items added to list");
        }else{
            //if file is null indicate to user that the file is invalid
            systemMessageArea.setText("Invalid file.");
        }
    }

    public ArrayList<Item> getInfoFromHTML(File inText, ArrayList<Item> currList){
        //initialize the Document for JSoup
        Document htmlFile = null;
        try {
            htmlFile = Jsoup.parse(inText, "UTF-8", "http://example.com/" );
        } catch (IOException e) {
            e.printStackTrace();
        }
        //In the case that there are multiple tables
        Elements tables = htmlFile.select("table");
        //iterate through tables
        for(Element table: tables) {
            //get the rows of the table
            Elements rows = table.select("tr");
            //iterate through the rows
            for (int i = 1; i < rows.size(); i++) {
                //get the data
                Elements tableData = rows.get(i).select("td");
                //initialize String Array that will hold the item's information
                String[] information = new String[3];
                //iterate through data
                for (int j = 0; j < tableData.size(); j++) {
                    //add the data to the String Array
                    information[j] = tableData.get(j).text();
                }
                //add item to the list
                currList = addItem(currList, information);
            }
        }
        //return the newly updated list
        return currList;
    }

    public void openJSONButtonPressed(ActionEvent actionEvent) {
        //get file picked by the user
        File selectedFile = openFileChooser();
        //if the file is not null
        if(selectedFile != null){
            //call the getInfoFromJSON method to get the information from the file
            mainList = getInfoFromJSON(selectedFile, mainList);
            //update the table with the newly added items
            updateTable(mainList);
            //indicate to user that the items were added successfully
            systemMessageArea.setText("All items added to list");
        }else{
            //if file is null indicate to user that the file is invalid
            systemMessageArea.setText("Invalid file.");
        }
    }


    public ArrayList<Item> getInfoFromJSON(File inFile, ArrayList<Item> currList){
        //initialize JSON parser
        JSONParser parser = new JSONParser();
        try {
            //get the array of objects from the file
            JSONArray list = (JSONArray)parser.parse(new FileReader(inFile));
            //iterate through objects in array
            for(Object obj: list){
                //initialize a JSON object
                JSONObject item = (JSONObject) obj;
                //initialize the string array that will hold the item's information
                String[] information = new String[3];
                //get the information from the file and add it to the string array
                information[0] = (String) item.get("Value");
                information[1] = (String) item.get("Serial Number");
                information[2] = (String) item.get("Name");
                //add item to the list
                currList = addItem(currList, information);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        //return newly updated list
        return currList;
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
        systemMessageArea.setText("Items successfully deleted.");
    }

    public ArrayList<Item> removeItems(ArrayList<Item> currList, ObservableList<Item> selectedItems){
        //iterate through all of the selected tasks
        for(Item item: selectedItems){
            //find the index of the selected task
            int index = searchBySN(currList, item.getSerialNumber());
            //remove it from the ArrayList
            currList.remove(index);
        }
        //return the updated ArrayList
        return currList;
    }

    /**
     * Sort Items
     * @param actionEvent
     */
    public void sortValueButtonPressed(ActionEvent actionEvent) {
        //call the sortValues method
        mainList = sortValues(mainList);
        //update the table with the sorted list
        updateTable(mainList);
        //indicate to user that the items were sorted successfully
        systemMessageArea.setText("Items sorted by value successfully.");
    }

    public ArrayList<Item> sortValues(ArrayList<Item> currList){
        //initialize temporary ArrayList<Item> variable that will hold the new sorted list
        ArrayList<Item> newList = new ArrayList<Item>();
        //initialize a variable that will keep track of the current item with the next largest value
        int alphaIndex;
        //initialize a variable that keeps track of how long the original list was
        int length = currList.size();
        //for loop from 0 to the length of the original list
        for (int i = 0; i < length; i++) {
            //reset the index of the item with the highest value back to 0 each time loop iterates
            alphaIndex = 0;
            //nested for loop from 1 to the current size of the original ArrayList
            for (int j = 1; j < currList.size(); j++) {
                //remove dollar signs
                char[] currDS = currList.get(j).getValue().toCharArray();
                char[] alphaDS = currList.get(alphaIndex).getValue().toCharArray();
                Double curr = Double.valueOf(removeDollarSign(currDS));
                Double alpha = Double.valueOf(removeDollarSign(alphaDS));
                //if the value is larger than the current item at the alphaIndex
                if (Double.compare(curr, alpha) > 0) {
                    //set the alphaIndex to the current index
                    alphaIndex = j;
                }
            }
            //once the item with the highest value is found add the element to the new list and delete the item from the old list
            newList.add(currList.get(alphaIndex));
            currList.remove(alphaIndex);
        }
        //return the sorted list
        return newList;
    }
    public void sortSerialNumberButtonPressed(ActionEvent actionEvent) {
        //call the sortSN method
        mainList = sortSN(mainList);
        //update the table with the sorted list
        updateTable(mainList);
        //indicate to user that the items were sorted successfully
        systemMessageArea.setText("Items sorted by serial number successfully.");
    }

    public ArrayList<Item> sortSN(ArrayList<Item> currList){
        //initialize temporary ArrayList<Item> variable that will hold the new sorted list
        ArrayList<Item> newList = new ArrayList<Item>();
        //initialize a variable that will keep track of the current item with the next highest serial number
        int alphaIndex;
        //initialize a variable that keeps track of how long the original list was
        int length = currList.size();
        //for loop from 0 to the length of the original list
        for (int i = 0; i < length; i++) {
            //reset the index of the item with the highest serial number back to 0 each time loop iterates
            alphaIndex = 0;
            //nested for loop from 1 to the current size of the original ArrayList
            for (int j = 1; j < currList.size(); j++) {
                //if the serial number is higher than the current item at the alphaIndex
                if (currList.get(j).getSerialNumber().compareTo(currList.get(alphaIndex).getSerialNumber()) < 0) {
                    //set the alphaIndex to the current index
                    alphaIndex = j;
                }
            }
            //once the item with the highest serial number is found add the element to the new list and delete the item from the old list
            newList.add(currList.get(alphaIndex));
            currList.remove(alphaIndex);
        }
        //return the sorted list
        return newList;
    }

    public void sortNameButtonPressed(ActionEvent actionEvent) {
        //call the sortName method
        mainList = sortName(mainList);
        //update the table with the sorted list
        updateTable(mainList);
        //indicate to user that the items were sorted successfully
        systemMessageArea.setText("Items sorted by name successfully.");
    }

    public ArrayList<Item> sortName(ArrayList<Item> currList){
        //initialize temporary ArrayList<Item> variable that will hold the new sorted list
        ArrayList<Item> newList = new ArrayList<Item>();
        //initialize a variable that will keep track of the current item with the next highest name
        int alphaIndex;
        //initialize a variable that keeps track of how long the original list was
        int length = currList.size();
        //for loop from 0 to the length of the original list
        for (int i = 0; i < length; i++) {
            //reset the index of the item with the highest name back to 0 each time loop iterates
            alphaIndex = 0;
            //nested for loop from 1 to the current size of the original ArrayList
            for (int j = 1; j < currList.size(); j++) {
                //if the name is higher than the current item at the alphaIndex
                if (currList.get(j).getName().compareTo(currList.get(alphaIndex).getName()) < 0) {
                    //set the alphaIndex to the current index
                    alphaIndex = j;
                }
            }
            //once the item with the highest name is found add the element to the new list and delete the item from the old list
            newList.add(currList.get(alphaIndex));
            currList.remove(alphaIndex);
        }
        //return the sorted list
        return newList;
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
        //get information from fields
        String[] information = getInfoFromFields();
        //call add item method
        mainList = addItem(mainList, information);

        //update table
        updateTable(mainList);
    }

    public ArrayList<Item> addItem(ArrayList<Item> currList, String[] information){
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
