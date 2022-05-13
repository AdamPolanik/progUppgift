import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Place extends Canvas {

    public Place(double x, double y){
        super(x,y);
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillOval(getLayoutX(), getLayoutY(), 20, 20);
        relocate((x - 10), (y - 10));

    }

}
