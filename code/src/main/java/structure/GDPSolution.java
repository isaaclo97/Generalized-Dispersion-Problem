package structure;

import grafo.optilib.structure.Solution;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@SuppressWarnings("Duplicates")
public class GDPSolution implements Solution {

    private GDPInstance instance;
    private double cost, dispersion, totalCapacity;
    private boolean updated;
    private HashSet<Integer> S; //selected nodes
    private double selectedNode[];
    public GDPSolution(GDPInstance instance) {
        S = new HashSet<>();
        this.instance = instance;
        this.updated = true;
        selectedNode = new double[instance.getNodes()];
    }
    public GDPSolution(GDPSolution solution) {
        copy(solution);
    }

    public void copy(GDPSolution solution){
        this.instance = solution.getInstance();
        this.S = new HashSet<>(solution.getS());
        this.updated = solution.isUpdated();
        this.cost = solution.getCost();
        this.dispersion = solution.getDispersion();
        this.totalCapacity = solution.getTotalCapacity();
        this.selectedNode = Arrays.copyOf(solution.getSelectedNode(),solution.getInstance().getNodes());
    }

    public double evaluateDispersion() {
        if(S.size()==0) return -1;
        double res = Double.MAX_VALUE;
        for(int s:S)
            selectedNode[s]=0x3f3f3f3f;
        Object[] facilities = S.toArray();
        for (int i=0; i<S.size();i++) {
            int realNode = (int)facilities[i];
            for (int j=i+1; j<S.size();j++) {
                int realNode2 = (int)facilities[j];
                double distanceBeetwenNodes = instance.getGraphDist()[realNode][realNode2];
                this.selectedNode[realNode]=Math.min(selectedNode[realNode],distanceBeetwenNodes);
                this.selectedNode[realNode2]=Math.min(selectedNode[realNode2],distanceBeetwenNodes);
                res = Math.min(res,distanceBeetwenNodes);
            }
        }
        return this.dispersion = res;
    }

    public double evaluateSum() {
        if(S.size()==0) return 0;
        double realRes = 0;
        Object[] facilities = S.toArray();
        for (int i=0; i<S.size();i++) {
            int realNode = (int)facilities[i];
            for (int j=i+1; j<S.size();j++) {
                int realNode2 = (int)facilities[j];
                realRes += instance.getGraphDist()[realNode][realNode2];
            }
        }
        return realRes;
    }

    public double getMark(){
        if(this.updated) {
            evaluateDispersion();
            evaluateCost();
            evaluateCapacity();
            this.updated = false;
        }
        return this.dispersion;
    }

    public double reevaluategetMark(){
        evaluateDispersion();
        evaluateCost();
        evaluateCapacity();
        return this.dispersion;
    }

    private void evaluateCapacity() {
        double totalCapacity = 0;
        for(Integer node:S){
            totalCapacity+=instance.getGraphCapacity()[node];
        }
        this.totalCapacity = totalCapacity;
    }

    private void evaluateCost() {
        double totalCost = 0;
        for(Integer node:S){
            totalCost+=instance.getGraphCostSite()[node];
        }
        this.cost = totalCost;
    }

    public double getMarkPartialMark(int node){
        double res = getMark();
        int i = node;
        for (Integer s: S) {
            res = Math.min(res, instance.getGraphDist()[i][s]);
        }
        return res;
    }

    public GDPInstance getInstance() {
        return instance;
    }

    public HashSet<Integer> getS() {
        return S;
    }

    public void setS(HashSet<Integer> s) {
        S = s;
    }
    public void addToSolution(int node){
        updated = true;
        double addSum = instance.getGraphCostSite()[node];
        this.cost+=addSum;
        this.totalCapacity+=instance.getGraphCapacity()[node];
        S.add(node);
    }
    public void removeToSolution(int node){
        updated = true;
        double addSum = instance.getGraphCostSite()[node];
        this.cost-=addSum;
        this.totalCapacity-=instance.getGraphCapacity()[node];
        S.remove(node);
    }
    public boolean isInSolution(int node){
        return S.contains(node);
    }

    public boolean checkFeasible(){
        return this.cost<instance.getK1() && this.totalCapacity>=instance.getB() && S.size()>0;
    }

    public boolean isFeasible(int add){
        double addSum = instance.getGraphCostSite()[add];
        return !getS().contains(add) && (this.cost+addSum)<=instance.getK1();
    }

    public boolean isFeasibleDrop(int drop){
        double removeCap = instance.getGraphCapacity()[drop];
        return  (this.totalCapacity-removeCap)>=instance.getB();
    }

    public double getCost() {
        return cost;
    }

    public double getDispersion() {
        return dispersion;
    }

    public void setDispersion(double dispersion) {
        this.dispersion = dispersion;
    }

    public boolean isUpdated() {
        return updated;
    }

    public double getTotalCapacity() {
        return totalCapacity;
    }

    public void printSolution(){
        try {
            PrintWriter writeToFile = new PrintWriter("./solutions/"+this.getInstance().getName());
            for(Integer facilitie: S) {
                writeToFile.write(facilitie + " ");
            }
            writeToFile.write("\nDispersion: " + this.getMark());
            writeToFile.write("\nTotal cost: " + this.getCost());
            writeToFile.write("\nTotal capacity: " + this.getTotalCapacity());
            writeToFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public double[] getSelectedNode() {
        return selectedNode;
    }

    public void getGreedySolution(){
        if(this.checkFeasible())
            return;
        ArrayList<Integer> dropValues = new ArrayList<>();
        if (!this.checkFeasible() && this.getCost() < this.getInstance().getK1()) {
            while (!this.checkFeasible()) {
                double curValue = -1;
                int selected_node = -1;
                for (int x = 0; x < this.getInstance().getNodes(); x++) {
                    if (!this.getS().contains(x) && this.notExceedCost(x)) {
                        double value = this.getMarkPartialMark(x);
                        if (value > curValue) {
                            curValue = value;
                            selected_node = x;
                        }
                    }
                }
                if (selected_node == -1) {
                    break;
                }
                this.addToSolution(selected_node);
            }
        }
        else {
            boolean drop = true;
            while (drop) {
                dropValues.clear();
                drop = false;
                for (int s : this.getS()) {
                    if (this.isFeasibleDrop(s)) {
                        drop = true;
                        dropValues.add(s);
                        break;
                    }
                }
                if (dropValues.size() != 0) {
                    Collections.shuffle(dropValues);
                    this.removeToSolution(dropValues.get(0));
                }
            }
        }
    }

    public boolean notExceedCost(int node){
        double addSum = instance.getGraphCostSite()[node];
        return  (this.cost+addSum)<instance.getK1();
    }
}
