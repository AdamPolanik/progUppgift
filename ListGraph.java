import java.io.Serializable;
import java.util.*;

/**
 * Oriktad graf
 */
public class ListGraph<T> implements Graph,Serializable{

    //Static för att testa metoden getNodes()
    private static final Map<Object, Set<Edge>> nodes = new HashMap<>();

    public void add(Object node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    //Lägg till remove metod här!
    //Inte testat!
    public void remove(Object node) throws NoSuchElementException {
        if (!nodes.containsKey(node)){
            throw new NoSuchElementException("Non existing node!");
        } else {
            nodes.remove(node).remove(getEdgesFrom(node).remove(getEdgeBetween(node, node))); //Osäker på om detta funkar
        }
    }

    //Lägg till funktionalitet i denna (Exceptions och felkontroller)
    public void connect(Object a, Object b, String name, int weight) {
        add(a);
        add(b);

        Set<Edge> aEdges = nodes.get(a);
        Set<Edge> bEdges = nodes.get(b);

        aEdges.add(new Edge(b, name, weight));
        bEdges.add(new Edge(a, name, weight));
    }

    //Lägg till en disconnect metod här!
    //Inte testad!
    public void disconnect(Object a, Object b) throws NoSuchElementException, IllegalStateException{
        if (!nodes.containsKey(a) || !nodes.containsKey(b)){
            throw new NoSuchElementException("Non existing node!");
        } else if (nodes.containsKey(a) && nodes.containsKey(b) && getEdgeBetween(a, b) == null){
            throw new IllegalStateException("Non existing edge between given nodes!");
        } else {
            nodes.remove(getEdgeBetween(a, b));
        }
    }

    //Lägg till setConnectionWeight metod här!
    //Klar med metod, ej testad!
    @Override
    public void setConnectionWeight(Object node1, Object node2, int weight) throws NoSuchElementException, IllegalArgumentException{
        if (weight < 0){
            throw new IllegalArgumentException("Negative weight not allowed!");
        } else if (!nodes.containsKey(node1) || !nodes.containsKey(node2)){
            throw new NoSuchElementException("Node missing in graph!");
        } else if (getEdgesFrom(node1) == null || getEdgesFrom(node2) == null){
            throw new NoSuchElementException("No edge connection between nodes!");
        } else {
            getEdgeBetween(node1, node2).setWeight(weight);
        }
    }

    //Lägg till getNodes metod här!
    //Gör static för att testa metoden!
    public Set<Edge> getNodes() {
        Set<Edge> nodeCopy = new HashSet<>();
//        Map<Object, Set<Edge>> nodeCopy = new HashMap<>();
        for (Object n : nodes.keySet()){
            nodeCopy.addAll(nodes.get(n));
//            nodeCopy.put(n, nodes.get(n));
        }
        // System.out.println("Kopia: " + nodeCopy); //Test sout
        return nodeCopy;
    }

    //Lägg till getEdgesFrom metod Här! (verkar fungera korrekt)
    public Set<Edge> getEdgesFrom(Object node) throws NoSuchElementException{
        if(!nodes.containsKey(node)){
            throw new NoSuchElementException();
        }else {
            Set<Edge> tempSet = new HashSet<>();
            tempSet.addAll(nodes.get(node));
            return tempSet;
        }
    }

    public boolean pathExists(Object a, Object b) {
        Set<Object> visited = new HashSet<>();
        depthFirstVisitAll(a, visited);
        return visited.contains(b);
    }

    @Override
    public List<Edge> getPath(Object from, Object to) {
        Map<Object, Object> connection = new HashMap<>();
        depthFirstConnection(from, null, connection);
        if (!connection.containsKey(to)) {
            return Collections.emptyList();
        }
        return gatherPath(from, to, connection);
    }

    public List<Edge> getShortestPath(Object from, Object to) {
        Map<Object, Object> connections = new HashMap<>();
        connections.put(from, null);

        LinkedList<Object> queue = new LinkedList<>();
        queue.add(from);
        while (!queue.isEmpty()) {
            Object node = queue.pollFirst();
            for (Edge edge : nodes.get(node)) {
                Object destination = edge.getDestination();
                if (!connections.containsKey(destination)) {
                    connections.put(destination, node);
                    queue.add(destination);
                }
            }
        }

        if (!connections.containsKey(to)) {
            throw new IllegalStateException("no connection");
        }
        return gatherPath(from, to, connections);
    }

    private List<Edge> gatherPath(Object from, Object to, Map<Object, Object> connection) {
        LinkedList<Edge> path = new LinkedList<>();
        Object current = to;
        while (!current.equals(from)) {
            Object next = connection.get(current);
            Edge edge = getEdgeBetween(next, current);
            path.addFirst(edge);
            current = next;
        }
        return Collections.unmodifiableList(path);
    }

    //Lägg till exception (felkontroll)(verkar fungera som den ska)
    public Edge getEdgeBetween(Object next, Object current) {
        if(!nodes.containsKey(next) || !nodes.containsKey(current)){
            throw new NoSuchElementException();
        }
        for (Edge edge : nodes.get(next)) {
            if (edge.getDestination().equals(current)) {
                return edge;
            }
        }
        return null;
    }

    private void depthFirstConnection(Object to, Object from, Map<Object, Object> connection) {
        connection.put(to, from);
        for (Edge edge : nodes.get(to)) {
            if (!connection.containsKey(edge.getDestination())) {
                depthFirstConnection(edge.getDestination(), to, connection);
            }
        }
    }

    private void depthFirstVisitAll(Object current, Set<Object> visited) {
        visited.add(current);
        for (Edge edge : nodes.get(current)) {
            if (!visited.contains(edge.getDestination())) {
                depthFirstVisitAll(edge.getDestination(), visited);
            }
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object node : nodes.keySet()) {
            sb.append(node).append(": ").append(nodes.get(node)).append("\n");
        }
        return sb.toString();
    }
}