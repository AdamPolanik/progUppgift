import java.util.Set;
import java.util.*;
public class TestKörning {

    public static Map<Node, Set<Edge>> nodes = new HashMap<>();
    public static Set<Edge> sthlmEdges = new HashSet<>();
    public static void main(String[] args) {

        ListGraph listGraph = new ListGraph();

        Node stockholm = new Node("Stockholm");
        Node norrtälje = new Node("Norrtälje");
        Node göteborg = new Node("Göteborg");
        Node täby = new Node("Täby");
        Node bromma = new Node("Bromma");

        Edge E20 = new Edge(norrtälje,"E20", 200);
        Edge E18 = new Edge(täby,"E18",50);
        Edge E4 = new Edge(stockholm, "E4", 100);

        E4.setWeight(50);
        //System.out.println(E4);


        listGraph.add(stockholm);
        listGraph.add(täby);
        listGraph.add(göteborg);
        listGraph.add(norrtälje);

        listGraph.connect(stockholm,täby,"E4",100);
        listGraph.connect(norrtälje,täby,"E18",50);
        listGraph.connect(norrtälje,stockholm,"E18",200);
        listGraph.connect(göteborg,stockholm,"E4",900);

        System.out.println(listGraph.getEdgesFrom(täby));
        //System.out.println(listGraph.getEdgeBetween(göteborg,stockholm));



        Node sthlmNode = new Node("Stockholm");
        Edge e4Edge = new Edge(sthlmNode, "E4", 450);
        Edge e20Edge = new Edge(sthlmNode, "E20", 490);
        //e4Edge1.setWeight(450);
        sthlmEdges.add(e4Edge);
        sthlmEdges.add(e20Edge);
        nodes.put(sthlmNode, sthlmEdges);

        //System.out.println(edge);

        ListGraph graph = new ListGraph();
        /* //För framtida test
        Node malmoeNode = new Node("Malmö");
        Edge e20Edge = new Edge(malmoeNode, "E20", 400);

        Node gbgNode = new Node("Göteborg");
        e4Edge = new Edge(gbgNode, "E4", 620);


        edge1.add(edge2);
        edge1.add(edge3);
        nodes.put(node2, edge1);
        nodes.put(node3, edge1);

        graph.add(node3);

        graph.connect(sthlmNode, malmoeNode, "E4", 620);

        System.out.println(nodes);

 */
            graph.getNodes(nodes);
    }
}


