package Model;

public class CarSpecifications {
    long id;
    String brand;
    String model;
    String color;
    String transmission;

    @Override
    public String toString(){
        String transm;
        if (transmission.trim() == "Automatic" || transmission.trim() == "Автоматична") {
            transm = "";
        } else {
            transm = "";
        }
        return brand + " " + model + " " + color + "  " + transm;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
}
