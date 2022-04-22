/**
 * PROG2 VT2022, Inlämningsuppgift, del 1
 * Grupp 089
 * Artin Mohseni armo9784
 * Adam Polanik adan7490
*/
import java.io.Serializable;
import java.util.*;

/**
 * Oriktad graf
 */
public class ListGraph<T> implements Graph,Serializable{

    //Static för att testa metoden getNodes()
    private static final Map<Object, Set<Edge>> nodes = new HashMap<>();

    @Override
    public void add(Object node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    //Lägg till remove metod här!
    //Inte testat!
    //Fungerar inte korrekt
    public void remove(Object node) throws NoSuchElementException {
        if (!nodes.containsKey(node)){
            throw new NoSuchElementException("Non existing node!");
        }
        Collection<Edge> edgesToRemove = getEdgesFrom(node);
        for (Edge edge : edgesToRemove){
            Object destination = edge.getDestination();
            Edge between = getEdgeBetween(edge.getDestination(), node);
            if (between != null){
                nodes.get(node).remove(edge);
                nodes.get(destination).remove(between);
            }
        }
        nodes.remove(node);
    }


    //Denna Fungerar!
    @Override
    public void connect(Object a, Object b, String name, int weight) {
        if(!getNodes().contains(b) || !getNodes().contains(a)){
            throw new NoSuchElementException();
        }
        if(weight < 0){ throw new IllegalArgumentException(); }

        if(getEdgeBetween(a, b) != null){ throw new IllegalStateException(); }

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
        }
        if (nodes.containsKey(a) && nodes.containsKey(b) && getEdgeBetween(a, b) == null){
            throw new IllegalStateException("Non existing edge between given nodes!");
        }
        nodes.get(a).remove(getEdgeBetween(a, b));
        nodes.get(b).remove(getEdgeBetween(b, a));
    }

    //Lägg till setConnectionWeight metod här!
    //Klar med metod, ej testad!
    @Override
    public void setConnectionWeight(Object node1, Object node2, int weight) throws NoSuchElementException, IllegalArgumentException{
        if (weight < 0){
            throw new IllegalArgumentException("Negative weight not allowed!");
        }
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)){
            throw new NoSuchElementException("Node missing in graph!");
        }
        if (getEdgesFrom(node1) == null || getEdgesFrom(node2) == null){
            throw new NoSuchElementException("No edge connection between nodes!");
        } else {
            getEdgeBetween(node1, node2).setWeight(weight);
            getEdgeBetween(node2, node1).setWeight(weight);
        }
    }

    //Lägg till getNodes metod här!
    //Gör static för att testa metoden!
    @Override
    public Set<Object> getNodes() {
        HashSet<Object> nodeCopy = new HashSet<>();
        for (Object n : nodes.keySet()){
            nodeCopy.add(n);
        }
        return nodeCopy;
    }

    //Lägg till getEdgesFrom metod Här! (verkar fungera korrekt)
    public Collection<Edge> getEdgesFrom(Object node) throws NoSuchElementException{
        if(!nodes.containsKey(node)){
            throw new NoSuchElementException();
        }else {
            Set<Edge> tempSet = new HashSet<>();
            tempSet.addAll(nodes.get(node));
            return tempSet;
        }
    }


    public boolean pathExists(Object a, Object b) {
        if(!nodes.containsKey(a) || !nodes.containsKey(b)){
            return false;
        }
        Set<Object> visited = new HashSet<>();
        depthFirstVisitAll(a, visited);
        return visited.contains(b);
    }


    @Override
    public List<Edge> getPath(Object from, Object to) {
        Map<Object, Object> connection = new HashMap<>();
        depthFirstConnection(from, null, connection);
        if (!connection.containsKey(to)) {
            return null;
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
    @Override
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