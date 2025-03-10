package es.brouse.zenword.event;

/**
 * Clase LettersLoadEvent que representa un evento de carga de letras.
 */
public class LettersLoadEvent {
    private static final AppEventHandler<LettersLoadEvent> HANDLER = new AppEventHandler<>();
    private final char[] letters;

    /**
     * Constructor del evento LettersLoadEvent, recibe una palabra y la convierte a un array de caracteres.
     *
     * @param word la palabra a convertir en un array de caracteres.
     */
    public LettersLoadEvent(String word) {
        this.letters = word.toCharArray();
    }

    /**
     * Constructor del evento LettersLoadEvent, recibe un array de caracteres.
     *
     * @param letters el array de caracteres.
     */
    public LettersLoadEvent(char[] letters) {
        this.letters = letters;
    }

    /**
     * Metodo que devuelve el array de caracteres asociados al evento.
     *
     * @return el array de caracteres.
     */
    public char[] getLetters() {
        return letters;
    }

    /**
     * Metodo estático que devuelve el manejador de eventos para LettersLoadEvent.
     *
     * @return el manejador de eventos para LettersLoadEvent.
     */
    public static AppEventHandler<LettersLoadEvent> getHandler() {
        return HANDLER;
    }
}
