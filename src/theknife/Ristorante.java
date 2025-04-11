package theknife;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    public static String sep = (File.separator);
    public void dettagliRistoranti () throws IOException {
        FileWriter fr = new FileWriter("data" + sep + "restaurants.csv", true);
        try {
            fr.write("\n" + name + "," + address + "," + location + "," + price + "," + cuisine + "," + phoneNumber + "," + award + "," + greenStar + "," + facilitiesAndServices);
            fr.close();
        }

        catch(IOException e) {
            System.out.println("Errore...");
        }
    }
}