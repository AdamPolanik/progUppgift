import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Place extends Canvas {

    public Place(double x, double y){
        super(x,y);
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillOval(getLayoutX(), getLayoutY(), 20, 20);
        relocate((x - 10), (y - 10));
        //nedan tillagt men funkar inte, fattar inte hur det ska gå att fixa
        //setOnMouseClicked(new ClickHandler());
    }

    //Lyssnare men tror det är pga graphicscontext som det strular
    /*class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event){
            System.out.println("Klickad");

        }

    }

     */

}
