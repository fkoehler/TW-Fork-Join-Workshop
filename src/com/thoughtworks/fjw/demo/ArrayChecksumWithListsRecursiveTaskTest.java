package com.thoughtworks.fjw.demo;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class ArrayChecksumWithListsRecursiveTaskTest {

    @Test
    public void testThatCompleteTaskWorksAsExpected() {
        List<Integer> integers = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Integer checksum = new ForkJoinPool().invoke(new ArrayChecksumWithListsRecursiveTask(integers));

        assertThat(checksum, is(55));
    }

    @Test
    public void testThatDirectComputationBehavesAsExpected() {
        List<Integer> integers = asList(1);

        assertThat(new ArrayChecksumWithListsRecursiveTask(integers).doCoreComputation(), is(1));
    }

    @Test
    public void testThatSplittingTheTaskWorksAsExpected() {
        List<Integer> integers = asList(1, 2, 3);

        List<List<Integer>> lists = new ArrayChecksumWithListsRecursiveTask(integers).splitListIntoParts();

        assertThat(lists, hasSize(2));
        assertThat(lists.get(0), is(asList(1)));
        assertThat(lists.get(1), is(asList(2, 3)));
    }

    @Test
    public void testThatMergingResultBehavesAsExpected() {
        assertThat(ArrayChecksumWithListsRecursiveTask.mergeResults(3, 8), is(11));
    }

}
