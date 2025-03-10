package es.brouse.zenword.event;

/**
 * Clase WordCompleteEvent que representa un evento de finalización de palabra.
 */
public class WordCompleteEvent {
    private static final AppEventHandler<WordCompleteEvent> HANDLER = new AppEventHandler<>();
    private final String word;

    /**
     * Constructor del evento WordCompleteEvent, recibe una palabra.
     *
     * @param word la palabra asociada al evento.
     */
    public WordCompleteEvent(String word) {
        this.word = word;
    }

    /**
     * Método que devuelve la palabra asociada al evento.
     *
     * @return la palabra asociada al evento.
     */
    public String getWord() {
        return word;
    }

    /**
     * Método estático que devuelve el manejador de eventos para WordCompleteEvent.
     *
     * @return el manejador de eventos para WordCompleteEvent.
     */
    public static AppEventHandler<WordCompleteEvent> getHandler() {
        return HANDLER;
    }
}
