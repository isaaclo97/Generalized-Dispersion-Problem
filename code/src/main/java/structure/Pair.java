package structure;

public class Pair implements Comparable<Pair>  {
    private int node;
    private double value;
    public Pair(int node, double value){
        this.node = node;
        this.value = value;
    }
    public Pair(Pair node){
        this.node = node.getNode();
        this.value = node.getValue();
    }

    public int getNode() {
        return node;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(Pair o) {
        return Double.compare(getValue(),o.getValue());
    }
}
