package edu.cegepvicto.itineraire;

/**
 * Calcule la distance entre deux villes
 */
public class CalculateurDistance {

    /**
     * Calcule la distance à vol d'oiseau en kilomètres à partir de la formule de haversine
     *
     * @param depart  la ville de départ du trajet.
     * @param arrivee la ville d'arrivée du trajet.
     * @return la distance en kilomètres entre les deux villes.
     */
    public double distanceVol(Ville depart, Ville arrivee) {
        // Utilisation de la formule de haversine
        // Formule de haversine. (2024, février 25). Wikipédia, l'encyclopédie libre. Page consultée le 23:18,
        // février 25, 2024 à partir de http://fr.wikipedia.org/w/index.php?title=Formule_de_haversine&oldid=212808628.

        double diffLatitude = arrivee.getLatitude() - depart.getLatitude();
        double diffLongitude = arrivee.getLongitude() - depart.getLongitude();

        double facteurLatitude = Math.sin(degresEnRadian(diffLatitude * 0.5));
        double facteurLongitude = Math.sin(degresEnRadian(diffLongitude * 0.5));

        double RAYON_TERRE = 6371;
        return 2.0 * RAYON_TERRE * Math.asin(Math.sqrt(facteurLatitude * facteurLatitude +
                Math.cos(degresEnRadian(depart.getLatitude() * Math.cos(degresEnRadian(arrivee.getLatitude())) * facteurLongitude))));
    }

    /**
     * Converti un angle en degrés en un angle en radian.
     * @param degres la valeur de l'angle en degrés.
     * @return la valeur de l'angle en radian.
     */
    private double degresEnRadian(double degres) {
        return degres * Math.PI / 180.0;
    }
}
