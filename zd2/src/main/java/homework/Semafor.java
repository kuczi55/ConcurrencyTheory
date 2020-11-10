package homework;

class Semafor {
    private boolean stan;
    private int czeka = 0;

    public Semafor() {
        this(true);
    }

    public Semafor(boolean stan) {
        this.stan = stan;
    }

    public synchronized void P() {
        czeka++;
        try {
            while(!stan) {
                this.wait();
            }
        }
        catch(InterruptedException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        this.stan = false;
        czeka--;
    }

    public synchronized void V() {
        this.stan = true;
        this.notify();
    }
}