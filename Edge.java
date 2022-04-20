import java.io.Serializable;
import java.util.Objects;

public class Edge<T> {

    private final Object destination;
    private final String name;
    private double weight;

    public Edge(Object destination, String name, double weight) {
        this.destination = Objects.requireNonNull(destination);
        this.name = Objects.requireNonNull(name);

        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException();
        }
        this.weight = weight;
    }

    public Object getDestination() {
        return destination;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
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
        return "Edge{" +
                "destination=" + destination +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
