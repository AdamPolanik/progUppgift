
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Place extends Circle {

    private String name;
    public Place(double x, double y, String name){
        super(x,y, 10, Color.BLUE);
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void rePaintNode (){
        setFill(Color.RED);
    }

    public void paintNode(){
        setFill(Color.BLUE);
    }

}
