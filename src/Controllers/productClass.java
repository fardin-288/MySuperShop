package Controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

class Product {

    public int ID;
    public String name;
    public Integer quantity;
    public Integer unitCost;

    Product(String name, int quantity, int ID, int unitCost) {
        this.name = name;
        this.quantity = quantity;
        this.ID = ID;
        this.unitCost = unitCost;
    }

    public void setname(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String info() {
        return "Name : " + name + "\n" + "Quantity : " + quantity.toString() + "\n" + "UnitCost : "
                + unitCost.toString();
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return unitCost.toString(ID);
    }

};

public class productClass {

    public static List<Product> products = new ArrayList<>();

}