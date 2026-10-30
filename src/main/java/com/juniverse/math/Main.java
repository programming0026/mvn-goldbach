package com.juniverse.math;

import java.math.BigInteger;
import java.util.List;

/**
 * Verifica la conjetura de Goldbach para un rango de pares cercano a 10¹⁹ e informa cuántos cumplieron.
 *
 * @author ajuar
 */
public class Main {

    /**
     * Punto de entrada.
     * @param args no se usan
     */
    public static void main(String[] args) {
        try {
            GoldbachVerifier verifier = new GoldbachVerifier();
            BigInteger inicio = new BigInteger("10000000000000000000");
            BigInteger fin = new BigInteger("10000000000000100000");

            List<String> resultados = verifier.validateGoldbach(inicio, fin);
            resultados.forEach(System.out::println);

            // validateGoldbach solo devuelve los pares verificados: si falta alguno sería un contraejemplo
            BigInteger esperados = fin.subtract(inicio).divide(BigInteger.TWO).add(BigInteger.ONE);
            System.out.println("Pares verificados: " + resultados.size() + " de " + esperados);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}
