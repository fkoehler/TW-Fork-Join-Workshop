package com.thoughtworks.fjw.shortestpath;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SequentialTest {

	@Test
	public void testShortestPathProblem() {

		// input: graph matrix
		// @formatter:off
		int[][] graphMatrix = new int[][] {
				//        0   1   2   3   4   5
				//        A   B   C   D   E   F
				/* A */{ -1, 10, -1, -1, -1, -1 },
				/* B */{ -1, -1,  8, 13, 24, 51 },
				/* C */{ -1, -1, -1, 14, -1, -1 },
				/* D */{ -1, -1, -1, -1,  9, -1 },
				/* E */{ -1, -1, -1, -1, -1, 17 },
				/* F */{ -1, -1, -1, -1, -1, -1 }
		};
		// @formatter:on

		ShortestPathServiceSeq shortestPathServiceSeq = new ShortestPathServiceSeq(graphMatrix, 0, 5);

		assertThat(shortestPathServiceSeq.getShortestDestination(), is(49));
	}

}
