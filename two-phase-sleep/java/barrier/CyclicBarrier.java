package barrier;

import java.util.concurrent.Semaphore;

public class CyclicBarrier {

    private Semaphore firstBarrier;

    private Semaphore secondBarrier;

    private Integer counter;

    private int permits;

    public CyclicBarrier(int permits) {
        this.firstBarrier = new Semaphore(0);
        this.secondBarrier = new Semaphore(1);
        this.counter = 0;
        this.permits = permits;
    }

    public void waitFor() throws InterruptedException {
        waitFirstBarrier();
        waitSecondBarrier();
    }

    private void waitFirstBarrier() throws InterruptedException {
        synchronized (this) {
            System.out.println(
                    String.format("The %s was entered in first barrier", Thread.currentThread().getName()));
            System.out.println("Counter: "+ counter);
        }

        synchronized(counter) {
            counter += 1;
            if (counter == permits) {
                firstBarrier.release();
                secondBarrier.acquire();
            }
        }

        firstBarrier.acquire();
        firstBarrier.release();
        System.out.println(
                String.format("The %s was exited in first barrier", Thread.currentThread().getName()));
    }

    private void waitSecondBarrier() throws InterruptedException {
        synchronized (this) {
            System.out.println(
                    String.format("The %s was entered in second barrier", Thread.currentThread().getName()));
            System.out.println("Counter: "+ counter);
        }

        synchronized (counter) {
            counter -= 1;
            if (counter == 0) {
                secondBarrier.release();
                firstBarrier.acquire();
            }
        }

        secondBarrier.acquire();
        secondBarrier.release();
        System.out.println(
                String.format("The %s was exited in second barrier", Thread.currentThread().getName()));
    }
}
