package edu.cegepvicto.itineraire;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test les méthodes de l'utilitaire mathématique
 */
class MathUtilTest {

    @Test
    public void TestConversionDegresRadians() {
        assertEquals(Math.PI, MathUtil.degresEnRadian(180), 0.00001, "Conversion de 180°");
        assertEquals(0, MathUtil.degresEnRadian(0), 0.00001, "Conversion de 0°");
        assertEquals(2 * Math.PI, MathUtil.degresEnRadian(360), 0.00001, "Conversion de 360°");
        assertEquals(Math.PI / 4.0, MathUtil.degresEnRadian(45), 0.00001, "Conversion de 45°");
    }

}