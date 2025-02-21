package datastructures

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

private class ExceptionMonitoringThread(
    private val exceptions: MutableList<Exception>,
    private val lock: Lock,
    private var body: () -> Unit,
) : Runnable {
    override fun run() {
        try {
            body()
        } catch (exception: Exception) {
            lock.withLock() {
                exceptions.add(exception)
            }
        }
    }
}

abstract class ThreadSafeCustomMutableMapTestsParent : CustomMutableMapTestsParent() {
    abstract fun emptyThreadSafeMapIntString(): CustomMutableMap<Int, String>

    @Suppress("SameParameterValue")
    private fun addElementsInRandomOrder(
        map: CustomMutableMap<Int, String>,
        lowerBound: Int,
        numElements: Int,
        seed: Int,
    ) {
        val randomGenerator = Random(seed)
        val remaining = mutableListOf<Int>()
        (lowerBound..<lowerBound + numElements).forEach {
            remaining.add(it)
        }
        while (remaining.isNotEmpty()) {
            val index = randomGenerator.nextInt(remaining.size)
            val toAdd = remaining.removeAt(index)
            map.put(toAdd, toAdd.toString())
        }
    }

    @Suppress("SameParameterValue")
    private fun removeElementsInRandomOrder(
        map: CustomMutableMap<Int, String>,
        lowerBound: Int,
        numElements: Int,
        seed: Int,
    ) {
        val randomGenerator = Random(seed)
        val remaining = mutableListOf<Int>()
        (lowerBound..<lowerBound + numElements).forEach {
            remaining.add(it)
        }
        while (remaining.isNotEmpty()) {
            val index = randomGenerator.nextInt(remaining.size)
            val toRemove = remaining.removeAt(index)
            map.remove(toRemove)
        }
    }

    private fun runConcurrencyTest(
        repeatRuns: Int,
        threadBodies: List<(CustomMutableMap<Int, String>) -> Unit>,
        initialEntries: List<CustomMutableMap.Entry<Int, String>>,
        expectedInFinalResult: Set<CustomMutableMap.Entry<Int, String>>,
        notExpectedInFinalResult: Set<CustomMutableMap.Entry<Int, String>>,
    ) {
        for (i in 1..repeatRuns) {
            println("Repeat run $i of $repeatRuns")
            val theMap = emptyThreadSafeMapIntString()
            for (entry in initialEntries) {
                theMap.put(entry)
            }
            val exceptions = mutableListOf<Exception>()
            val lock = ReentrantLock()
            val threads = threadBodies.map {
                Thread(
                    ExceptionMonitoringThread(
                        exceptions,
                        lock,
                    ) { it(theMap) },
                )
            }
            threads.forEach(Thread::start)
            threads.forEach(Thread::join)
            if (exceptions.isNotEmpty()) {
                System.err.println("Exceptions thrown by thread(s):")
                for (exception in exceptions) {
                    System.err.println(exception)
                }
                fail()
            }
            val finalContentsAsList = theMap.toList()
            val finalContentsAsSet = finalContentsAsList.toSet()
            // There should be no difference in the size of final contents as a list vs. as a set
            assertEquals(finalContentsAsList.size, finalContentsAsSet.size)
            assertTrue(finalContentsAsSet.containsAll(expectedInFinalResult))
            assertTrue((finalContentsAsSet intersect notExpectedInFinalResult).isEmpty())
        }
    }

    @Test
    fun `one thread adds, one thread removes`() {
        val chunkSize = 1 shl 12
        val adderBody: (CustomMutableMap<Int, String>) -> Unit = { theMap ->
            Thread.sleep(1)
            addElementsInRandomOrder(
                map = theMap,
                lowerBound = 0,
                numElements = 2 * chunkSize,
                seed = 0,
            )
        }
        val removerBody: (CustomMutableMap<Int, String>) -> Unit = { theMap ->
            Thread.sleep(1)
            removeElementsInRandomOrder(
                map = theMap,
                lowerBound = chunkSize,
                numElements = 3 * chunkSize,
                seed = 0,
            )
        }
        runConcurrencyTest(
            repeatRuns = 10,
            threadBodies = listOf(adderBody, removerBody),
            initialEntries = (chunkSize..<3 * chunkSize).map { CustomMutableMap.Entry(it, it.toString()) },
            expectedInFinalResult = (0..<chunkSize).map { CustomMutableMap.Entry(it, it.toString()) }.toSet(),
            notExpectedInFinalResult = (chunkSize * 2..<3 * chunkSize).map { CustomMutableMap.Entry(it, it.toString()) }.toSet(),
        )
    }

    @Test
    fun `eight threads add, eight threads remove`() {
        val chunkSize = 1 shl 12
        val adderBodies: List<(CustomMutableMap<Int, String>) -> Unit> = (0..<8).map { seed ->
            { theMap ->
                addElementsInRandomOrder(
                    map = theMap,
                    lowerBound = 0,
                    numElements = 2 * chunkSize,
                    seed = seed,
                )
            }
        }
        val removerBodies: List<(CustomMutableMap<Int, String>) -> Unit> = (8..<16).map { seed ->
            { theMap ->
                removeElementsInRandomOrder(
                    map = theMap,
                    lowerBound = chunkSize,
                    numElements = 3 * chunkSize,
                    seed = seed,
                )
            }
        }
        runConcurrencyTest(
            repeatRuns = 4,
            threadBodies = adderBodies + removerBodies,
            initialEntries = (chunkSize..<3 * chunkSize).map { CustomMutableMap.Entry(it, it.toString()) },
            expectedInFinalResult = (0..<chunkSize).map { CustomMutableMap.Entry(it, it.toString()) }.toSet(),
            notExpectedInFinalResult = (chunkSize * 2..<3 * chunkSize).map { CustomMutableMap.Entry(it, it.toString()) }.toSet(),
        )
    }
}
