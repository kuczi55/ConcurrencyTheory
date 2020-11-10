package zd3;

public class Philosopher extends Thread{
    private final Fork f1, f2;
    private final int philIdx;
    private int amountOfDinners;
    private final int total;
    private long thinkingTime;
    private long startThinking;

    public Philosopher(int idx, Fork left, Fork right, int amount) {
        philIdx = idx;
        f1 = left;
        f2 = right;
        amountOfDinners = amount;
        total = amount;
    }

    private void eat() throws InterruptedException {
        if(f1.pickUp()) {
            if (f2.pickUp()) {
                thinkingTime += System.currentTimeMillis()-startThinking;
                System.out.println("Philosopher " + philIdx + " starts eating");
                Thread.sleep(10);
                System.out.println("Philosopher " + philIdx + " end eating");
                amountOfDinners--;
                f1.putDown();
                f2.putDown();
                startThinking = System.currentTimeMillis();
            } else {
                f1.putDown();
                System.out.println("Philosopher " + philIdx + " couldn't get second fork");
            }
        }
        else {
            System.out.println("Philosopher " + philIdx + " try failed");
        }
    }

    public void run() {
        startThinking = System.currentTimeMillis();
        while (amountOfDinners > 0) {
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
