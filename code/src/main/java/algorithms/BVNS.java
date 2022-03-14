package algorithms;

import LocalSearch.*;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.results.Result;
import grafo.optilib.structure.Solution;
import grafo.optilib.tools.RandomManager;
import structure.GDPInstance;
import structure.GDPSolution;
import structure.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class BVNS implements Algorithm<GDPInstance> {

	final Constructive<GDPInstance, GDPSolution> constructive;

	// The output directory path, used for testing purposes only
	private Double kMax;
	private LocalSearchSwaps improvement;
	private GDPSolution MDCCSolution=null,copySolution,MDCCSolutionTest=null;
	private ArrayList<Pair> p = new ArrayList<>();
	private long totalTimeToBest = 0,startTime;

	public BVNS(Constructive<GDPInstance, GDPSolution> constructive, LocalSearchSwaps improvement, Double kMax){
		this.constructive = constructive;
		this.kMax = kMax;
		this.improvement = improvement;
	}

	@Override
	public Result execute(GDPInstance MDCCInstance){
		MDCCSolution = new GDPSolution(MDCCInstance);
		MDCCSolutionTest = new GDPSolution(MDCCInstance);
		copySolution = new GDPSolution(MDCCInstance);
		startTime = MDCCInstance.getStartTime();
		long timeToSolution = 0;

		Result r = new Result(MDCCInstance.getName());
		for(int i=1;i<=8000000  && timeToSolution < MDCCInstance.getMinutes();i++){
			GDPSolution sol = constructive.constructSolution(MDCCInstance);
			if (sol.checkFeasible()) {
				if (sol.getMark() > MDCCSolution.getMark()) {
					MDCCSolution.copy(sol);
					System.out.println("Curbest: " + MDCCSolution.getMark());
					totalTimeToBest = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
				}
			}
			improvement.improve(sol);
			if (sol.checkFeasible()) {
				if (sol.getMark() > MDCCSolution.getMark()) {
					MDCCSolution.copy(sol);
					System.out.println("Curbest: " + MDCCSolution.getMark());
					totalTimeToBest = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
				}
			}

			timeToSolution = getTimeToSolution(startTime);
			int k = 1;
			double realKMax = Math.max(1, sol.getS().size() * kMax);
			GDPSolution mdccVNS = new GDPSolution(sol);
			MDCCSolutionTest.copy(mdccVNS);
			while (k <= realKMax && timeToSolution < MDCCInstance.getMinutes()) { // && timeToSolution < MDCCInstance.getMinutes()
				shakeRandomSwap(mdccVNS, k);
				improvement.improve(mdccVNS);
				k = neighborhoodChange(mdccVNS, k);
				timeToSolution = getTimeToSolution(startTime);
			}
			timeToSolution = getTimeToSolution(startTime);
			if (sol.checkFeasible() && MDCCSolutionTest.getMark() > MDCCSolution.getMark()) {
				MDCCSolution.copy(MDCCSolutionTest);
				System.out.println("Curbest: " + MDCCSolution.getMark());
				totalTimeToBest = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
			}
			r.add("# FO iteration " + i, MDCCSolution.getMark());
			r.add("# Time Iteration " + i, getTimeToSolution(startTime));
		}

		MDCCSolution.reevaluategetMark();
		if(!MDCCSolution.checkFeasible()){
			System.out.println("ERROR");
			MDCCSolution.setS(new HashSet<>());
			MDCCSolution.reevaluategetMark();
			MDCCSolution.setDispersion(-1);
		}
		double seconds = getTimeToSolution(startTime);
		System.out.println("Time (s): " + seconds);
		System.out.println("Final solution: " + MDCCSolution.getMark());

		r.add("Time (s)", seconds);
		r.add("# Dispersion", MDCCSolution.getMark());
		r.add("# Cost", MDCCSolution.getCost());
		r.add("# MaxCost", MDCCSolution.getInstance().getK1());
		r.add("# Cap", MDCCSolution.getTotalCapacity());
		r.add("# MinCap", MDCCSolution.getInstance().getB());
		r.add("# timeToBest", totalTimeToBest);
		System.gc();
		MDCCSolution.printSolution();
		showMemoData();

		return r;
	}

	private long getTimeToSolution(long startTime) {
		long timeToSolution;
		timeToSolution = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
		timeToSolution = (timeToSolution / 1000);
		return timeToSolution;
	}

	private void shakeRandomSwap(GDPSolution bcVNS, int k) {
		if(bcVNS.getS().size()<1)
			return;
		long totalTime = bcVNS.getInstance().getMinutes();
		for(int i=0; i<k;i++){
			int firstRandom = RandomManager.getRandom().nextInt(bcVNS.getS().size());
			int secondRandom = RandomManager.getRandom().nextInt(bcVNS.getInstance().getNodes());
			while(bcVNS.getS().contains(secondRandom) && !bcVNS.isFeasible(secondRandom)){
				secondRandom = RandomManager.getRandom().nextInt(bcVNS.getInstance().getNodes());
				long time = getTimeToSolution(bcVNS.getInstance().getStartTime());
				if(time>totalTime)
					return;
			}
			int realNode = (Integer)bcVNS.getS().toArray()[firstRandom];
			copySolution.copy(bcVNS);
			bcVNS.removeToSolution(realNode);
			bcVNS.addToSolution(secondRandom);

			bcVNS.getGreedySolution();

			if(!bcVNS.checkFeasible()){
				bcVNS.copy(copySolution);
			}
		}
	}

	private int neighborhoodChange(GDPSolution bcVNSImprove, int k) {
		if(bcVNSImprove.checkFeasible() && bcVNSImprove.getMark()>MDCCSolutionTest.getMark()){
			MDCCSolutionTest.copy(bcVNSImprove);
			k=1;
			totalTimeToBest = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
		}
		else {
			bcVNSImprove.copy(MDCCSolutionTest);
			k++;
		}
		return k;
	}


	public void showMemoData(){
		// Get current size of heap in bytes
		long heapSize = Runtime.getRuntime().totalMemory();

		// Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
		long heapMaxSize = Runtime.getRuntime().maxMemory();

		// Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
		long heapFreeSize = Runtime.getRuntime().freeMemory();
		System.out.println(heapSize + " - " + heapMaxSize + " - " + heapFreeSize);
	}

	@Override
	public Solution getBestSolution() {
		return null;
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "(" + constructive + ")";
	}

}
