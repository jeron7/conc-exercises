package barrier;

import java.util.concurrent.Semaphore;

public class OneShotBarrier {

    private Semaphore barrier;

    private Semaphore mutex;

    private Integer counter;

    public OneShotBarrier(int permits) {
        this.barrier = new Semaphore(0);
        this.mutex = new Semaphore(1);
        this.counter = permits;
    }

    private void countDown() throws InterruptedException {
        mutex.acquire();
        counter -= 1;
        mutex.release();
    }

    public void waitFor() throws InterruptedException {
        this.countDown();

        if (counter == 0) {
            barrier.release();
        }
        barrier.acquire();
        barrier.release();
    }
}
