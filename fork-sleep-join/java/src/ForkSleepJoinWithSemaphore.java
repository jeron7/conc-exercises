import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class ForkSleepJoinWithSemaphore {

    public static final int MAX_SECONDS = 5;

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Choose a number n: ");
        int n = new Scanner(System.in).nextInt();
        Semaphore semaphore = new Semaphore(-(n - 1));
        startThreadsWithSemaphore(n, semaphore);
        semaphore.acquire();
        System.out.println("The number choose was: " + n);
    }

    private static void startThreadsWithSemaphore(int n, Semaphore semaphore) {
        for (int i = 0; i < n; i++) {
            Thread current = new Thread(() -> sleepAndRelease(semaphore));
            current.start();
        }
    }

    private static void sleepAndRelease(Semaphore semaphore) {
        Random random = new Random();
        float seconds = random.nextInt(MAX_SECONDS);
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " will sleep for " + seconds);
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(threadName + " finished");
            semaphore.release();
        }
    }
}
