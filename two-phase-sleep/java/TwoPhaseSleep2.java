import barrier.CyclicBarrier;
import util.TwoPhaseSleepUtil;

import java.util.*;
import java.util.concurrent.Semaphore;

public class TwoPhaseSleep2 {

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Choose a number n: ");
        int n = new Scanner(System.in).nextInt();
        startThreadsAndWait(n);
        System.out.println("The number choose was: " + n);
    }

    private static void startThreadsAndWait(int n) throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(n);
        Semaphore holdMainFlow = new Semaphore(0);
        List<Integer> chosenTimers = new ArrayList<>(Collections.nCopies(n, 0));
        for (int i = 0; i < n; i++) {
            int currentIndex = i;
            Thread current = new Thread(() -> {
                try {
                    TwoPhaseSleepUtil.firstPhase(chosenTimers, currentIndex);
                    barrier.waitFor();

                    TwoPhaseSleepUtil.secondPhase(chosenTimers, currentIndex);
                    barrier.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // The problem for this solution is that all threads will release
                // So semaphore that holds main flow, at the end, will be positive
                holdMainFlow.release();
            });
            current.start();
        }

        holdMainFlow.acquire();
    }
}
