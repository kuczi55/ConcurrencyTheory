package homework;

public class BlednyCounter {
    private int value;

    public BlednyCounter(int val) {
        value = val;
    }

    public void increment(BlednySemafor semafor) {
        semafor.P();
        value += 1;
        semafor.V();
    }

    public void decrement(BlednySemafor semafor) {
        semafor.P();
        value -= 1;
        semafor.V();
    }

    public int getValue() {
        return value;
    }

    public static void main(String[] args) throws InterruptedException {
        BlednySemafor semafor = new BlednySemafor();
        BlednyCounter c = new BlednyCounter(0);
        Runnable inc = () -> {
            for(int i = 0; i < 100000; i++) {
                c.increment(semafor);
            }
        };
        Runnable dec = () -> {
            for(int i = 0; i < 100000; i++) {
                c.decrement(semafor);
            }
        };

        Thread increment = new Thread(inc);
        Thread decrement = new Thread(dec);

        long start=System.currentTimeMillis();
        increment.start();
        decrement.start();

        try {
            increment.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            decrement.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long stop=System.currentTimeMillis();
        System.out.println("Czas wykonania:"+(stop-start));
        System.out.println(c.getValue());
    }
}
