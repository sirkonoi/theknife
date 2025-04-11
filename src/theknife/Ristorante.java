package theknife;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    public static LinkedList<List<String>> getRistoranti() throws IOException {
        LinkedList<List<String>> restaurants = new LinkedList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("data"+ sep + "restaurants.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                restaurants.add(Arrays.asList(values));
            }
        } finally {
            if (br != null) br.close();
        }

        return restaurants;
    }

    public static boolean checkRistoranti (String name) throws IOException {
        boolean isCreated = false;
        LinkedList<List<String>> restaurants = getRistoranti();
        for (List<String> restaurant : restaurants) {
            if (restaurant.get(0).equals(name)) {
                isCreated = true;
                break;
            }
        }
        return isCreated;
    }

    public static void scriviRistorante (String name,String address, String location, String price, String cuisine, String phoneNumber, int award, String greenStar, String facilitiesAndServices) throws IOException {
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