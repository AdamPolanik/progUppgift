import java.util.Objects;

/**
 * Används som en nod i en graf
 */
public class Node {
    private final String name;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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
