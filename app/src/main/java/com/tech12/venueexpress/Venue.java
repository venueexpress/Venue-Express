package com.tech12.venueexpress;

public class Venue {

    private String Name, Capacity,image, Price;
    public Venue()
    {

    }

    public Venue(String name, String capacity, String image,String price) {
        Name = name;
        Capacity = capacity;
        this.image = image;
        this.Price = price;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getCapacity() {
        return Capacity;
    }

    public void setCapacity(String capacity) {
        this.Capacity = capacity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
