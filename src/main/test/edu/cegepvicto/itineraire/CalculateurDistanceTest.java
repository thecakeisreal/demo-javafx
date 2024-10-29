package edu.cegepvicto.itineraire;

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
        assertEquals(calculateurDistance.distanceVol(villeA, villeB), 14412.0, 0.1, "Erreur du calcule de la distance entre la ville A et B");
        assertEquals(calculateurDistance.distanceVol(villeB, villeA), 14412.0, 0.1, "Erreur du calcule de la distance entre la ville B et A");
        assertEquals(calculateurDistance.distanceVol(villeA, villeC), 10563.0, 0.1, "Erreur du calcule de la distance entre la ville A et C");
        assertEquals(calculateurDistance.distanceVol(villeC, villeB), 4448.5, 0.1, "Erreur du calcule de la distance entre la ville C et B");
    }
}