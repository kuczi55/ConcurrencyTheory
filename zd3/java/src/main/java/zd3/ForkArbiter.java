package zd3;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ForkArbiter {
    private static final Random rand = new Random();

    private final Semaphore sem = new Semaphore(1);

    public void pickUp(){
        try {
            sem.acquire();
        } catch(InterruptedException e) {
            e.getStackTrace();
        }
    }

    public void putDown(){
        sem.release();
    }
}
