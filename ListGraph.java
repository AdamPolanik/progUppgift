import java.util.*;

/**
 * Oriktad graf
 */
public class ListGraph {

    //Static för att testa metoden getNodes()
    private static final Map<Node, Set<Edge>> nodes = new HashMap<>();

    public void add(Node node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    //Lägg till funktionalitet i denna (Exceptions och felkontroller)
    public void connect(Node a, Node b, String name, double weight) {
        add(a);
        add(b);

        Set<Edge> aEdges = nodes.get(a);
        Set<Edge> bEdges = nodes.get(b);

        aEdges.add(new Edge(b, name, weight));
        bEdges.add(new Edge(a, name, weight));

    }

    //Lägg till en disconnect metod här!

    //Lägg till setConnectionWeight metod här!

    //Lägg till getNodes metod här!
    //Static för att testa metoden!
    public static Map<Node, Set<Edge>> getNodes(Map<Node, Set<Edge>> nodes) {
        Map<Node, Set<Edge>> nodeCopy = new HashMap<>();
        for (Node n : nodes.keySet()){
            nodeCopy.put(n, nodes.get(n));
        }
        System.out.println("Kopia: " + nodeCopy);
        return nodeCopy;
    }

    //Lägg till getEdgesFrom metod Här! (verkar fungera korrekt)
    public Set<Edge> getEdgesFrom(Node node){
        if(!nodes.containsKey(node)){
            throw new NoSuchElementException();
        }else {
            Set<Edge> tempSet = new HashSet<>();
            tempSet.addAll(nodes.get(node));
            return tempSet;
        }
    }

    public boolean pathExists(Node a, Node b) {
        Set<Node> visited = new HashSet<>();
        depthFirstVisitAll(a, visited);
        return visited.contains(b);
    }


    public List<Edge> getAnyPath(Node from, Node to) {
        Map<Node, Node> connection = new HashMap<>();
        depthFirstConnection(from, null, connection);
        if (!connection.containsKey(to)) {
            return Collections.emptyList();
        }
        return gatherPath(from, to, connection);
    }

    public List<Edge> getShortestPath(Node from, Node to) {
        Map<Node, Node> connections = new HashMap<>();
        connections.put(from, null);

        LinkedList<Node> queue = new LinkedList<>();
        queue.add(from);
        while (!queue.isEmpty()) {
            Node node = queue.pollFirst();
            for (Edge edge : nodes.get(node)) {
                Node destination = edge.getDestination();
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

    private List<Edge> gatherPath(Node from, Node to, Map<Node, Node> connection) {
        LinkedList<Edge> path = new LinkedList<>();
        Node current = to;
        while (!current.equals(from)) {
            Node next = connection.get(current);
            Edge edge = getEdgeBetween(next, current);
            path.addFirst(edge);
            current = next;
        }
        return Collections.unmodifiableList(path);
    }

    //Lägg till exception (felkontroll)(verkar fungera som den ska)
    private Edge getEdgeBetween(Node next, Node current) {
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

    private void depthFirstConnection(Node to, Node from, Map<Node, Node> connection) {
        connection.put(to, from);
        for (Edge edge : nodes.get(to)) {
            if (!connection.containsKey(edge.getDestination())) {
                depthFirstConnection(edge.getDestination(), to, connection);
            }
        }

    }

    private void depthFirstVisitAll(Node current, Set<Node> visited) {
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
        for (Node node : nodes.keySet()) {
            sb.append(node).append(": ").append(nodes.get(node)).append("\n");
        }
        return sb.toString();
    }


}