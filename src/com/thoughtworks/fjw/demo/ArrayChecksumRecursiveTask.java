package com.thoughtworks.fjw.demo;

import java.util.concurrent.RecursiveTask;

public class ArrayChecksumRecursiveTask extends RecursiveTask<Integer> {

    @Override
    protected Integer compute() {
        if (workingUnitSmallEnough()) {
            return doCoreComputation();
        }

        forkTasks();
        return joinTasks();
    }

    private Integer joinTasks() {
        return 0;
    }

    private void forkTasks() {
    }

    private Integer doCoreComputation() {
        return 0;
    }

    private boolean workingUnitSmallEnough() {
        return false;
    }

}
