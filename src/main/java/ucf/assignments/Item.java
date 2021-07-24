package ucf.assignments;

public class Item {
    private String value;
    private String serialNumber;
    private String name;

    public Item(String value, String serialNumber, String name){
        this.value = value;
        this.serialNumber = serialNumber;
        this.name = name;
    }

    public void setValue(double value){
        String num = Double.toString(value);
        this.value = "$" + num;
    }

    public String getValue(){
        return value;
    }

    public void setSerialNumber(String serialNumber){
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber(){
        return serialNumber;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
