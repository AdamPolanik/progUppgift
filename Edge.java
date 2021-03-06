/**
 * PROG2 VT2022, Inlämningsuppgift, del 1
 * Grupp 089
 * Artin Mohseni armo9784
 * Adam Polanik adan7490
 */
import java.io.Serializable;
import java.util.Objects;

public class Edge<T> implements Serializable{

    private final Object destination;
    private final String name;
    private int weight;

    public Edge(Object destination, String name, int weight) {
        this.destination = Objects.requireNonNull(destination);
        this.name = Objects.requireNonNull(name);

        if (weight < 0) {
            throw new IllegalArgumentException();
        }
        this.weight = weight;
    }

    public String getDestinationString() {
        return destination.toString();
    }

    public Object getDestination() {return destination ;}

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) throws IllegalArgumentException {
        if(weight < 0){
            throw new IllegalArgumentException();
        }else {
            this.weight = weight;
        }
    }

    public boolean equals(Object other) {
        if (other instanceof Edge edge) {
            return Objects.equals(name, edge.name) &&
                    Objects.equals(destination, edge.destination);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(name, destination);
    }

    @Override
    public String toString() {
        return
                "to " + destination +
                        " by " + name +
                        " takes " + weight;
    }
}
