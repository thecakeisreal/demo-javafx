package edu.cegepvicto.itineraire;

/**
 * Classe utilistaire de fonctions mathématiques
 */
public class MathUtil {

    /**
     * Converti un angle en degrés en un angle en radian.
     * @param degres la valeur de l'angle en degrés.
     * @return la valeur de l'angle en radian.
     */
    public static double degresEnRadian(double degres) {
        return degres * Math.PI / 180.0;
    }

}
