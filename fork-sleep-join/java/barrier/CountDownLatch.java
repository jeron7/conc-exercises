package barrier;

import java.util.concurrent.Semaphore;

public class CountDownLatch {

    private Semaphore barrierSemaphore;

    private Semaphore counterSemaphore;

    private Integer counter;

    /**
        A one shot barrier 
     */
    public CountDownLatch(int permits) {
        this.barrierSemaphore = new Semaphore(0);
        this.counterSemaphore = new Semaphore(1);
        this.counter = permits;
    }

    private void countDown() throws InterruptedException {
        counterSemaphore.acquire();
        counter -= 1;
        counterSemaphore.release();
    }

    public void waitFor() throws InterruptedException {
        this.countDown();

        if (counter == 0) {
            barrierSemaphore.release();
        }
        barrierSemaphore.acquire();
        barrierSemaphore.release();
    }
}
