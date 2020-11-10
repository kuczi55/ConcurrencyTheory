package zd3;

import java.util.concurrent.Semaphore;

public class PhilosopherArbiter extends Thread{
    private final ForkArbiter f1, f2;
    private final int philIdx;
    private final Semaphore arbiter;
    private int amountOfDinners;
    private long thinkingTime;
    private long startThinking;
    private final int total;

    public PhilosopherArbiter(int idx, ForkArbiter left, ForkArbiter right, Semaphore arbiter, int amount) {
        philIdx = idx;
        f1 = left;
        f2 = right;
        this.arbiter = arbiter;
        amountOfDinners = amount;
        total = amount;
    }

    private void eat() throws InterruptedException {
        arbiter.acquire();
        f1.pickUp();
        f2.pickUp();
        thinkingTime += System.currentTimeMillis()-startThinking;
        System.out.println("Philosopher " + philIdx + " starts eating");
        Thread.sleep(10);
        System.out.println("Philosopher " + philIdx + " end eating");
        amountOfDinners--;
        f1.putDown();
        f2.putDown();
        arbiter.release();
    }

    public void run() {
        while (amountOfDinners > 0) {
            startThinking = System.currentTimeMillis();
            try {
                sleep(1);
                eat();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double averageThinkingTime() {
        return (double)(thinkingTime/total);
    }
}
