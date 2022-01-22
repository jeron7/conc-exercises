import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class TwoPhaseSleep {

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Choose a number n: ");
        int n = new Scanner(System.in).nextInt();
        Counter counter = new Counter();
        startThreadsWithSharedCounter(n, counter);
        waitCounterBeN(counter, n);
        System.out.println("The number choose was: " + n);
    }

    private static void waitCounterBeN(Counter counter, int n) throws InterruptedException {
        while (counter.getValue() < n) {
            Thread.sleep(1);
        }
    }

    private static void startThreadsWithSharedCounter(int n, Counter counter) {
        Semaphore semaphore = new Semaphore(1);
        List<TwoPhasesSleeper> sleepers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            TwoPhasesSleeper sleeper = new TwoPhasesSleeper(counter, semaphore, sleepers);
            sleepers.add(sleeper);
            Thread current = new Thread(sleeper);
            current.start();
        }
    }
}
