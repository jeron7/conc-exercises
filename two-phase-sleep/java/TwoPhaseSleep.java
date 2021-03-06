import barrier.OneShotBarrier;
import util.TwoPhaseSleepUtil;

import java.util.*;

public class TwoPhaseSleep {

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Choose a number n: ");
        int n = new Scanner(System.in).nextInt();
        startThreadsAndWait(n);
        System.out.println("The number choose was: " + n);
    }

    private static void startThreadsAndWait(int n) throws InterruptedException {
        OneShotBarrier firstBarrier = new OneShotBarrier(n);
        OneShotBarrier secondBarrier = new OneShotBarrier(n + 1);
        List<Integer> chosenTimers = new ArrayList<>(Collections.nCopies(n, 0));
        for (int i = 0; i < n; i++) {
            int currentIndex = i;
            Thread current = new Thread(() -> {
                try {
                    TwoPhaseSleepUtil.firstPhase(chosenTimers, currentIndex);
                    firstBarrier.waitFor();

                    TwoPhaseSleepUtil.secondPhase(chosenTimers, currentIndex);
                    secondBarrier.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            current.start();
        }
        secondBarrier.waitFor();
    }
}
