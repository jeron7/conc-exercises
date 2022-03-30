This dir contains solutions for the question **two-phase-sleep**. 

For the [test 1](https://docs.google.com/document/d/1rIDYsYBL8ruJaBVpDj3CKgVd2eVQhPqE4hEBDLGwPe8/edit#):
- *java*: we have two solutions: 
    1. using two barriers: **TwoPhaseSleep** and **barrier.OneShotBarrier** files.
    2. using a cyclic barrier and a semaphore: **TwoPhaseSleep2** and **barrier.CyclicBarrier** files.

    All the solutions above use the file **util.TwoPhaseSleepUtil**.

- *rust*: we have only one solution, that uses two Barriers. 

For [test 3](https://docs.google.com/document/d/1V_gE8B719MlEBKJdCF6k9Ee-5y-jLWbFliKphyl-aj4/edit): 
- For *go*, we have only one solution, that uses two WaitGroups and an array of channels to communicate the sleep number that was generated to the predecessor thread.