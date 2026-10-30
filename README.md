# Conjetura de Goldbach

Verifica la conjetura de Goldbach —todo número par mayor que 2 es la suma de dos primos— para un rango de números muy grandes, usando todos los núcleos del procesador.

## Características

- Criba de Eratóstenes hasta 10 millones.
- Miller-Rabin (`BigInteger.isProbablePrime`) para complementos mayores.
- Verificación en paralelo con `ForkJoinPool`.
- Soporta números de cualquier tamaño con `BigInteger`.

## Requisitos

- JDK 21 y Maven 3.9 o superior.

## Compilar y ejecutar

```bash
mvn compile exec:java
```

## Estructura

```text
src/main/java/com/juniverse/math/
├── Main.java
└── GoldbachVerifier.java
```

## Notas

- El rango se cambia en `Main.java`; por defecto verifica 50 001 pares alrededor de 10¹⁹.
- Al terminar se informa cuántos pares se verificaron; si faltara alguno sería un contraejemplo.

## JAR ejecutable (fat jar)

```bash
mvn clean package
java -jar target/mvn-goldbach-1.0-SNAPSHOT-launcher.jar
```

El archivo `-launcher.jar` incluye todas las dependencias.

## Licencia

Código de ejemplo de uso libre con fines educativos.
