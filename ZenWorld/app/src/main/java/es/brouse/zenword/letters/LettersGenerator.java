package es.brouse.zenword.letters;

import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import es.brouse.zenword.R;
import es.brouse.zenword.utils.Utils;

/**
 * Clase utilizada para generar nuevos botones de letras en el juego. Esto solo
 * tendra el diseño y el estilo predeterminados.
 * <br/>
 * Los botones se generaran en un diseño circular, y el radio del
 * circulo se puede modificar actualizando el recurso <code>R.dimen.letters_circle</code>.
 */
public class LettersGenerator {
    private final int CIRCLE_IN_PX;
    private final int CONTAINER_IN_PX;
    private double angle;
    private final AppCompatActivity context;

    /**
     * Constructor de LettersGenerator, inicializa el contexto y las dimensiones de los circulos y el contenedor.
     *
     * @param context el contexto de la aplicacion.
     */
    public LettersGenerator(AppCompatActivity context) {
        this.context = context;
        CIRCLE_IN_PX = Utils.getIntPx(R.dimen.letters_circle);
        CONTAINER_IN_PX = Utils.getIntPx(R.dimen.letters_container);
    }

    /**
     * Genera todos los botones circulares que contendran las letras en el juego.
     * Se generara adjunto al diseño con el id dado.
     *
     * @param n_circles el numero de circulos que se generaran
     *
     * @return los botones generados
     */
    public List<Button> generateLetters(int n_circles) {
        final List<Button> buttons = new ArrayList<>();

        // Update the global vars
        this.angle = (double) 360 / n_circles;

        // Add the buttons to the parent layout
        for (int i = 0; i < n_circles; i++) {
            Button button = generateCircle(i);
            buttons.add(button);
        }

        return buttons;
    }

    /**
     * Metodo privado que genera el diseño para un circulo especifico.
     *
     * @param circleNumber el numero del circulo.
     * @return los parametros de diseño para el circulo.
     */
    private ConstraintLayout.LayoutParams generateLayout(int circleNumber) {
        ConstraintLayout.LayoutParams layout = new ConstraintLayout.LayoutParams(
                CIRCLE_IN_PX,
                CIRCLE_IN_PX
        );

        // Circle layout
        layout.circleRadius = CONTAINER_IN_PX / 2 - CIRCLE_IN_PX / 2 - 15;
        layout.circleConstraint = R.id.letters_container;
        layout.circleAngle = (float) (angle * circleNumber);

        // Align to parent
        layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

        return layout;
    }

    /**
     * Metodo privado que genera un circulo especifico.
     *
     * @param circleNumber el numero del circulo.
     * @return el boton que representa el circulo.
     */
    private Button generateCircle(int circleNumber) {
        // Create the button
        Button button = new Button(context);
        button.setId(View.generateViewId());

        // Set the button view props
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_shape));

        // Set the button text props
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX,Utils.getIntPx(R.dimen.letter_textSize));
        button.setTextColor(ContextCompat.getColor(context, R.color.black));

        // Assign the button layout
        button.setLayoutParams(generateLayout(circleNumber));

        return button;
    }
}
