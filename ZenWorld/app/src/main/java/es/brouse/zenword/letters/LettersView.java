package es.brouse.zenword.letters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import es.brouse.zenword.event.WordCompleteEvent;
import es.brouse.zenword.utils.AppColors;
import es.brouse.zenword.utils.MultiPath;
import es.brouse.zenword.R;
import es.brouse.zenword.event.ButtonPressEvent;
import es.brouse.zenword.event.ButtonReleaseEvent;
import es.brouse.zenword.event.LettersLoadEvent;
import es.brouse.zenword.utils.Utils;

/**
 * Clase LettersView que implementa la interfaz de la vista del controlador LettersController.
 */
public class LettersView implements LettersController.View {
    private final AppCompatActivity context;
    private Canvas connectionCanvas;
    private Paint canvasPaint;

    private int[] triggered;

    private List<Button> buttons;

    private final TextView viewById;

    /**
     * Constructor de LettersView, inicializa el contexto y registra los eventos.
     *
     * @param context el contexto de la aplicación.
     */
    public LettersView(AppCompatActivity context) {
        this.context = context;
        this.viewById = context.findViewById(R.id.userWord);

        LettersLoadEvent.getHandler().register(this::lettersLoadEvent);
        ButtonPressEvent.getHandler().register(this::buttonPressEvent);
        ButtonReleaseEvent.getHandler().register(this::buttonReleaseEvent);
    }

    /**
     * Método que inicializa la vista.
     */
    @Override
    public void init() {
        Bitmap bitmap = Bitmap.createBitmap(
                Utils.getIntPx(R.dimen.letters_container),
                Utils.getIntPx(R.dimen.letters_container),
                Bitmap.Config.ARGB_8888
        );

        ImageView container = context.findViewById(R.id.letters_container);
        connectionCanvas = new Canvas(bitmap);
        container.setImageBitmap(bitmap);

        canvasPaint = new Paint();
        canvasPaint.setColor(Color.BLACK);
        canvasPaint.setAntiAlias(true);
        canvasPaint.setStrokeWidth(Utils.getFloatPx(R.dimen.letters_circle) / 2);
        canvasPaint.setStyle(Paint.Style.STROKE);
        canvasPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * Método que carga los botones en la vista.
     *
     * @param nButtons el número de botones a cargar.
     * @return los botones cargados.
     */
    @Override
    public List<Button> loadButtons(int nButtons) {
        // Generate the buttons
        buttons = new LettersGenerator(context).generateLetters(nButtons);

        // Add the buttons to the parent
        final ConstraintLayout parent = context.findViewById(R.id.main_layout);
        Iterator<Button> iterator = buttons.iterator();
        while (iterator.hasNext()) {
            parent.addView(iterator.next());
        }

        triggered = new int[nButtons];

        return buttons;
    }

    /**
     * Método que cambia el color de los botones en la vista.
     */
    @Override
    public void changeColor() {
        Iterator<Button> iterator = buttons.iterator();
        while (iterator.hasNext()) {
            Button button = iterator.next();
            button.getBackground().setColorFilter(
                    AppColors.getPrimaryColor(),
                    PorterDuff.Mode.SRC_ATOP
            );
        }
        canvasPaint.setColor(AppColors.getSecondaryColor());
    }

    /**
     * Método que deshabilita o habilita los botones en la vista.
     *
     * @param b un booleano que indica si los botones deben ser deshabilitados.
     */
    @Override
    public void setDisabled(boolean b) {
        Iterator<Button> iterator = buttons.iterator();
        while (iterator.hasNext()) {
            Button button = iterator.next();
            button.setTextColor(b ? Color.GRAY : Color.BLACK);
            button.setEnabled(!b);
        }
    }

    /**
     * Método que limpia los botones en la vista.
     */
    @Override
    public void clearButtons() {
        if (buttons == null || buttons.isEmpty()) return;

        final ConstraintLayout parent = context.findViewById(R.id.main_layout);
        Iterator<Button> iterator = buttons.iterator();
        while (iterator.hasNext()) {
            parent.removeView(iterator.next());
        }

        Arrays.fill(triggered, 0);
        viewById.setText("");
    }

    /**
     * Método que establece el listener de arrastre en la vista.
     *
     * @param listener el listener de arrastre.
     */
    @Override
    @SuppressWarnings("ClickableViewAccessibility")
    public void setDragListener(View.OnTouchListener listener) {
        Iterator<Button> iterator = buttons.iterator();
        while (iterator.hasNext()) {
            iterator.next().setOnTouchListener(listener);
        }
    }

    /**
     * Método que crea las conexiones en la vista.
     *
     * @param path el camino de las conexiones.
     */
    @Override
    public void createConnections(MultiPath path) {
        path.consume((start, end) ->
                connectionCanvas.drawLine(
                        start.x, start.y,
                        end.x, end.y,
                        canvasPaint
                )
        );
    }

    /**
     * Método que limpia las conexiones en la vista.
     */
    @Override
    public void clearConnections() {
        connectionCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    /**
     * Método que maneja el evento de carga de letras.
     *
     * @param event el evento de carga de letras.
     */
    public void lettersLoadEvent(LettersLoadEvent event) {
        char[] letters = event.getLetters();

        int index = 0;
        Iterator<Button> iterator = buttons.iterator();
        while (iterator.hasNext()) {
            iterator.next().setText(String.valueOf(letters[index++]));
        }
    }

    /**
     * Método que maneja el evento de presión de botón.
     *
     * @param event el evento de presión de botón.
     */
    public void buttonPressEvent(ButtonPressEvent event) {
        int index = 0;
        Iterator<Button> iterator = buttons.iterator();
        while (iterator.hasNext()) {
            Button button = iterator.next();
            if (button.getId() == event.getButton() && triggered[index] == 0) {
                viewById.append(button.getText());
                triggered[index] = 1;
                break;
            }
            index++;
        }
    }

    /**
     * Método que maneja el evento de liberación de botón.
     *
     * @param event el evento de liberación de botón.
     */
    public void buttonReleaseEvent(ButtonReleaseEvent event) {
        Arrays.fill(triggered, 0);

        WordCompleteEvent.getHandler().handleEvent(
                new WordCompleteEvent(viewById.getText().toString())
        );

        viewById.setText("");
    }
}
