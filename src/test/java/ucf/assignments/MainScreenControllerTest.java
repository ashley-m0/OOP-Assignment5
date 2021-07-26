package ucf.assignments;

import static org.junit.jupiter.api.Assertions.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

class MainScreenControllerTest {
    private ArrayList<Item> itemList = new ArrayList<Item>();
    private MainScreenController controller = new MainScreenController();
    private TextArea systemMessageArea;

    @Before
    public void setUp(){
        boolean placeholder;
        Item testItem1 = new Item();
        placeholder= testItem1.setValue("399.00");
        placeholder= testItem1.setSerialNumber("AXB124AXY3");
        placeholder= testItem1.setName("Xbox One");
        itemList.add(testItem1);

        Item testItem2 = new Item();
        placeholder = testItem2.setValue("599.99");
        placeholder =  testItem2.setSerialNumber("S40AZBDE47");
        placeholder =  testItem2.setName("Samsung TV");
        itemList.add(testItem2);
    }

    @Test
    public void clearsListCorrectly(){
        setUp();
        ArrayList<Item> actualList = controller.clearList(itemList);
        ArrayList<Item> expectedList = new ArrayList<Item>();
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    public void formatsListForTXTFileCorrectly(){
        setUp();
        String expected = String.format("%s\t %s\t %s\n", "Value", "Serial Number", "Name");
        expected += String.format("%s\t %s\t %s\n", itemList.get(0).getValue(), itemList.get(0).getSerialNumber(), itemList.get(0).getName());
        expected += String.format("%s\t %s\t %s\n", itemList.get(1).getValue(), itemList.get(1).getSerialNumber(), itemList.get(1).getName());
        String actual = controller.formatListForTXT(itemList);
        assertEquals(expected, actual);
    }

    @Test
    public void formatsListForHTMLFileCorrectly(){
        setUp();
        String expected = "<!DOCTYPE html>\n" +"<html lang=\"en\">\n" +
                "<head>\n" + "<meta charset=\"UTF-8\">\n" +"<title>Inventory</title>\n" +
                "</head>\n" + "<body>\n" + "<table>\n" + "<td>Value</td>\n" +
                "<td>Serial Number</td>\n" + "<td>Name</td>\n" + "<tr>\n" +
                "<td>$399.00</td>\n" + "<td>AXB124AXY3</td>\n" + "<td>Xbox One</td>\n" +
                "</tr>\n" + "<tr>\n" + "<td>$599.99</td>\n" + "<td>S40AZBDE47</td>\n" +
                "<td>Samsung TV</td>\n" + "</tr>\n" + "</table>\n" + "</body>\n" + "</html>\n";
        String actual = controller.formatListForHTML(itemList);
        assertEquals(expected, actual);
    }


    @Test
    public void formatsListForJSONFileCorrectly(){
        setUp();
        String expected = "[{\"Value\":\"$399.00\",\"Serial Number\":\"AXB124AXY3\",\"Name\":\"Xbox One\"},{\"Value\":\"$599.99\",\"Serial Number\":\"S40AZBDE47\",\"Name\":\"Samsung TV\"}]";
        String actual = controller.formatListForJSON(itemList).toJSONString();
        assertEquals(expected, actual);
    }

    @Test
    public void getInfoFromTXTCorrectly(){
        setUp();
        File file = new File("ucf/assignments/testInput.txt");
        ArrayList<Item> actualList  = new ArrayList<>();
        actualList = controller.getInfoFromTXT(file, itemList);
        assertEquals("$399.00", actualList.get(0).getValue());
        assertEquals("AXB124AXY3", actualList.get(0).getSerialNumber());
        assertEquals("Xbox One", actualList.get(0).getName());
        assertEquals("$599.99", actualList.get(1).getValue());
        assertEquals("S40AZBDE47", actualList.get(1).getSerialNumber());
        assertEquals("Samsung TV", actualList.get(1).getName());
    }

    @Test
    public void sortsByValueCorrectly(){
        setUp();
        ArrayList<Item> actualList  = new ArrayList<>();
        actualList = controller.sortValues(itemList);
        assertEquals("$599.99", actualList.get(0).getValue());
        assertEquals("S40AZBDE47", actualList.get(0).getSerialNumber());
        assertEquals("Samsung TV", actualList.get(0).getName());
        assertEquals("$399.00", actualList.get(1).getValue());
        assertEquals("AXB124AXY3", actualList.get(1).getSerialNumber());
        assertEquals("Xbox One", actualList.get(1).getName());
    }

    @Test
    public void sortsBySNCorrectly(){
        setUp();
        ArrayList<Item> actualList  = new ArrayList<>();
        actualList = controller.sortSN(itemList);
        assertEquals("$399.00", actualList.get(0).getValue());
        assertEquals("AXB124AXY3", actualList.get(0).getSerialNumber());
        assertEquals("Xbox One", actualList.get(0).getName());
        assertEquals("$599.99", actualList.get(1).getValue());
        assertEquals("S40AZBDE47", actualList.get(1).getSerialNumber());
        assertEquals("Samsung TV", actualList.get(1).getName());
    }

    @Test
    public void sortsByNameCorrectly(){
        setUp();
        ArrayList<Item> actualList  = new ArrayList<>();
        actualList = controller.sortName(itemList);
        assertEquals("$599.99", actualList.get(0).getValue());
        assertEquals("S40AZBDE47", actualList.get(0).getSerialNumber());
        assertEquals("Samsung TV", actualList.get(0).getName());
        assertEquals("$399.00", actualList.get(1).getValue());
        assertEquals("AXB124AXY3", actualList.get(1).getSerialNumber());
        assertEquals("Xbox One", actualList.get(1).getName());
    }

    @Test
    public void updateValueCorrectly(){
        setUp();
        ArrayList<Item> actualList  = controller.updateValue(itemList,"S40AZBDE47", "5.99" );
        assertEquals("$5.99", actualList.get(0).getValue());
        assertEquals("S40AZBDE47", actualList.get(0).getSerialNumber());
        assertEquals("Samsung TV", actualList.get(0).getName());
       }


}