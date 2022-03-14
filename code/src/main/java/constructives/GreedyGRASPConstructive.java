package constructives;

import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.GDPInstance;
import structure.GDPSolution;
import structure.Node;
import java.util.ArrayList;


public class GreedyGRASPConstructive implements Constructive<GDPInstance, GDPSolution> {

    GDPSolution feasibleSol;
    public GDPSolution constructSolution(GDPInstance instance) {
    GDPSolution M = new GDPSolution(instance);
    feasibleSol = new GDPSolution(instance);
    boolean firstNode = true;
    double realAlpha = 0;
    while(!M.checkFeasible()){
        ArrayList<Node> CL = new ArrayList<>();
        ArrayList<Node> RCL = new ArrayList<>();
        for(Node n: instance.getGraph()){
            if(!M.getS().contains(n.getID()) && M.isFeasible(n.getID()))
                CL.add(n);
        }
        double gmax = 0;
        double gmin = Integer.MAX_VALUE;
        for(Node i:CL){
            double value = greedy_function_value_CC(i,M,instance);
            i.setValue(value);
            gmax = Math.max(gmax,i.getValue());
            gmin = Math.min(gmin,i.getValue());
        }
        double threshold = gmax-realAlpha*(gmax-gmin);
        //double threshold = gmin+realAlpha*(gmax-gmin);
        for(Node i:CL){
            boolean eliteList = true;
            if((i.getValue()>=threshold && eliteList) || firstNode){
                RCL.add(i);
            }
        }
        firstNode = false;
        if(RCL.size()!=0){
            Node i = select_site_randomly(RCL);
            M.addToSolution(i.getID());
        }
        else{
            M = repairMethodAddCapDividedByCapacity(M);
            if(!M.checkFeasible()){
                M.copy(feasibleSol);
                return M;
            }
            return M;
        }
    }
    return M;
}

    private Node select_site_randomly(ArrayList<Node> rcl) {
        int randomIndex = RandomManager.getRandom().nextInt(rcl.size());
        Node selectedNode = rcl.get(randomIndex);
        rcl.remove(randomIndex);
        return selectedNode;
    }

    private double greedy_function_value_CC(Node i, GDPSolution sol, GDPInstance instance) {
        double normalized_cost = Math.max(0.01,i.getCost())/instance.getKj();
        double normalized_capacity = Math.max(0.01,i.getCapacity())/instance.getCj();
        double multiplier = Math.max(0.01,sol.getMarkPartialMark(i.getID()));
        return multiplier*(normalized_capacity/normalized_cost);
    }

    private GDPSolution repairMethodAddCapDividedByCapacity(GDPSolution sol){
        for (int x = 0; x < sol.getInstance().getNodes(); x++) {
            sol.addToSolution(sol.getInstance().getGraph()[x].getID());
            //ADDs one or more nodes
            for (Node l : sol.getInstance().getGraph()) {
                int realNode = l.getID();
                if (!sol.getS().contains(realNode) && sol.notExceedCost(realNode)) {
                    sol.addToSolution(realNode);
                }
                if (sol.checkFeasible()) {
                    return sol;
                }
            }
            sol.copy(feasibleSol);
        }
        return sol;
    }
}
