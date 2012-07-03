package com.thoughtworks.fjw.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ArrayChecksumWithListsRecursiveTask extends RecursiveTask<Integer> {

    private final List<Integer> integers;
    private ArrayChecksumWithListsRecursiveTask leftTaskWithLists;
    private ArrayChecksumWithListsRecursiveTask rightTaskWithLists;

    public ArrayChecksumWithListsRecursiveTask(List<Integer> integers) {
        this.integers = integers;
    }

    @Override
    protected Integer compute() {
        if (workingUnitSmallEnough()) {
            return doCoreComputation();
        }

        forkTasks();
        return joinTasks();
    }

    private Integer joinTasks() {
        return mergeResults(leftTaskWithLists.join(), rightTaskWithLists.join());
    }

    private void forkTasks() {
        List<List<Integer>> splittedList = splitListIntoParts();

        leftTaskWithLists = new ArrayChecksumWithListsRecursiveTask(splittedList.get(0));
        rightTaskWithLists = new ArrayChecksumWithListsRecursiveTask(splittedList.get(1));

        leftTaskWithLists.fork();
        rightTaskWithLists.fork();
    }

    List<List<Integer>> splitListIntoParts() {
        ArrayList<List<Integer>> splittedLists = new ArrayList<List<Integer>>();
        int midpoint = integers.size() / 2;

        splittedLists.add(integers.subList(0, midpoint));
        splittedLists.add(integers.subList(midpoint, integers.size()));

        return splittedLists;
    }

    Integer doCoreComputation() {
        return integers.get(0);
    }

    private boolean workingUnitSmallEnough() {
        return integers.size() == 1;
    }

    static Integer mergeResults(int left, int right) {
        return left + right;
    }

}
