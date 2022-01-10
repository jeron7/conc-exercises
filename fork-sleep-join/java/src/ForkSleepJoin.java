import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ForkSleepJoin {

    public static final int MAX_TIMEOUT = 5;

    public static void main(String[] args) {
        System.out.print("Choose a number n: ");
        int n = new Scanner(System.in).nextInt();
        startThreadsAndWait(n);
        System.out.println("The number choose was: " + n);
    }

    private static void startThreadsAndWait(int n) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Thread thread = new Thread(() -> sleepAndRelease());
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sleepAndRelease() {
        Random random = new Random();
        float seconds = random.nextInt(MAX_TIMEOUT + 1);
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " will sleep for " + seconds);
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(threadName + " finished");
        }
    }
}
