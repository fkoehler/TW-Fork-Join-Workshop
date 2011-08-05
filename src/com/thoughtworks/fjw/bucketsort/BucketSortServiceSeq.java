package com.thoughtworks.fjw.bucketsort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class BucketSortServiceSeq {

	private static final int INFINITY = -1;

	private final int[][] graphMatrix;
	private final int numberOfVertexes;
	private final Queue<Integer> vertexesToExamine = new LinkedList<Integer>();
	private final int[] currentMinimumDistances;
	private final int targetVertex;

	public BucketSortServiceSeq(final int[][] graphMatrix, final int sourceVertex, final int targetVertex) {
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

	public int getShortestDestination() {
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
						vertexesToExamine.add(targetVertex);
					}
				}
			}
		}

		System.out.println(Arrays.toString(graphMatrix));
		System.out.println(Arrays.toString(currentMinimumDistances));

		return currentMinimumDistances[targetVertex];
	}

}
