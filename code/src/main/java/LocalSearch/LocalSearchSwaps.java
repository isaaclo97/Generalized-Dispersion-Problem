package LocalSearch;

import grafo.optilib.metaheuristics.Improvement;
import structure.GDPSolution;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


public class LocalSearchSwaps implements Improvement<GDPSolution> {

    public void improve(GDPSolution solution) {
        long timeToSolution = 0;
        long startTime = solution.getInstance().getStartTime();
        GDPSolution newSolution = new GDPSolution(solution);
        GDPSolution copySolution = new GDPSolution(solution);
        boolean improveResult = true;
        ArrayList<Integer> nodes  = new ArrayList<>();
        ArrayList<Integer> nodesInSolution  = new ArrayList<>();
        for(int i = 1; i<newSolution.getInstance().getNodes();i++){
            int ID = newSolution.getInstance().getGraph()[i].getID();
            if(!newSolution.isInSolution(ID))
                nodes.add(ID);
        }
        for(int s:solution.getS()){
            if(solution.getMark()==solution.getSelectedNode()[s]){
                nodesInSolution.add(s);
            }
        }
        ArrayList<Integer> addNodes = new ArrayList<>();
        ArrayList<Integer> dropValues = new ArrayList<>();
        while(improveResult){
            Collections.sort(nodes);
            Collections.shuffle(nodesInSolution);
            improveResult = false;
            for(int i=0; i<nodesInSolution.size() && !improveResult;i++){
                for(int j=0; j<nodes.size()  && !improveResult;j++){
                    timeToSolution = getTimeToSolution(startTime);
                    if(timeToSolution > newSolution.getInstance().getMinutes())
                        return;
                    int remove = nodesInSolution.get(i);
                    int add = nodes.get(j);
                    copySolution.copy(newSolution);
                    swapMovement(newSolution, add, remove);
                    addNodes.clear();
                    addNodes.add(add);
                    if(!newSolution.checkFeasible() && newSolution.getCost()<newSolution.getInstance().getK1()) {
                        addValues(newSolution, addNodes);
                    }
                    else{
                        dropValues(newSolution, dropValues);
                    }
                    if(newSolution.checkFeasible() && Double.compare(solution.getMark(),newSolution.getMark())<0){
                        improveResult = copyBestSolution(solution, newSolution, nodes, nodesInSolution, addNodes, remove);
                        continue;
                    }
                    else if(newSolution.checkFeasible() && Double.compare(solution.getMark(),newSolution.getMark())==0){
                        if(newSolution.evaluateSum()>solution.evaluateSum()){
                            improveResult = copyBestSolution(solution, newSolution, nodes, nodesInSolution, addNodes, remove);
                            continue;
                        }
                    }
                    newSolution.copy(copySolution);
                }
            }
        }
    }

    private long getTimeToSolution(long startTime) {
        long timeToSolution;
        timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timeToSolution = (timeToSolution / 1000);
        return timeToSolution;
    }

    private boolean copyBestSolution(GDPSolution solution, GDPSolution newSolution, ArrayList<Integer> nodes, ArrayList<Integer> nodesInSolution, ArrayList<Integer> addNodes, Integer remove) {
        nodes.removeAll(addNodes);
        nodes.add(remove);
        solution.copy(newSolution);
        nodesInSolution.clear();
        for (int s : solution.getS()) {
            if (solution.getMark() == solution.getSelectedNode()[s]) {
                nodesInSolution.add(s);
            }
        }
        return true;
    }

    private void dropValues(GDPSolution newSolution, ArrayList<Integer> dropValues) {
        boolean drop = true;
        while(drop){
            dropValues.clear();
            drop = false;
            for(int s: newSolution.getS()){
                if(newSolution.isFeasibleDrop(s)){
                    drop=true;
                    dropValues.add(s);
                    break;
                }
            }
            if(dropValues.size()!=0){
                Collections.shuffle(dropValues);
                newSolution.removeToSolution(dropValues.get(0));
            }
        }
    }

    private void addValues(GDPSolution newSolution, ArrayList<Integer> addNodes) {
        while (!newSolution.checkFeasible()) {
            double curValue = -1;
            int selected_node = -1;
            for (int x = 0; x < newSolution.getInstance().getNodes(); x++) {
                if (!newSolution.getS().contains(x) && newSolution.notExceedCost(x)) {
                    double value = newSolution.getMarkPartialMark(x);
                    if (value > curValue) {
                        curValue = value;
                        selected_node = x;
                    }
                }
            }
            if(selected_node==-1){
                break;
            }
            newSolution.addToSolution(selected_node);
            addNodes.add(selected_node);
        }
    }

    private void swapMovement(GDPSolution newSolution, int add, int remove) {
        newSolution.addToSolution(add);
        newSolution.removeToSolution(remove);
    }
}
