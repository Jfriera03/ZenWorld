package es.brouse.zenword.gamescreen;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

import es.brouse.zenword.event.WordCompleteEvent;
import es.brouse.zenword.storage.PersistentStorage;
import es.brouse.zenword.utils.AppColors;
import es.brouse.zenword.R;
import es.brouse.zenword.event.LettersLoadEvent;
import es.brouse.zenword.letters.LettersController;
import es.brouse.zenword.letters.LettersView;
import es.brouse.zenword.utils.DictionaryReader;

/**
 * Clase GameScreenController que controla la lógica del juego.
 */
public class GameScreenController {
    // Utils vars
    private Map<String, String> solWords, auxWords, usedWords, dupped, hintsWords;

    private final Comparator<String> comparator = Comparator
            .comparingInt(String::length).thenComparing(Comparator.naturalOrder());
    private final DictionaryReader dictReader;
    private final LettersController lettersController;
    private final AppCompatActivity context;
    private final PersistentStorage storage;
    private final View view;

    // Game logic vars
    private int hints, solsCount, actualSolutions = -1;
    private boolean disabled = false;
    private String lastWord;

    /**
     * Interfaz que define los métodos que la vista debe implementar.
     */
    public interface View {
        void setWords(Map<String, String> words, String word, int totalWords, boolean alert);
        void createRow(int id, int numberLetters);
        void deleteRows();
        void loadWord(int id, char[] word);
        void loadChar(int id, char character, int position);
        void setHints(int hints);
        void displayToast(String text);
    }

    /**
     * Constructor del controlador GameScreenController, inicializa las variables y registra el evento WordCompleteEvent.
     *
     * @param view la vista asociada al controlador.
     * @param context el contexto de la aplicación.
     */
    public GameScreenController(View view, AppCompatActivity context) {
        this.view = view;
        this.context = context;
        this.storage = new PersistentStorage(context);
        this.dictReader = new DictionaryReader();
        this.lettersController = new LettersController(new LettersView(context), context);

        // Load the event
        WordCompleteEvent.getHandler().register(this::wordCompleteEvent);

        loadContext();
    }

    /**
     * Método que inicia el juego, prepara las palabras y carga las letras.
     */
    public void startGame() {
        auxWords = new TreeMap<>(comparator);
        solWords = new TreeMap<>(comparator);
        usedWords = new TreeMap<>(comparator);
        dupped = new TreeMap<>(comparator);
        hintsWords = new TreeMap<>(comparator); // new copy for handling hints and correct words
        solsCount = dictReader.getX(5, auxWords, solWords);

        // Copy the solution words
        Iterator<Entry<String, String>> iterator = solWords.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            dupped.put(entry.getKey(), entry.getValue());
            hintsWords.put(entry.getKey(), entry.getValue());
        }

        System.out.println(solWords);
        System.out.println(auxWords);

        // Copy the solution words
        iterator = solWords.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            dupped.put(entry.getKey(), entry.getValue());
        }

        iterator = solWords.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> solution = iterator.next();
            int hash = solution.getKey().hashCode();

            view.createRow(hash, solution.getKey().length());
            lastWord = solution.getKey();
        }

        // Load the buttons to play
        lettersController.resetButtons();
        lettersController.loadButtons(lastWord.length());
        LettersLoadEvent.getHandler().handleEvent(new LettersLoadEvent(lastWord));

        // Load the user hints
        view.setHints(hints);

        // Load the total words
        view.setWords(new HashMap<>(0), null, solsCount, false);
    }

    /**
     * Método que reinicia el juego, limpia las palabras y carga nuevas letras.
     */
    public void restartGame() {
        disabled = false;
        lettersController.setDisabled(false);
        view.deleteRows();
        AppColors.changeColor();
        startGame();
    }

    /**
     * Método que mezcla las letras.
     */
    public void shuffleLetters() {
        if (disabled) return;

        lettersController.shuffleButtons();
        LettersLoadEvent.getHandler().handleEvent(new LettersLoadEvent(lastWord));
    }

    /**
     * Método que muestra las pistas disponibles.
     */
    public void showHints() {
        // Don't know where to put this, for not here :/
        // Create the toast and display it
        view.setWords(usedWords, null, solsCount, true);
    }

    /**
     * Método que muestra una ayuda, revela la primera letra de una palabra no encontrada.
     */
    public void showHelp() {
        if (disabled) return;

        // Check if all hints have been shown
        if (hintsWords.isEmpty()) {
            view.displayToast(context.getString(R.string.no_more_words_hints));
            return;
        }

        if (hints < 5) {
            view.displayToast(context.getString(R.string.hint_no_hints));
            return;
        }

        // Update the words
        hints -= 5;
        view.setHints(hints);
        storeContext();

        // Convert the key set into an array
        String[] keys = hintsWords.keySet().toArray(new String[0]);

        // Get a random key from the key set
        String randomKey = keys[new Random().nextInt(keys.length)];

        // Load the character
        view.loadChar(randomKey.hashCode(), hintsWords.get(randomKey).charAt(0), 0);
        hintsWords.remove(randomKey);
    }

    /**
     * Método que se ejecuta cuando se completa una palabra, verifica si la palabra está en las soluciones o palabras auxiliares.
     *
     * @param event el evento de finalización de palabra.
     */
    public void wordCompleteEvent(WordCompleteEvent event) {
        String completeWord;
        String word = event.getWord();

        // Check if the words was found previously
        String foundWord = usedWords.get(word);
        if (foundWord != null) {
            view.setWords(usedWords, foundWord, solsCount, false);
            return;
        }

        // Could be more sophisticated check, but works here
        hintsWords.remove(word); // remove from hintsWords instead of dupped

        // Check if the word is in the solution or the auxiliary words
        if ((completeWord = solWords.remove(word)) != null) {
            foundWord(word, completeWord, true);
            return;
        } else if (( completeWord = auxWords.remove(word)) != null) {
            foundWord(word, completeWord, false);
            return;
        }

        // The word was not found
        view.displayToast(String.format(
                context.getString(R.string.word_not_found),
                word
        ));
    }

    /**
     * Método privado que se ejecuta cuando se encuentra una palabra, actualiza las pistas y las palabras encontradas.
     *
     * @param key la palabra encontrada.
     * @param word la palabra completa.
     * @param isSolution indica si la palabra es una solución.
     */
    private void foundWord(String key, String word, boolean isSolution) {
        // Create the toast and display it
        view.displayToast(context.getString(
                isSolution ? R.string.word_found : R.string.word_found_hidden
        ));

        // Update the hints and the found words
        usedWords.put(key, word);
        hints += isSolution ? 0 : 1;
        view.setHints(hints);
        actualSolutions -= isSolution ? 1 : 0;
        storeContext();

        // Load the words used
        view.setWords(usedWords, null, solsCount, false);

        // Load the word in the view
        if (isSolution) {
            view.loadWord(key.hashCode(), word.toCharArray());
        }

        // Check if the game is complete
        if (solWords.isEmpty()) {
            view.displayToast(context.getString(R.string.game_complete));
            lettersController.setDisabled(true);
            disabled = true;
        }
    }

    /**
     * Método privado que carga el contexto desde el almacenamiento persistente.
     */
    private void loadContext() {
        hints = storage.getInt("zenWord:hints");
    }

    /**
     * Método privado que guarda el contexto en el almacenamiento persistente.
     */
    private void storeContext() {
        storage.setInt("zenWord:hints", hints);
    }
}
