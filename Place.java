
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Place extends Circle {

    private double x;
    private double y;

    private String name;
    public Place(double x, double y, String name){
        super(x,y, 10, Color.BLUE);
        this.name = name;
        this.x = x;
        this.y = y;
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

    public double getX() { return x; }

    public double getY() { return y; }
}
