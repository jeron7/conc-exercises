package util;

import java.util.List;
import java.util.Random;

public class TwoPhaseSleepUtil {

    public static void firstPhase(List<Integer> chosenTimers, int currentIndex) {
        int secondsToSleep = generateRandomSleepDurationInSeconds();
        System.out.println(currentThread(currentIndex) + " will sleep for " + secondsToSleep + " seconds");
        makeThreadSleep(secondsToSleep);
        System.out.println(currentThread(currentIndex) + " wake up");

        int predecessorIndex = getPredecessorIndex(currentIndex, chosenTimers.size());
        int predecessorSeconds = generateRandomSleepDurationInSeconds();
        System.out.println(currentThread(currentIndex) + " set " + predecessorSeconds + " seconds for " + currentThread(predecessorIndex) + " sleep");
        chosenTimers.set(predecessorIndex, predecessorSeconds);
    }

    public static void secondPhase(List<Integer> chosenTimers, int currentIndex) {
        int secondsToSleep = chosenTimers.get(currentIndex);

        System.out.println(currentThread(currentIndex) + " will sleep, again, for " + secondsToSleep + " seconds");
        makeThreadSleep(secondsToSleep);
        System.out.println(currentThread(currentIndex) + " wake up, again");
    }

    private static void makeThreadSleep(int seconds) {
        try {
            Thread.sleep((long) seconds * 1000);
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
        return currentIndex - 1;
    }

    private static String currentThread(int currentIndex) {
        return "Thread(" + currentIndex + ")";
    }
}
