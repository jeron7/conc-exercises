import barrier.CountDownLatch;

import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final int EXCLUSIVE_UPPER_BOUND = 6;


    /**
     * Fork Sleep Join with barrier.CountDownLatch (One-shoot barrier)
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.print("Choose a number n: ");
        int n = new Scanner(System.in).nextInt();
        startThreadsAndWaitForBarrier(n);
        System.out.println("The number choose was: " + n);
    }

    private static void startThreadsAndWaitForBarrier(int n) throws InterruptedException {
        CountDownLatch barrier = new CountDownLatch(n + 1);
        for (int i = 0; i < n; i++) {
            Thread thread = new Thread(() -> sleepWakeUpAndWaitFor(barrier));
            thread.start();
        }
        barrier.waitFor();
    }

    private static void sleepWakeUpAndWaitFor(CountDownLatch barrier) {
        Random random = new Random();
        float seconds = random.nextInt(EXCLUSIVE_UPPER_BOUND);
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " will sleep for " + seconds);
        try {
            Thread.sleep((long) (seconds * 1000));
            System.out.println(threadName + " wake up");
            barrier.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
