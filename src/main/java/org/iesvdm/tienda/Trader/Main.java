package org.iesvdm.tienda.Trader;

import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Trader raoul = new Trader("Raul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");
        List<Transaction> transactions = Arrays.asList(new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000), new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710), new Transaction(mario, 2012, 700), new Transaction(alan, 2012, 950));

        // 1 Encuentre todas las transacciones del año 2011 y ordénelas por valor (menor a mayor).

        List<Transaction> tr2011 = transactions.stream()
                .filter(transaction -> transaction.getAnio() == 2011)
                .sorted(comparing(Transaction::getValor, reverseOrder())
                    .thenComparing(transaction -> transaction.getTrader().getCiudad()))
                .collect(toList());

        // 2 ¿Cuáles son todas las ciudades (sin repetición) donde trabajan los traders?

        List<String> cities = transactions.stream()
                .map(transaction -> transaction.getTrader().getCiudad())
                .distinct()
                .collect(toList());

        // O mediante toSet
        Set<String> ciudades = transactions.stream()
                .map(transaction -> transaction.getTrader().getCiudad())
                .collect(toSet());

        // 3 Encuentre todos los traders de Cambridge y ordénelos por nombre.
        List<Trader> traders = transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCiudad().equals("Cambridge"))
                .distinct()
                .sorted(comparing(Trader::getNombre))
                .collect(toList());
        // 4 Devuelva los nombres de todos los traders ordenados alfabéticamente en una
        // sola cadena

        String tradersStr = transactions.stream()
                .map(transaction -> transaction.getTrader().getNombre())
                .distinct()
                .sorted()
                .reduce("", (n1, n2) -> n1 + n2);

        // O mediante joining
        tradersStr = transactions.stream()
                .map(transaction -> transaction.getTrader().getNombre())
                .distinct()
                .sorted()
                .collect(joining());

        // 5 ¿Hay traders con sede en Milán? Sí o no
        boolean milanBased = transactions.stream()
                .anyMatch(transaction
                        -> transaction.getTrader().getCiudad().equals("Milan"));

        // 6 Imprime los valores de todas las transacciones de los traders que viven en
        // Cambridge.
        transactions.stream()
                .filter(t -> "Cambridge".equals(t.getTrader().getCiudad()))
                .map(Transaction::getValor)
                .forEach(System.out::println);

        // 7 ¿Cuál es el valor más alto de todas las transacciones?
        Optional<Integer> highestValue = transactions.stream()
                .map(Transaction::getValor)
                .reduce(Integer::max);

        // 8 Encuentra la transacción con el valor más pequeño.
        Optional<Transaction> smallestTransaction =  transactions.stream()
                .reduce((t1, t2) -> 	 t1.getValor() < t2.getValor() ? t1 : t2);

        smallestTransaction = transactions.stream()
                .min(comparing(Transaction::getValor));

    }
}

