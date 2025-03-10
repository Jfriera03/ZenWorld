package es.brouse.zenword.utils;

import java.util.Arrays;

/**
 * Clase para generar un número aleatorio basado en un array ponderado.
 * @see <a href="https://dev.to/jacktt/understanding-the-weighted-random-algorithm-581p">Dev.to</a>
 */
public class WeightedRandom {
    private final int[] weights;
    private final int[] values;
    private final int totalWeight;

    /**
     * Constructor principal de la clase utilizado para crear nuevas instancias de {@link WeightedRandom}.
     *
     * @param weights Los pesos de los valores.
     * @param values Los valores a devolver.
     */
    public WeightedRandom(int[] weights, int[] values) {
        this.weights = weights;
        this.values = values;
        this.totalWeight = Arrays.stream(weights).sum();
    }

    /**
     * Metodo para obtener un número aleatorio basado en los pesos.
     *
     * @return El numero aleatorio.
     */
    public int getRandom() {
        // Generate random number
        int random = (int) (Math.random() * totalWeight);

        // Seek index to find which area the random is in
        int index = 0;
        for (int i = 0; i < weights.length; i++) {
            index += weights[i];

            if (index >= random) return values[i];
        }

        // Should never reach this point
        return -1;
    }
}
