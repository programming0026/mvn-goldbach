package com.juniverse.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * Comprueba la conjetura de Goldbach (todo par mayor que 2 es suma de dos primos).
 * Usa una criba de Eratóstenes hasta 10 millones y Miller-Rabin para complementos mayores.
 *
 * @author ajuar
 */
public class GoldbachVerifier {

    private static final int LIMIT_CRIBA = 10_000_000;
    private final boolean[] isCompound = new boolean[LIMIT_CRIBA + 1];
    private final List<Integer> primes;

    /** Construye la criba de primos hasta {@code LIMIT_CRIBA}. */
    public GoldbachVerifier() {
        this.primes = generatePrimes(LIMIT_CRIBA);
    }

    /**
     * Busca dos primos cuya suma sea {@code number}.
     * @param number número par mayor que 2
     * @return la descomposición ("n = p + q") o vacío si no es par, es menor que 4 o no se encontró
     */
    public Optional<String> verify(BigInteger number) {
        if (!number.mod(BigInteger.TWO).equals(BigInteger.ZERO)
                || number.compareTo(BigInteger.TWO) <= 0) {
            return Optional.empty();
        }

        for (int prime : primes) {
            BigInteger p = BigInteger.valueOf(prime);
            if (p.compareTo(number) > 0) {
                break;
            }

            BigInteger complement = number.subtract(p);
            // Si cabe en la criba se consulta directamente; si no, se usa Miller-Rabin
            // (antes se truncaba con intValue() y se buscaba linealmente en la lista).
            boolean complementIsPrime = complement.compareTo(BigInteger.valueOf(LIMIT_CRIBA)) <= 0
                    ? !isCompound[complement.intValue()] && complement.intValue() >= 2
                    : complement.isProbablePrime(20);
            if (complementIsPrime) {
                return Optional.of(number + " = " + p + " + " + complement);
            }
        }

        return Optional.empty();
    }

    /**
     * Verifica en paralelo todos los pares del rango.
     * @param start inicio del rango (se ajusta a par y mínimo 4)
     * @param end fin del rango (inclusive)
     * @return descomposiciones de los pares verificados; un par ausente sería un contraejemplo
     */
    public List<String> validateGoldbach(BigInteger start, BigInteger end) {
        if (start.compareTo(BigInteger.TWO) <= 0) {
            start = BigInteger.valueOf(4);
        }
        if (start.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
            start = start.add(BigInteger.ONE); // Asegura que sea par
        }

        List<BigInteger> even = new ArrayList<>();
        for (BigInteger i = start; i.compareTo(end) <= 0; i = i.add(BigInteger.TWO)) {
            even.add(i);
        }

        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        try {
            return pool.submit(()
                    -> even.parallelStream()
                            .map(this::verify)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList())
            ).get();
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace(System.err);
        } finally {
            pool.shutdown();
        }

        return Collections.emptyList();
    }

    /**
     * Criba de Eratóstenes: rellena {@code isCompound} y devuelve los primos hasta {@code limit}.
     * @param limit límite superior (inclusive)
     */
    private List<Integer> generatePrimes(int limit) {
        for (int i = 2; i * i <= limit; i++) {
            if (!isCompound[i]) {
                for (int j = i * i; j <= limit; j += i) {
                    isCompound[j] = true;
                }
            }
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (!isCompound[i]) {
                result.add(i);
            }
        }
        return result;
    }
}
