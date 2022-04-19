public class TestKörning {
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





    }

}
