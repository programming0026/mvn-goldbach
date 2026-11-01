package com.juniverse.math;

import java.math.BigInteger;
import java.util.List;

import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * Verifica la conjetura de Goldbach para un rango de pares cercano a 10¹⁹ e informa cuántos cumplieron.
 *
 * @author ajuar
 */
public class Main {
    private static final Logger LOGGER = AppLogger.get(Main.class);


    /**
     * Punto de entrada.
     * @param args no se usan
     */
    public static void main(String[] args) {
        try {
            GoldbachVerifier verifier = new GoldbachVerifier();
            BigInteger start = new BigInteger("10000000000000000000");
            BigInteger fin = new BigInteger("10000000000000100000");

            List<String> results = verifier.validateGoldbach(start, fin);
            results.forEach(System.out::println);

            // validateGoldbach solo devuelve los pares verificados: si falta alguno sería un contraejemplo
            BigInteger expected = fin.subtract(start).divide(BigInteger.TWO).add(BigInteger.ONE);
            LOGGER.info("Pares verificados: " + results.size() + " de " + expected);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
