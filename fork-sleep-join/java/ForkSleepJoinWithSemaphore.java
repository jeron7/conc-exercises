import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class ForkSleepJoinWithSemaphore {

    public static final int EXCLUSIVE_UPPER_BOUND = 6;

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Choose a number n: ");
        int n = new Scanner(System.in).nextInt();
        Counter counter = new Counter();
        startThreadsWithSharedCounter(n, counter);
        waitCountToN(n, counter);
        System.out.println("The number choose was: " + n);
    }

    private static void waitCountToN(int n, Counter counter) throws InterruptedException {
        while(counter.getValue() < n) {
            Thread.sleep(1);
        }
    }

    private static void startThreadsWithSharedCounter(int n, Counter counter) {
        Semaphore semaphore = new Semaphore(1);
        for (int i = 0; i < n; i++) {
            Thread current = new Thread(() -> sleepAndWakeUp(counter, semaphore));
            current.start();
        }
    }

    private static void sleepAndWakeUp(Counter counter, Semaphore semaphore) {
        Random random = new Random();
        float seconds = random.nextInt(EXCLUSIVE_UPPER_BOUND);
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " will sleep for " + seconds);
        try {
            Thread.sleep((long) (seconds * 1000));
            System.out.println(threadName + " wake up");
            semaphore.acquire();
            counter.count();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}
