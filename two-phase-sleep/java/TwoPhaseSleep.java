import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class TwoPhaseSleep {

    public static final int EXCLUSIVE_UPPER_BOUND = 6;

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Choose a number n: ");
        int n = new Scanner(System.in).nextInt();
        Semaphore semaphore = new Semaphore(-(n - 1));
        startThreadsWithSemaphore(n, semaphore);
        semaphore.acquire();
        System.out.println("The number choose was: " + n);
    }

    private static void startThreadsWithSemaphore(int n, Semaphore semaphore) {
        List<TwoPhasesSleeper> sleepers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            TwoPhasesSleeper sleeper = new TwoPhasesSleeper(semaphore, sleepers);
            sleepers.add(sleeper);
            Thread current = new Thread(sleeper);
            current.start();
        }
    }
}
