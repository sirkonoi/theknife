package theknife;

public class Ristorante {
    //campi
    private String name;
    private String address;
    private String location;
    private String price;
    private String cuisine;
    /* GUARDA DOPO
    private double Longitude;
    private double Latitude;
    */
    private String phoneNumber;
    private int award;
    private String greenStar;
    private String facilitiesAndServices;

    //costruttore
    public Ristorante(String name,String address, String location, String price, String cuisine, String greenStar, String facilitiesAndServices) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.price = price;
        this.cuisine = cuisine;
        this.greenStar = greenStar;
        this.facilitiesAndServices = facilitiesAndServices;
    }
}