import barrier.OneShotBarrier;

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
                    firstPhase(chosenTimers, currentIndex);
                    firstBarrier.waitFor();

                    secondPhase(chosenTimers, currentIndex);
                    secondBarrier.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            current.start();
        }

        secondBarrier.waitFor();
    }

    private static void firstPhase(List<Integer> chosenTimers, int currentIndex) {
        int secondsToSleep = generateRandomSleepDurationInSeconds();
        System.out.println(currentThread(currentIndex) + " will sleep for " + secondsToSleep + " seconds");
        makeThreadSleep(secondsToSleep);
        System.out.println(currentThread(currentIndex) + " wake up");

        int predecessorIndex = getPredecessorIndex(currentIndex, chosenTimers.size());
        chosenTimers.add(predecessorIndex, generateRandomSleepDurationInSeconds());
    }

    private static void secondPhase(List<Integer> chosenTimers, int currentIndex) {
        int predecessorIndex = getPredecessorIndex(currentIndex, chosenTimers.size());
        int secondsToSleep = chosenTimers.get(predecessorIndex);

        System.out.println(currentThread(currentIndex) + " will sleep, again, for " + secondsToSleep + " seconds");
        makeThreadSleep(secondsToSleep);
        System.out.println(currentThread(currentIndex) + " wake up, again");
    }

    private static void makeThreadSleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int generateRandomSleepDurationInSeconds() {
        Random random = new Random();
        return random.nextInt(6);
    }

    private static int getPredecessorIndex(int currentIndex, int chosenTimersSize) {
        if (currentIndex == 0) {
            currentIndex = chosenTimersSize;
        }
        return (int) currentIndex - 1;
    }

    private static String currentThread(int currentIndex) {
        return "Thread(" + currentIndex + ")";
    }
}
