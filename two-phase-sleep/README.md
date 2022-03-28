This dir contains solutions for the question **two-phase-sleep**.

For *java*, we have two solutions: 
1. using two barriers: **TwoPhaseSleep** and **barrier.OneShotBarrier** files.
2. using a cyclic barrier and a semaphore: **TwoPhaseSleep2** and **barrier.CyclicBarrier** files.

All the solutions above use the file **util.TwoPhaseSleepUtil**.

For *rust*, we have only one solution, that uses two Barriers. 

For *go*, we have only one solution, that uses two WaitGroups and an array of channels to communicate the sleep number that was generated to the predecessor thread. 