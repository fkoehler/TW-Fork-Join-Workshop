package com.thoughtworks.fjw.bucketsort;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

public class BucketSortAction extends RecursiveAction {

	private static final Logger LOGGER = Logger.getLogger(BucketSortAction.class.getName());

	private static final int INFINITY = -1;

	private final int[][] graphMatrix;
	private final int numberOfVertexes;
	private final Queue<Integer> vertexesToExamine = new LinkedList<Integer>();
	private final int[] currentMinimumDistances;
	private final int targetVertex;
	private int shortestDestination;

	public BucketSortAction(final int[][] graphMatrix, final int sourceVertex, final int targetVertex) {
		this.graphMatrix = graphMatrix;
		this.targetVertex = targetVertex;

		// add source vertex
		vertexesToExamine.add(sourceVertex);

		numberOfVertexes = graphMatrix.length;

		// create minimum distances array
		currentMinimumDistances = new int[numberOfVertexes];
		for (int vertex = 0; vertex < numberOfVertexes; vertex++) {
			currentMinimumDistances[vertex] = 0;
		}
	}

	@Override
	protected void compute() {
		// mit divide and conquer nicht lösbar, weil die aufgabe nicht in autarke teilaufgaben separierbar
		// ist

		while (vertexesToExamine.peek() != null) {
			int vertexToExamine = vertexesToExamine.poll();
			for (int targetVertex = 0; targetVertex < numberOfVertexes; targetVertex++) {
				int distanceFromToExamineToTargetVertex = graphMatrix[vertexToExamine][targetVertex];
				if (distanceFromToExamineToTargetVertex != INFINITY) {
					int newDistanceForTargetVertex = currentMinimumDistances[vertexToExamine]
							+ distanceFromToExamineToTargetVertex;
					if (newDistanceForTargetVertex < currentMinimumDistances[targetVertex]
							|| currentMinimumDistances[targetVertex] == 0) {
						currentMinimumDistances[targetVertex] = newDistanceForTargetVertex;

						// start a new task here? problem: the new currentminimumdistance is not available to
						// the other tasks
						vertexesToExamine.add(targetVertex);
					}
				}
			}
		}

	}

	public int getShortestDestination() {
		return shortestDestination;
	}

}
