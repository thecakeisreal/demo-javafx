package edu.cegepvicto.itineraire;

/**
 * Une ville du système. Chaque ville possède une position et un nom.
 */
public class Ville {

    /**
     * La position de la ville.
     */
    private String nom;

    /**
     * Le pays où se trouve la ville.
     */
    private Pays pays;

    /**
     * La longitude de la ville de -180° à 180°.
     */
    private double longitude;

    /**
     * La lattitude de la ville de -90° à 90°.
     */
    private double latitude;

    /**
     * Crée une nouvelle ville avec une longitude et une lattitude connue.
     * @param nom le nom de la ville.
     * @param pays le pays dans lequel se situe la ville.
     * @param latitude la latitude à laquelle se trouve la ville.
     * @param longitude la longitude à laquelle se trouve la ville.
     */
    public Ville(String nom, Pays pays, double latitude, double longitude ) {
        this.nom = nom;
        this.pays = pays;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Accesseur de la longitude d'une ville.
     * @return la longitude d'une ville en degrés.
     */
    public double getLongitude() {
        return longitude;
    }

    /***
     * Accesseur de la latitude d'une ville.
     * @return la latitude d'une ville en degrés.
     */
    public double getLatitude() {
        return latitude;
    }
}
