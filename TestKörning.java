public class TestKörning {
    public static void main(String[] args) {
        Node node = new Node("Stockholm");
        Edge edge = new Edge(node, "E4", 100);
        edge.setWeight(50);
        System.out.println(edge);
    }

}
