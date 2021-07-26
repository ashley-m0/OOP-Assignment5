package ucf.assignments;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Item testItem = new Item();

    @Before
    public void setUp(){
        boolean placeholder;
        placeholder = testItem.setValue("399.00");
        placeholder = testItem.setSerialNumber("AXB124AXY3");
        placeholder = testItem.setName("Xbox One");
    }

    @Test
    public void setsValueInvalid(){
        boolean actualResult = testItem.setValue("32.12345");
        assertEquals(false, actualResult);
    }

    @Test
    public void getValueCorrectly(){
        setUp();
        String expectedString = "$399.00";
        String actualString = testItem.getValue();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void setsSerialNumberInvalid(){
        boolean actualResult = testItem.setSerialNumber("hello");
        assertEquals(false, actualResult);
    }

    @Test
    public void getSNCorrectly(){
        setUp();
        String expectedString = "AXB124AXY3";
        String actualString = testItem.getSerialNumber();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void setsNameInvalid(){
        boolean actualResult = testItem.setName("a");
        assertEquals(false, actualResult);
    }

    @Test
    public void getNameCorrectly(){
        setUp();
        String expectedString = "Xbox One";
        String actualString = testItem.getName();
    }
}