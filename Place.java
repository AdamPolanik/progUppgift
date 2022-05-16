import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Place extends Circle {

    private Boolean selected = false;

    public Place(double x, double y){
        super(x,y,10);
        setFill(Color.BLUE);
        setOnMouseClicked(new PlaceClickHandler());
    }

    public void selectCircle(){
        setFill(Color.RED);
    }

    public void unselectCircle(){
        setFill(Color.BLUE);
    }

    class PlaceClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent){
            if(!selected){
                selectCircle();
                selected = true;
            }
            else{
                unselectCircle();
                selected = false;
            }
        }
    }

}
