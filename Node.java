/**
 * PROG2 VT2022, Inlämningsuppgift, del 1
 * Grupp 089
 * Artin Mohseni armo9784
 * Adam Polanik adan7490
 */
import java.util.Objects;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
/**
 * Används som en nod i en graf
 */
public class Node extends Circle{

    private double x;
    private double y;
    private final String name;

    public Node(double x, double y, String name){
        super(x,y, 10, Color.BLUE);
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
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

    public boolean equals(Object other) {
        if (other instanceof Node node) {
            return name.equals(node.name);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(name);
    }

    public String toString() {
        return name;
    }
}
