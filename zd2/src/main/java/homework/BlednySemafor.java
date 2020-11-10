package homework;

class BlednySemafor {
    private boolean stan = true;
    private int czeka = 0;

    public BlednySemafor() {
    }

    public synchronized void P() {
        czeka++;
        try {
            if(!stan) {
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
