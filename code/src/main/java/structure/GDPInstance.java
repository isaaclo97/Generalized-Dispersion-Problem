package structure;

import grafo.optilib.structure.Instance;

import java.io.*;
import java.util.Arrays;

public class GDPInstance implements Instance {

    private String name;
    private int nodes,K1,K2,B; //maximum budget, maximum variable budget, minimum capacity
    private long startTime;
    private long minutes = 60;//1*60;
    private double graphDist[][];
    private double graphCostSite[];
    private double graphCostPerUnit[];
    private double graphCapacity[];
    private double Fj=0;
    private Node[] graph;

    double Kj=0,Dj=0,Cj=0;

    public GDPInstance(String path) {
        readInstance(path);
        startTime = System.nanoTime();
    }

    @Override
    public void readInstance(String path) {
        this.name = path.substring(path.lastIndexOf('\\') + 1);
        System.out.println("Reading instance " + this.name);
        FileReader fr= null;
        try {
            fr = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br=new BufferedReader(fr);
        String line;
        try {
            line = br.readLine();
            nodes = Integer.parseInt(line.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // read line by line
        try{
            String[] l;
            graphDist = new double[nodes][nodes];
            graphCostSite = new double[nodes];
            graphCostPerUnit = new double[nodes];
            graphCapacity = new double[nodes];
            graph = new Node[nodes];

            for(int i = 0; i<nodes*(nodes-1)/2;i++) {
                line = br.readLine();
                l = line.split(" ");
                int start = Integer.parseInt(l[0])- 1;
                int end = Integer.parseInt(l[1]) - 1;
                double cost = Double.parseDouble(l[2]);
                graphDist[start][end] = cost;
                graphDist[end][start] = cost;
            }

            for(int i = 0; i<nodes;i++) {
                line = br.readLine();
                l = line.split(" ");
                int site = Integer.parseInt(l[0])-1;
                int costSite = (int)Double.parseDouble(l[1]);
                double costPerUnit = Double.parseDouble(l[2]);
                double capacity = Double.parseDouble(l[3]);
                graphCostSite[site] = costSite;
                graphCostPerUnit[site] = costPerUnit;
                graphCapacity[site] = capacity;
                graph[site] = new Node(costSite,costPerUnit,capacity,site);
            }
            line = br.readLine();
            l = line.split(" ");
            K1 = Integer.parseInt(l[0]);
            K2 = Integer.parseInt(l[1]);
            B = Integer.parseInt(l[2]);
            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        Arrays.sort(graph);
        calculatePrevData();
    }

    private void calculatePrevData() {
        for(Node i:graph) {
            for (int j = 0; j < nodes; j++) {
                if (j == i.getID())
                    continue;
                i.setDi(i.getDi() + getGraphDist()[i.getID()][j]);
            }
            Dj = Math.max(Dj,i.getDi());
            Kj = Math.max(Kj,i.getCost());
            Cj = Math.max(Cj,i.getCapacity());
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public long getMinutes() {
        return minutes;
    }

    public String getName() {
        return name;
    }

    public int getNodes() {
        return nodes;
    }

    public int getK1() {
        return K1;
    }

    public int getB() {
        return B;
    }

    public double[][] getGraphDist() {
        return graphDist;
    }

    public double[] getGraphCostSite() {
        return graphCostSite;
    }


    public double[] getGraphCapacity() {
        return graphCapacity;
    }

    public Node[] getGraph() {
        return graph;
    }

    public double getKj() {
        return Kj;
    }

    public double getCj() {
        return Cj;
    }

}
