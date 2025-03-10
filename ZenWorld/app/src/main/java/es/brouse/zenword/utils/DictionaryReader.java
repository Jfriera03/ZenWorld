package es.brouse.zenword.utils;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import es.brouse.zenword.R;

/**
 * Clase DictionaryReader que proporciona métodos para leer un diccionario de palabras y generar palabras aleatorias.
 */
public class DictionaryReader {
    /**
     * Etiqueta para los registros de depuración.
     */
    private static final String TAG = "DictionaryReader";
    /**
     * Objeto para generar números aleatorios ponderados.
     */
    private final WeightedRandom weightedRandom;
    /**
     * Mapa para almacenar las palabras del diccionario agrupadas por longitud.
     */
    private static final Map<Integer, Set<String>> map = new HashMap<>(7);
    /**
     * Array para almacenar la cantidad de palabras de cada longitud.
     */
    private static final int[] sizes = new int[7];

    /**
     * Constructor de DictionaryReader, inicializa el generador de números aleatorios ponderados.
     */
    public DictionaryReader() {
        this.weightedRandom = new WeightedRandom(
                new int[] {10, 25, 40, 15, 10},
                new int[] {3, 4, 5, 6, 7}
        );
    }

    /**
     * Método estático para leer el diccionario desde un recurso y almacenarlo en el mapa.
     *
     * @param context el contexto de la aplicación.
     */
    public static void readDictionary(AppCompatActivity context) {
        String strCurrentLine;
        InputStream inputStream = context.getResources().openRawResource(R.raw.paraules);
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

        int skip = 0;

        try {
            while ((strCurrentLine = r.readLine()) != null) {
                String[] split = strCurrentLine.split(";");
                int length = split[0].length();

                if (length > 7 || length < 3) {
                    skip++;
                    continue;
                }

                Set<String> strings = map.get(length);
                if (strings == null) {
                    strings = new TreeSet<>();
                    map.put(length, strings);
                }

                strings.add(strCurrentLine);
                sizes[length - 1]++;
            }

            Log.i("DictionaryReader", "Skipped " + skip + " words");

            inputStream.close();
            r.close();
        } catch (Exception e) {
            Log.e("DictionaryReader", "Error reading dictionary");
        }
    }

    /**
     * Método para generar una cantidad de palabras intermedias y una palabra solución.
     *
     * @param number el número de palabras a generar.
     * @param aux un mapa para almacenar las palabras intermedias.
     * @param sol un mapa para almacenar la palabra solución.
     * @return el número de palabras generadas.
     */
    public int getX(int number, Map<String, String> aux, Map<String, String> sol) {
        int maxLenght = weightedRandom.getRandom();
        int remainder = number - 1;
        int sols = 1;

        // We load the solution word
        String solution = generateSolutionWord(maxLenght);
        assert solution != null;
        String[] split = solution.split(";");
        sol.put(split[1], split[0]);

        // Load all the intermediate strings
        for (int length = maxLenght - 1; length > 3; length--) {
            sols += generateDuplicates(length, sol, aux, 1, solution);
            remainder--;
        }

        sols += generateDuplicates(3, sol, aux, remainder, solution);

        return sols;
    }

    /**
     * Método privado para generar la palabra solución.
     *
     * @param length la longitud de la palabra a generar.
     * @return la palabra generada.
     */
    private String generateSolutionWord(int length) {
        long current = System.currentTimeMillis();
        Set<String> strings = map.get(length);

        if (strings == null) return null;
        int wordIndex = (int) Math.ceil(Math.random() * sizes[length - 1]);

        // We generate a random index to get the word from the map
        int index = 0;
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            String string = iterator.next();
            if (wordIndex > index++) continue;

            // We have found the first solution word
            Log.d(TAG, String.format(
                            "Time to generate solution word: %dms",
                            System.currentTimeMillis() - current
                    )
            );
            return string;
        }
        // Should not happen
        return null;
    }

    /**
     * Método privado para generar palabras intermedias.
     *
     * @param length la longitud de las palabras a generar.
     * @param sol un mapa para almacenar las palabras solución.
     * @param aux un mapa para almacenar las palabras intermedias.
     * @param max el número máximo de palabras a generar.
     * @param solution la palabra solución.
     * @return el número de palabras generadas.
     */
    private int generateDuplicates(int length, Map<String, String> sol, Map<String, String> aux,
                                   int max, String solution) {
        String[] split;
        int sols = 0;
        int solIndex = 0;
        long current = System.currentTimeMillis();

        // Get the set with the length of the word
        Set<String> strings = map.get(length);
        if (strings == null) return 0;

        // We generate a random index to get the word from the map
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            String string = iterator.next();
            if (!isSolutionWord(solution, string)) continue;

            split = string.split(";");

            // As we are requests to return more than one strings
            // If we find any similar word, we add first to the array
            if (solIndex >= max) {
                aux.put(split[1], split[0]);
                sols++;
            } else if (sol.put(split[1], split[0]) == null) {
                solIndex++;
                sols++;
            }
        }

        Log.d(TAG, String.format(
                        "Time to generate solution word %d: %dms",
                        length,
                        System.currentTimeMillis() - current
                )
        );
        return sols;
    }

    /**
     * Método para verificar si una palabra puede formarse a partir de otra.
     *
     * @param word1 la primera palabra.
     * @param word2 la segunda palabra.
     * @return un booleano que indica si la segunda palabra puede formarse a partir de la primera.
     */
    public boolean isSolutionWord(String word1, String word2) {
        int[] letterCounts = new int[26];

        // We add all the chars of the first word to the array
        for (int length = word1.length(); length > 0; length--) {
            if (word1.charAt(length - 1) == ';') break;

            letterCounts[word1.charAt(length - 1) - 'a']++;
        }

        // We check if the second word can be formed using the first word
        for (int length = word2.length(); length > 0; length--) {
            if (word2.charAt(length - 1) == ';') break;

            int index = word2.charAt(length - 1) - 'a';
            if (letterCounts[index] == 0) return false;
            letterCounts[index]--;
        }

        return true;
    }
}
