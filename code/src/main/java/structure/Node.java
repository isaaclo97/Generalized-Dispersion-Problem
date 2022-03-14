package structure;

import java.util.Objects;

public class Node implements Comparable<Node>{
    private double costSite,costPerUnit,capacity,cost,value,di;
    private int ID;

    public Node(double costSite, double costPerUnit, double capacity, int ID){
        this.costSite = costSite;
        this.costPerUnit = costPerUnit;
        this.capacity = capacity;
        this.ID = ID;
        cost = costSite ;
    }


    public double getCapacity() {
        return capacity;
    }

    public int getID() {
        return ID;
    }

    public double getCost() {
        return cost;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getDi() {
        return di;
    }

    public void setDi(double di) {
        this.di = di;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return ID == node.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public int compareTo(Node o) {
        return -Double.compare(this.getCapacity()/this.getCost(),o.getCapacity()/o.getCost());
    }
}
