package com.thoughtworks.fjw.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

public class ShortestPathCalculatorAction extends RecursiveAction {

	private static final Logger LOGGER = Logger.getLogger(ShortestPathCalculatorAction.class.getName());

	private static final int INFINITY = -1;

	private final int[][] graphMatrix;
	private final int numberOfVertexes;
	//private final Queue<Integer> vertexesToExamine = new LinkedList<Integer>();
	private final int[] currentMinimumDistances;
	private final int targetVertex;

	private final int sourceVertex;

	public ShortestPathCalculatorAction(final int[][] graphMatrix, final int sourceVertex, final int targetVertex,
			final int[] currentMinimumDistances) {
		this.graphMatrix = graphMatrix;
		this.sourceVertex = sourceVertex;
		this.targetVertex = targetVertex;

		numberOfVertexes = graphMatrix.length;

		// create minimum distances array
		this.currentMinimumDistances = currentMinimumDistances;
	}

	@Override
	protected void compute() {
		long threadId = Thread.currentThread().getId();
		String name = Thread.currentThread().getName();
		String msg = String.format("Thread id:%d name:%s computing\n", threadId, name);
		msg += String.format("Working on vertex: %d, searching way to vertex %d\n", sourceVertex, targetVertex);
		msg += "current minimum distances before computing: " + Arrays.toString(currentMinimumDistances);

		LOGGER.info(msg);

		List<ShortestPathCalculatorAction> tasksToFork = new ArrayList<ShortestPathCalculatorAction>();

		int vertexToExamine = sourceVertex;
		for (int targetVertex = 0; targetVertex < numberOfVertexes; targetVertex++) {
			int distanceFromToExamineToTargetVertex = graphMatrix[vertexToExamine][targetVertex];
			if (distanceFromToExamineToTargetVertex != INFINITY) {
				int newDistanceForTargetVertex = currentMinimumDistances[vertexToExamine]
						+ distanceFromToExamineToTargetVertex;
				if (newDistanceForTargetVertex < currentMinimumDistances[targetVertex]
						|| currentMinimumDistances[targetVertex] == 0) {
					currentMinimumDistances[targetVertex] = newDistanceForTargetVertex;

					msg = "Found new minimum distance for targetvertex: " + targetVertex + ": "
							+ currentMinimumDistances[targetVertex] + "\n";
					msg += "Building new task for vertex " + targetVertex + " because we need to check it again";

					// TODO i need a way to create two tasks for the same target vertex to test concurrency
					LOGGER.info(msg);

					// start a new task here? problem: the new currentminimumdistance is not available to
					// the other tasks, we would need to access a shared variable then?
					ShortestPathCalculatorAction shortestPathCalculatorAction = new ShortestPathCalculatorAction(
							graphMatrix, targetVertex, this.targetVertex, currentMinimumDistances);

					tasksToFork.add(shortestPathCalculatorAction);
				}
			}
		}

		msg = String.format("Thread id:%d name:%s done computing\n", threadId, name);
		msg += "current minimum distances after computing: " + Arrays.toString(currentMinimumDistances) + "\n\n";
		LOGGER.info(msg);

		invokeAll(tasksToFork);
	}

	public int getShortestDestination() {
		return currentMinimumDistances[targetVertex];
	}

}
