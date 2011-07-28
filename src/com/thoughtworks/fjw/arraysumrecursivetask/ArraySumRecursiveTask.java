package com.thoughtworks.fjw.arraysumrecursivetask;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

import com.thoughtworks.fjw.utils.Utils;

public class ArraySumRecursiveTask extends RecursiveTask<Long> {

	private final int[] arrayToCalculateSumOf;

	public ArraySumRecursiveTask(final int[] arrayToCalculateSumOf) {
		this.arrayToCalculateSumOf = arrayToCalculateSumOf;
	}

	@Override
	protected Long compute() {
		if (arrayToCalculateSumOf.length == 1) {
			// working unit is small enough, do the work
			return (long) arrayToCalculateSumOf[0];
		}

		// TODO github.com rumspielen -> aufsetzen
		// TODO DO im laufe des tages plaudern
		// TODO FR nach möglichkeit aufgaben benennen

		// TODO was ist ein aufwändiger task?
		// TODO auf welcher ebene ist die taskqueue
		// TODO mehrere tasks in einem forkjoinpool? was ist mit leichtgewichtigen?
		// TODO was ist die summe aller tasks als bedeutung?
		// TODO ganz einfacher sortieralgoryhtmus parallel machen
		// TODO in den tasks das aufteilen und rechnen unterscheiden und unterschiedlich messen
		//      events loggen mit den status und irgendwie zusammenführen, z.b. in zeitreihen
		//      zeitlinie pro core/thread
		// TODO DOMÄNE: sortier algo implementieren, QuickSort
		// TODO SUB DOMÄNE: zelluläre automaten -> was kann ich parallelisieren? wie aufwöndig?
		// TODO DIM 1: try the auto steal avoid thinggy
		// TODO DIM 2: more mechanisms of the fork join framework. Was bietet das framework noch?
		//             was bringen die frameworkvarianten? wann das eine, das andere verwenden?

		// split up the work into smaller parts (fork)
		int midpoint = arrayToCalculateSumOf.length / 2;

		int[] l1 = Arrays.copyOfRange(arrayToCalculateSumOf, 0, midpoint);
		int[] l2 = Arrays.copyOfRange(arrayToCalculateSumOf, midpoint, arrayToCalculateSumOf.length);

		ArraySumRecursiveTask s1 = new ArraySumRecursiveTask(l1);
		ArraySumRecursiveTask s2 = new ArraySumRecursiveTask(l2);

		s1.fork();
		long result2 = s2.compute();

		// join the calculated parts and do the real calculation
		long result1 = s1.join();

		Utils.doCpuIntensiveCalculation();

		return result1 + result2;
	}

}
