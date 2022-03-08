import barrier.OneShotBarrier;

import java.util.Random;
import java.util.Scanner;

public class ForkSleepJoinWithSemaphore {

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Choose a number n: ");
        int n = new Scanner(System.in).nextInt();
        startThreadsWithSharedCounter(n);
        System.out.println("The number choose was: " + n);
    }

    private static void startThreadsWithSharedCounter(int n) throws InterruptedException {
        OneShotBarrier barrier = new OneShotBarrier(n + 1);
        for (int i = 0; i < n; i++) {
            Thread current = new Thread(() -> sleepAndWakeUp(barrier));
            current.start();
        }
        barrier.waitFor();
    }

    private static void sleepAndWakeUp(OneShotBarrier barrier) {
        int seconds = generateRandomSeconds();
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " will sleep for " + seconds);
        try {
            Thread.sleep(seconds * 1000);
            System.out.println(threadName + " wake up");
            barrier.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int generateRandomSeconds() {
        Random random = new Random();
        return random.nextInt(6);
    }
}
