package ucf.assignments;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item {
    private String value;
    private String serialNumber;
    private String name;

    public boolean setValue(String value){
        //if the value fits the requirements
        if (authenticateValue(value)){
            //Format the String to ensure that it has decimal values
            Double valueDouble = Double.valueOf(value);
            //Assign the String with a "$" in front
            this.value = String.format("$%.2f", valueDouble);
            //return true to indicate that the value was added successfully
            return true;
        }else{
            //return false to indicate an incorrect value
            return false;
        }
    }

    private boolean authenticateValue(String value){
        Double num = Double.valueOf(value);
        if (BigDecimal.valueOf(num).scale() > 2){
            return false;
        }else{
            return true;
        }
    }

    public String getValue(){
        return value;
    }

    public boolean setSerialNumber(String serialNumber){
        //if serial number fits the requirements
        if(authenticateSerialNumber(serialNumber)){
            //assign serial number
            this.serialNumber = serialNumber;
            //return true to indicate that the serialNumber was added successfully
            return true;
        }else{
            //returns false to indicate an incorrect serial number
            return false;
        }

    }

    private boolean authenticateSerialNumber(String serialNumber){
        if(correctLength(serialNumber) && !specialCharactersPresent(serialNumber)){
            return true;
        }else{
            return false;
        }
    }

    private boolean correctLength(String serialNumber){
        //transform string into a character array
        char[] numbers = serialNumber.toCharArray();
        //if the array's length is longer than 10 return false and return true otherwise
        if (numbers.length != 10){
            return false;
        }else{
            return true;
        }
    }

    private boolean specialCharactersPresent(String serialNumber){
        //initialize pattern to see if any special characters were entered
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        //initialize matcher to the string
        Matcher m = p.matcher(serialNumber);
        //returns true if there is a special character in the string; false otherwise
        return m.find();
    }

    public String getSerialNumber(){
        return serialNumber;
    }

    public boolean setName(String name){
        //if name fits the requirements
        if(authenticateName(name)){
            //assign name
            this.name = name;
            //return true to indicate that the name was added successfully
            return true;
        }else{
            //return false to indicate an incorrect name
            return false;
        }

    }

    private boolean authenticateName(String name){
        char[] letters = name.toCharArray();
        if(letters.length >= 2 && letters.length <= 256){
            return true;
        }else{
            return false;
        }
    }

    public String getName(){
        return name;
    }





}
