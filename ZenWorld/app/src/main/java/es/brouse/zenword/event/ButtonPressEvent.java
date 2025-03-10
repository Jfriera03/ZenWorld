package es.brouse.zenword.event;

/**
 * Clase ButtonPressEvent que representa un evento de presion de boton.
 */
public class ButtonPressEvent {
    private static final AppEventHandler<ButtonPressEvent> HANDLER = new AppEventHandler<>();
    private final int button;

    /**
     * Constructor del evento ButtonPressEvent, recibe un identificador de boton.
     *
     * @param id el identificador del boton.
     */
    public ButtonPressEvent(int id) {
        this.button = id;
    }

    /**
     * Metodo que devuelve el identificador del boton asociado al evento.
     *
     * @return el identificador del boton.
     */
    public int getButton() {
        return button;
    }

    /**
     * Metodo estatico que devuelve el manejador de eventos para ButtonPressEvent.
     *
     * @return el manejador de eventos para ButtonPressEvent.
     */
    public static AppEventHandler<ButtonPressEvent> getHandler() {
        return HANDLER;
    }
}
