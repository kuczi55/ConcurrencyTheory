package homework;

public class Counter {
    private int value;

    public Counter(int val) {
        value = val;
    }

    public void increment(Semafor semafor) {
        semafor.P();
        value += 1;
        semafor.V();
    }

    public void decrement(Semafor semafor) {
        semafor.P();
        value -= 1;
        semafor.V();
    }

    public int getValue() {
        return value;
    }

    public static void main(String[] args) throws InterruptedException {
        Semafor semafor = new Semafor();
        Counter c = new Counter(0);
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