import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Place extends Circle {

    private boolean marked = false;

    public Place(double x, double y){
        super(x,y, 10, Color.BLUE);
        setOnMouseClicked(new ClickHandler());
    }

    class ClickHandler implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent event){
            if (!marked){
                rePaintNode();
                marked = true;
            } else {
                paintNode();
                marked =! marked;
            }
        }
    }
    
    public void rePaintNode (){
        setFill(Color.RED);
    }

    public void paintNode(){
        setFill(Color.BLUE);
    }

}
