import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class TwoPhasesSleeper implements Runnable {

    private static final int EXCLUSIVE_UPPER_BOUND = 6;

    private int randomSleepDuration;

    private Semaphore internalSemaphore;

    private Semaphore externalSemaphore;

    private List<TwoPhasesSleeper> sleepers;

    public TwoPhasesSleeper(Semaphore externalSemaphore, List<TwoPhasesSleeper> sleepers) {
        this.externalSemaphore = externalSemaphore;
        this.internalSemaphore = new Semaphore(0);
        this.sleepers = sleepers;
    }

    public int getRandomSleepDuration() {
        return randomSleepDuration;
    }

    public Semaphore getInternalSemaphore() {
        return internalSemaphore;
    }

    private int generateRandomSleepDurationInSeconds() {
        Random random = new Random();
        return random.nextInt(EXCLUSIVE_UPPER_BOUND);
    }

    private TwoPhasesSleeper getPredecessorSleeper() {
        int index = sleepers.indexOf(this);
        if (index == 0) {
            index = sleepers.size();
        }
        return sleepers.get(index - 1);
    }

    private void secondPhase() {
        this.randomSleepDuration = generateRandomSleepDurationInSeconds();
        internalSemaphore.release();
        TwoPhasesSleeper predecessorSleeper = getPredecessorSleeper();
        try {
            predecessorSleeper.getInternalSemaphore().acquire();
            int secondsToSleep = predecessorSleeper.getRandomSleepDuration();
            System.out.println(toString() + " will sleep, again, for " + secondsToSleep);
            Thread.sleep((long) secondsToSleep * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(toString() + " wake up, again");
        }
    }

    private void firstPhase() {
        int randomSecondsToSleep = generateRandomSleepDurationInSeconds();
        System.out.println(toString() + " will sleep for " + randomSecondsToSleep);
        try {
            Thread.sleep((long) (randomSecondsToSleep * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(toString() + " wake up");
        }
    }

    @Override
    public void run() {
        firstPhase();
        secondPhase();
        externalSemaphore.release();
    }

    @Override
    public String toString() {
        return "Sleeper(" + this.sleepers.indexOf(this) + ")";
    }
}
