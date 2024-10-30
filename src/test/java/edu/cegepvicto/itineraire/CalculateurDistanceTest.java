package edu.cegepvicto.itineraire;

import edu.cegepvicto.itineraire.CalculateurDistance;
import edu.cegepvicto.itineraire.Pays;
import edu.cegepvicto.itineraire.Ville;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculateurDistanceTest {

    Ville villeA;
    Ville villeB;
    Ville villeC;

    static Pays pays;

    @org.junit.jupiter.api.BeforeAll
    static void setUpClass() {
        pays = new Pays("Test");
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        villeA = new Ville("Ville A", pays, 85, 150);
        villeB = new Ville("Ville B", pays, -35, -8);
        villeC = new Ville("Ville C", pays, -1.5, 15.5);
    }

    @Test
    void distanceVolTest() {
        CalculateurDistance calculateurDistance = new CalculateurDistance();
        assertEquals(14412.0, calculateurDistance.distanceVol(villeA, villeB),  0.1, "Erreur du calcule de la distance entre la ville A et B");
        assertEquals(14412.0, calculateurDistance.distanceVol(villeB, villeA), 0.1, "Erreur du calcule de la distance entre la ville B et A");
        assertEquals(10563.0, calculateurDistance.distanceVol(villeA, villeC), 0.1, "Erreur du calcule de la distance entre la ville A et C");
        assertEquals(4448.5, calculateurDistance.distanceVol(villeC, villeB),  0.1, "Erreur du calcule de la distance entre la ville C et B");
    }
}