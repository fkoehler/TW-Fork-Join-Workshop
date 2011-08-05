package com.thoughtworks.fjw.arraysumrecursiveaction;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

import com.thoughtworks.fjw.utils.Utils;

public class ArraySumRecursiveAction extends RecursiveAction {

	private static final Logger LOGGER = Logger.getLogger(ArraySumRecursiveAction.class.getName());

	private long result;
	private final int[] arrayToCalculateSumOf;

	public ArraySumRecursiveAction(final int[] arrayToCalculateSumOf) {
		this.arrayToCalculateSumOf = arrayToCalculateSumOf;
	}

	public long getResult() {
		return result;
	}

	// - Warum sollte mich ein Berechnungsschritt in einem Algorhytmus interessieren? Warum reicht
	//   mir nicht das Gesamtergebnis????
	// Angenommen ich will es testen:
	// Woran bin ich interessiert im splitfall?
	// - Das die Teilungsmenge jeweils korrekt berechnet wird
	// - Das der Fork stattfindet mit zwei neuen Tasks
	// - Das die eigentliche Berechnung korrekt ist
	// => Idee 1: Eine Klasse zwischen RecursiveAction schieben, dessen Zustand ich beim Testen abfragen kann
	//    Produktionscode enthält eine spezielle Testschicht = unschön

	// TODO suchalgorhytmen durchlesen für aufgaben
	// TODO work stealing mechanism verstehen
	// Aufbau:
	// - Was ist parallel programming
	// - Was machden die Klassen? + Feature, Executor
	// - Bsp: Quersumme berechnen, was muss ich machen: Live zeigen!!
	// - Setup:
	// Bsp: - Selbst programmieren => einfacher Suchalgorhytmus, verteilte volltext suche, wie oft kommt string in text
	//      vor?

	// Laufen lassen: boottime klasse
	@Override
	protected void compute() {
		long threadId = Thread.currentThread().getId();
		String name = Thread.currentThread().getName();

		if (arrayToCalculateSumOf.length == 1) {
			// working unit is small enough, do the work
			result = arrayToCalculateSumOf[0];

			String msg2 = String.format("Thread id:%d name:%s => Starting calculation\n", threadId, name);
			msg2 += String.format("do simple calculation: %d\n", result);
			msg2 += String.format("queued task count: %d\n", getQueuedTaskCount());
			msg2 += String.format("steal count: %d\n", getPool().getStealCount());

			LOGGER.info(msg2);

			if (ArraySumRecursiveActionLoggingTest.firstCalcStartWatch != null) {
				ArraySumRecursiveActionLoggingTest.firstCalcStartWatch.stop("first calc ended");
				ArraySumRecursiveActionLoggingTest.firstCalcStartWatch = null;
			}

		} else {
			// split up the work into smaller parts (fork)
			int midpoint = arrayToCalculateSumOf.length / 2;

			int[] l1 = Arrays.copyOfRange(arrayToCalculateSumOf, 0, midpoint);
			int[] l2 = Arrays.copyOfRange(arrayToCalculateSumOf, midpoint, arrayToCalculateSumOf.length);

			ArraySumRecursiveAction s1 = new ArraySumRecursiveAction(l1);
			ArraySumRecursiveAction s2 = new ArraySumRecursiveAction(l2);

			// join the calculated parts and do the real calculation

			String msg = String.format("Thread id:%d name:%s => Forking tasks\n", threadId, name);
			msg += String.format("queued task count: %d\n", getQueuedTaskCount());
			msg += String.format("steal count: %d\n", getPool().getStealCount());
			LOGGER.info(msg);

			invokeAll(s1, s2);

			Utils.doCpuIntensiveCalculation();

			result += s1.result + s2.result;
			String msg2 = String.format("Thread id:%d name:%s => Starting calculation\n", threadId, name);
			msg2 += String.format("calc: %d + %d = %d\n", s1.result, s2.result, result);
			msg2 += String.format("queued task count: %d\n", getQueuedTaskCount());
			//			msg += String.format("pool size: %d\n", getPool().getPoolSize());
			msg2 += String.format("steal count: %d\n", getPool().getStealCount());
			//			msg += String.format("active thread count: %d\n", getPool().getActiveThreadCount());
			//			msg += String.format("running thread count: %d\n", getPool().getRunningThreadCount());

			LOGGER.info(msg2);
		}
	}

}
