package zd3;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Fork {
    private static final Random rand = new Random();

    private final Semaphore sem = new Semaphore(1);

    public boolean pickUp(){
        return sem.tryAcquire();
    }

    public void putDown(){
        sem.release();
    }
}
