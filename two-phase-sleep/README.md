This dir contains solutions for the question **two-phase-sleep**.

For *java*, we have two solutions: 
1. using two barriers: **TwoPhaseSleep** and **barrier.OneShotBarrier** files.
2. using a cyclic barrier and a semaphore: **TwoPhaseSleep2** and **barrier.CyclicBarrier** files.

All the solutions above use the file **util.TwoPhaseSleepUtil**.

For *rust*, we only have one solution, that uses two Barriers. 