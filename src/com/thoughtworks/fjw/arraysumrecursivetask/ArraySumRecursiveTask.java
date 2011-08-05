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

		// TODO mein hauptproblem: wie fuktioniert der steal mechanism
		//   - jeder workerthread hat eine taskqueue
		//   - angenommen am anfang sind ganz viele 
		//   - priority for queue of workerthreads:
		//     1) taks in own queue (LIFO or FIFO) => decentral, almost no communication overhead
		//     2) randomizly steal other tasks from other workerthread queues
		//     3) new submitted tasks
		/*
		 * When a task is started (forked), it is added to the queue of the thread that is executing its parent task.

		Because each thread can be executing only one task at a time, each thread's task queues can accumulate tasks
		which are not currently executing. Threads that have no tasks allocated to them will attempt to steal a task
		from a thread whose queue has at least one task - this is called work stealing. By this mechanism, tasks are
		distributed to all of the threads in the thread pool.

		By using a thread pool with work stealing, a fork/join framework can allow a relatively fine-grained division
		of the problem, but only create the minimum number of threads needed to fully exploit the available CPU cores
		 Typically, the thread pool will have one thread per available CPU core.
		 */

		// - Es gibt immer einen WorkerThread pro logischem processor der tasks abarbeitet
		// - Die Tasks werden auf Worker-Threads verteilt, teilweise können sich dann queues bilden
		// - Verdammt: Wer hat diese Queue? der Thread oder der Task oder der Pool? Vmtl. THread? 
		//   Vmtl. kann ich die quue nicht einsehen?

		/*
		 * Fork-join is actually implemented using an idea called "work-stealing". Basically, every thread has its own
		 * dequeue (double-ended queue, pronounced "deck") and only that thread reads from the head of the queue.
		 * If a thread runs out of work, it steals work from the tail of someone else's queue. Because the initial
		 * biggest jobs are placed at the tail of each queue, workers steal the biggest task available, which keeps
		 * them busy for longer. This further reduces queue contention and also provides built-in load balancing. 

		 */

		// TODO auf welcher ebene ist die taskqueue
		// was ist mit leichtgewichtigen? kann es zu starvation kommmen, weil die aufwändigen tasks vorrang haben?
		// woher weiss das framework ob ein task aufwändig ist oder nicht?

		// TODO was hat die summer aller tasks für ein name? ist es einfach die taskqueue evtl. ?

		// TODO ganz einfacher sortieralgoryhtmus parallel machen
		// TODO in den tasks das aufteilen und rechnen unterscheiden und unterschiedlich messen
		//      events loggen mit den status und irgendwie zusammenführen, z.b. in zeitreihen
		//      zeitlinie pro core/thread
		// TODO DOMÄNE: sortier algo implementieren, QuickSort
		// TODO SUB DOMÄNE: zelluläre automaten -> was kann ich parallelisieren? wie aufwöndig?
		// TODO DIM 1: try the auto steal avoid thinggy
		// TODO DIM 2: more mechanisms of the fork join framework. Was bietet das framework noch?
		//             was bringen die frameworkvarianten? wann das eine, das andere verwenden?
		// TODO async mode: was heisst das?
		// TODO verschiedene client methoden, execute/submit/invoke !! weiter untersuchen!

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
